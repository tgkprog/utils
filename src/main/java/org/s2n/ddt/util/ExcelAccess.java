package org.s2n.ddt.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelAccess {
	private static final Logger logger = LogManager.getLogger(ExcelAccess.class);
	
	private static Workbook wb;

	
	

	/**
	 * This method is to open a workbook excel.
	 * 
	 * @param workbookPath
	 *            the workbook path
	 * @return the workbook
	 */

	public void openWorkBook(File workbookPath) {
		closeIfOpen();
		InputStream inputStream = null;
		logger.info("Opening workbook");
		try {
			inputStream = new BufferedInputStream(new FileInputStream(
					workbookPath));
			wb = WorkbookFactory.create(inputStream);

		} catch (Exception e) {
			logger.log(Level.ERROR, "openWorkBook opening " + workbookPath
					+ ", " + e, e);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();

				} catch (Exception e) {
					logger.log(Level.ERROR, "openWorkBook Closing"
							+ workbookPath + ", " + e, e);

				}
			}
		}
	}

	private void closeIfOpen() {
		try {
			if (wb != null) {
				// WorkbookFactory.
				
			}
		} catch (Throwable e) {
			logger.log(Level.ERROR, "closeIfOpen " + e, e);
		}
	}

	/**
	 * This method is used to get the cell value for string format.
	 * 
	 * @param iRow
	 *            the row no
	 * @param iCol
	 *            the column no
	 * @param sSheet
	 *            the sheet name
	 * 
	 * @return the string
	 */

	public String getCellValue(String sheetName, int iRow, int iCol) {

		Cell oCell = null;
		String sValue = null;
		try {
			oCell = getCell(wb, sheetName, iRow, iCol);

			if (oCell == null) {
				sValue = "";
			} else {
				sValue = oCell.getStringCellValue();
			}
		} catch (java.lang.IllegalStateException e) {
			sValue = String.valueOf((int) oCell.getNumericCellValue());
		} catch (java.lang.NullPointerException e) {
			sValue = "";
		}
		if (sValue != null){
			return sValue;
		}
		else{
			return "-EOF-";
		}
	}

	/**
	 * This method is used to get the cell value for numeric format.
	 * 
	 * @param iRow
	 *            the row no
	 * @param iCol
	 *            the column no
	 * @param sSheet
	 *            the sheet name
	 * 
	 * @return the double
	 */

	public double getCellNumericValue(String sheetName, int iRow, int iCol) {
		Cell oCell;
		double sValue = 0;
		try {
			oCell = getCell(wb, sheetName, iRow, iCol);
			if (oCell == null) {
				sValue = 0;
			} else {
				sValue = oCell.getNumericCellValue();
			}
		} catch (NullPointerException e) {
			sValue = 0;
		}
		return sValue;
	}

	/**
	 * This method is used to get the total row count in the sheet.
	 * 
	 * @param sSheet
	 *            the sheet name
	 * 
	 * @return the int
	 */

	public int getTotalRowCount(String sheetName) {
		Sheet oSheet;
		oSheet = wb.getSheet(sheetName);
		return oSheet.getLastRowNum();
	}

	/**
	 * This method is used to get the total column count in the sheet.
	 * 
	 * @param sSheet
	 *            the sheet name
	 * 
	 * @return the int
	 */
	public int getTotalColumnCount(String sheetName) {
		int columncount = 0;
		Sheet oSheet;
		oSheet = wb.getSheet(sheetName);
		for( int colcnt = 0 ; colcnt <= oSheet.getLastRowNum();colcnt++){
			columncount = columncount + 1; 
		}
		/*for (Row row : oSheet) {
			System.out.println(row.getFirstCellNum());
			columncount = row.getLastCellNum();
		}*/
			
		return columncount;
	}

	/**
	 * Gets the number of sheets.
	 * 
	 * @return the number of sheets
	 */
	public int getNumberOfSheets() {
		return wb.getNumberOfSheets();
	}
	
	
	/**
	 * isSheetExists.
	 * 
	 * @returns whether sheet exists
	 */
	public boolean isSheetExists(String sheetName) {
		Sheet oSheet = null;
		if(sheetName != null){
			
			oSheet = wb.getSheet(sheetName);
		}
			if(oSheet!= null ){
				return true;
			}
			else{			
				return false;
			}
	}

	/**
	 * Gets the sheet names.
	 * 
	 * @param index
	 *            the index
	 * @return the sheet names
	 */
	public String getSheetNames(int index) {
		return wb.getSheetName(index);
	}

	/**
	 * This method is used to get the cell value in the sheet.
	 * 
	 * @param wb
	 *            the workbook excel
	 * @param sSheet
	 *            the sheet name
	 * @param iRow
	 *            the row no
	 * @param iCol
	 *            the column no
	 * @return the cell
	 */

	public Cell getCell(Workbook wb, String sheetName, int iRow, int iCol) {
		Sheet oSheet;
		Row oRow;
		Cell oCell;

		oSheet = wb.getSheet(sheetName);
		oRow = oSheet.getRow(iRow);
		oCell = oRow.getCell(iCol);
		return oCell;
	}

	public void writeToExcel(String fileName, String sheetNum, int irow,
			int icol, String value) throws IOException {
		Sheet sheet = wb.getSheet(sheetNum);
		Row row = sheet.getRow(irow);
		Cell cell = row.getCell(icol);

		HSSFCellStyle style = (HSSFCellStyle) wb.createCellStyle();
		if (cell == null){
			cell = row.createCell(icol);
		}
		HSSFFont font = (HSSFFont) wb.createFont();
		font.setFontHeightInPoints((short) 13);
		font.setFontName("Arial");
		if (value.equalsIgnoreCase("FAIL")) {
			font.setColor(HSSFColor.RED.index);
		} else if (value.equalsIgnoreCase("PASS")) {
			font.setColor(HSSFColor.GREEN.index);
		}
		style.setFont(font);
		cell.setCellStyle(style);
		cell.setCellValue(value);
		FileOutputStream fout = null;
        try{
		fout = new FileOutputStream(fileName);
		wb.write(fout);
	
        }finally{
        	fout.close();
        }
		
	}
}
