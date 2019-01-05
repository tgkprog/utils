package org.s2n.ddt.utils.io;

import java.io.File;

public class FileNameEndsWithFilter implements java.io.FileFilter {
	private String filtr;
	private boolean folderAlso;
	
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
