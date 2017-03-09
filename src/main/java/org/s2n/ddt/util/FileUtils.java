package org.s2n.ddt.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;

public class FileUtils {

	private static Logger logger = LogManager.getLogger(FileUtils.class);

	/**
	 * Copying files
	 */

	public static void fileCopy(String inputFile, String outputFile) {
		logger.log(Level.INFO, "in [" + inputFile + "] o [" + outputFile);
		File in = new File(inputFile);
		File out = new File(outputFile);
		fileCopy(in, out);

	}

	public static void fileCopy(File inputFile, File outputFile) {

		try {

			BufferedInputStream reader = new BufferedInputStream(new FileInputStream(inputFile), 4096);
			//FileUtils.stream(outputFile);
			BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(outputFile), 4096);
			byte[] buf = new byte[4096];
			int byteRead;
			while ((byteRead = reader.read(buf, 0, 4096)) >= 0) {
				writer.write(buf, 0, byteRead);
			}
			reader.close();
			writer.flush();
			writer.close();

		}catch (FileNotFoundException e) {			
			logger.log(Level.ERROR, "err copy " + e + ", in :" + inputFile + ", out :" + outputFile + ".", e);
		}  catch (IOException e) {		
			logger.log(Level.ERROR, "err copy " + e + ", in :" + inputFile + ", out :" + outputFile + ".", e);
		}
	}

	/**
	 * File output stream
	 * 
	 * @param output
	 *            file
	 */
	private static FileOutputStream stream(String outputFile) throws FileNotFoundException {

		return new FileOutputStream(new File(outputFile));
	}

	/**
	 * Zip it
	 * 
	 * @param zipFile
	 *            output ZIP file location
	 */
	public static void zipIt(String source, String zipFile, List<String> fileList) {

		byte[] buffer = new byte[1024];
		try {
			ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile));
			for (String file : fileList) {
				zos.putNextEntry(new ZipEntry(file));
				FileInputStream in = new FileInputStream(source + File.separator + file);
				int len;
				while ((len = in.read(buffer)) > 0) {
					zos.write(buffer, 0, len);
				}
				in.close();
			}
			zos.closeEntry();
			zos.close();
		} catch (IOException ex) {
			ex.printStackTrace();
			logger.error("Error in FileUtils  :" + ex + "]");
		}
	}

	/**
	 * Traverse a directory and get all files, and add the file into fileList
	 * 
	 * @param node
	 *            file or directory
	 */
	public static void generateFileList(File node, String source, List<String> fileList) {

		if (node.isFile()) {
			fileList.add(FileUtils.generateZipEntry(node.getAbsolutePath(), source));
		}

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename), source, fileList);
			}
		}
	}

	/**
	 * delete files with extension
	 * 
	 * @param file
	 *            file path
	 * @param string
	 *            extension
	 * 
	 */
	public static void deleteFilesWithExtension(final String directoryName, final String extension) {

		final File dir = new File(directoryName);
		final String[] allFiles = dir.list();
		for (final String file : allFiles) {
			if (file.endsWith(extension)) {
				new File(directoryName + "/" + file).delete();
			}
		}
	}

	/**
	 * Format the file path for zip
	 * 
	 * @param file
	 *            file path
	 * @return Formatted file path
	 */
	private static String generateZipEntry(String file, String sourceFolder) {

		return file.substring(sourceFolder.length() + 1, file.length());
	}

	public static String getBaseName(String name) {
		if(name == null){
			return null;
		}
		int i = name.lastIndexOf('.');
		if(i < 0)
		{return name;}
		return name.substring(0, i);
	}

}
