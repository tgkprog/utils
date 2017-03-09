package org.s2n.ddt.util;

import org.junit.Test;

import org.s2n.ddt.bean.UtlConf;

public class ExcelAccessTst {
	public static void main(String[] args) {
		/*before running this make sure */
		org.s2n.ddt.util.ExcelAccess ee = new org.s2n.ddt.util.ExcelAccess();
		ee.openWorkBook(new java.io.File("/src/main/TestUtf.xls"));
		String ss = ee.getCellValue("createOrder", 8, 0);
		System.out.println(ss + " :" + ((int) ss.charAt(0)) + ";");
		
		ExcelAccessTst a = new ExcelAccessTst();
		a.openWorkBook();
	}

	@Test
	public void openWorkBook() {
		org.s2n.ddt.util.ExcelAccess ee = new org.s2n.ddt.util.ExcelAccess();
		ee.openWorkBook(new java.io.File(UtlConf.getProperty("xlsPath.AgentRunnerSets")));
		String ss = ee.getSheetNames(0);
		System.out.println("Sheet first :" + ss);
		org.junit.Assert.assertNotNull("loaded ", ss);

	}

	@Test
	public void countShts() {
		org.s2n.ddt.util.ExcelAccess ee = new org.s2n.ddt.util.ExcelAccess();
		ee.openWorkBook(new java.io.File(UtlConf.getProperty("xlsPath.AgentRunnerSets")));
		// String ss = ee.getSheetNames(0);
		int i = ee.getNumberOfSheets();
		System.out.println("Sheet count :" + i);
		org.junit.Assert.assertTrue("has sheets :" + i, i > 0);

	}
}
