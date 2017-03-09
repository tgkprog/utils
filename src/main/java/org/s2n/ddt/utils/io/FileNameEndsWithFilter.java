package org.s2n.ddt.utils.io;

import java.io.File;

public class FileNameEndsWithFilter implements java.io.FileFilter {
	private String filtr;
	private boolean folderAlso;
	

	
	//private String sourceDirectory = "/Users/muktar/Documents/JBoss/jboss-5.1.0.GA/server/default/deploy/ROOT.war/reports/r/";
	//private File f = new File(sourceDirectory);

	public String getSubString(String sample, String myDir){

	String path = sample.substring(myDir.length()); //"r/2013aug05run1_DFJE/index.html";

	String name = path.replace("/index.html", "") ;
	
	name = name.replace("r/", "");  // name = "2013_aug_05run1_DFJE";

	return name;
	}
	
	public FileNameEndsWithFilter(String filte, boolean folderAlso){
		this.filtr = filte;
		this.folderAlso = folderAlso;
	}

	public boolean accept(File pathname) {
		if(pathname.isDirectory() && folderAlso){
			return true;
		}
		if(pathname.getName().endsWith(filtr)){
			return true;
		}
		return false;
	}
	
	
	
}
