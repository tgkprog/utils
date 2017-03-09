package org.s2n.ddt.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;import org.apache.logging.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import org.s2n.ddt.constants.*;
import org.s2n.ddt.util.ExcelAccess;


public class DriverUtil {

	/**
	 * logger for this class
	 */
	private static Logger logger = LogManager.getLogger(DriverUtil.class);

	/**
	 * Excel parser implementation
	 */
	private ExcelAccess excelAcs;

	/**
	 * This method is used to handle empty field name.
	 * @return
	 */
	public List<String> emptyFieldName() {
		List<String> list = getListInstance();
		list.add(TestCaseConstants.FIELD_NAME_IS_MISSING);
		logger.error(TestCaseConstants.FIELD_NAME_IS_MISSING);
		return list;
	}

	/**
	 * This method is used to handle invalid actions.
	 * @param actionType
	 * @return
	 */
	public List<String> foundNoAction(String actionType) {
		List<String> list = getListInstance();
		list.add(actionType + TestCaseConstants.IS_NOT_A_VALID_ACTION);
		logger.error(actionType + TestCaseConstants.IS_NOT_A_VALID_ACTION);
		return list;
	}

	/**
	 * This method is used to handle invalid object
	 * 
	 * @param realObjectPath
	 * @param actionObject
	 * @return
	 */
	public List<String> foundNoObject(String realObjectPath,
			String actionObject) {
		List<String> list = getListInstance();
		list.add(actionObject
				+ " not found in Object Map.This test case will not be executed");
		return list;
	}

	/** TODO Improve
	 * This method is used generate the date in MMDDYYYY_HMMSS format
	 * @return
	 */
	public static String getFormattedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat(
				TestCaseConstants.DATE_FORMAT_MMDDYYYY_HMMSS);
		return sdf.format(new Date());
	}

	/**
	 * This method is used to get the list instance.
	 * @return
	 */
	private List<String> getListInstance() {
		List<String> list = new ArrayList<String>();
		list.add(TestCaseConstants.STOP);
		list.add(TestCaseConstants.FAIL);
		return list;
	}

	/**
	 * This method is used to create the List
	 * @return
	 */
	public List<String> getEmptyListInstance() {
		return new ArrayList<String>();
	}

	/**
	 * This method is used to 
	 * @param actionWB
	 * @return
	 * @throws InvalidFormatException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public Workbook createWorkBook(String actionWB)
			throws InvalidFormatException, FileNotFoundException, IOException {
		return WorkbookFactory.create(new FileInputStream(actionWB));
	}

	/**
	 * This methid is used to compare the Expected result and Actual result
	 * @param expected
	 * @param actual
	 * @return
	 */
	public String compareResults(String expected, String actual) {
		String result = null;
		if (actual.equalsIgnoreCase(TestCaseConstants.PASS)
				&& expected.equalsIgnoreCase(TestCaseConstants.PASS)) {
			result = TestCaseConstants.PASS;
		} else if (actual.equalsIgnoreCase(TestCaseConstants.FAIL)
				&& (expected.equalsIgnoreCase(TestCaseConstants.FAIL))) {
			result = TestCaseConstants.PASS;
		} else if (actual.equalsIgnoreCase(TestCaseConstants.PASS)
				&& (expected.equalsIgnoreCase(TestCaseConstants.EMPTY_STRING))) {
			result = TestCaseConstants.PASS;
		} else if (actual.equalsIgnoreCase(TestCaseConstants.FAIL)
				&& (expected.equalsIgnoreCase(TestCaseConstants.EMPTY_STRING))) {
			result = TestCaseConstants.FAIL;
		} else if ((actual.equalsIgnoreCase(TestCaseConstants.PASS))
				&& (expected.equalsIgnoreCase(TestCaseConstants.FAIL))) {
			result = TestCaseConstants.FAIL;
		} else if ((actual.equalsIgnoreCase(TestCaseConstants.FAIL))
				&& (expected.equalsIgnoreCase(TestCaseConstants.PASS))) {
			result = TestCaseConstants.FAIL;
		}
		return result;
	}

	/**
	 * This method is used to get the objects Id (object)
	 * 
	 * @param file
	 * @param sheet
	 * @param formName
	 * @return
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	
	public String getObjectPath(String sheet,
			String formName) throws InvalidFormatException, IOException {
		String objectPath;
		String curValue;
		int tcStartRow = 1;
		int tcEndRow = 1;
		int objectMaxRows = 0;
		int rowCtr;
		String foundStart = TestCaseConstants.STRING_FALSE;
		String foundEnd = TestCaseConstants.STRING_FALSE;
		String foundEndSuite = TestCaseConstants.STRING_FALSE;
		String objecFound = TestCaseConstants.STRING_FALSE;
		String realPath = null;
		
		while (true) {
			for (rowCtr = tcStartRow; rowCtr <= excelAcs.getTotalRowCount(sheet); rowCtr++) {
				curValue = excelAcs.getCellValue(sheet, rowCtr, 0);
				if (foundStart.equals(TestCaseConstants.STRING_FALSE)) {
					if (curValue.equals(TestCaseConstants.OBJECT_MAP_START)) {
						tcStartRow = rowCtr + 1;
						foundStart = TestCaseConstants.STRING_TRUE;
					}
				}
				if (foundEndSuite.equals(TestCaseConstants.STRING_FALSE)) {
					if (curValue.equals(TestCaseConstants.OBJECT_MAPPING_ENDS)) {
						foundEndSuite = TestCaseConstants.STRING_TRUE;
						objectMaxRows = rowCtr + 1;
					}
				}
			}
			if (foundEndSuite.equals(TestCaseConstants.STRING_FALSE)) {
				break;
			}
			if (foundStart.equals(TestCaseConstants.STRING_FALSE)) {
				break;
			}
			for (rowCtr = tcStartRow; rowCtr <= excelAcs
					.getTotalRowCount(sheet); rowCtr++) {
				curValue = excelAcs.getCellValue(sheet, rowCtr, 0);
				if (curValue.equals(TestCaseConstants.OBJECT_MAP_END)) {
					tcEndRow = (rowCtr - 1);
					foundEnd = TestCaseConstants.STRING_TRUE;
					break;
				}
				String formNameColumn = excelAcs.getCellValue(sheet,
						rowCtr, 0);
				objectPath = excelAcs.getCellValue(sheet, rowCtr, 1);
				if (formNameColumn.equals(formName)) {
					objecFound = TestCaseConstants.STRING_TRUE;
					realPath = objectPath;
				}
			}
			if (objecFound.equals(TestCaseConstants.STRING_FALSE)) {
				realPath = TestCaseConstants.NO_OBJECT_FOUND;
			}
			if (foundEnd.equals(TestCaseConstants.STRING_FALSE)) {
				break;
			}
			tcStartRow = (tcEndRow + 2);
			foundStart = TestCaseConstants.STRING_FALSE;
			foundEnd = TestCaseConstants.STRING_FALSE;
		}
		return realPath;
	}

	public ExcelAccess getExcelAcs() {
	
		return excelAcs;
	}

	public void setExcelAcs(ExcelAccess excelAcs) {
	
		this.excelAcs = excelAcs;
	}

	/**
	 * This method is used to check the dependency of the current test case.
	 * @param actionWorkbook
	 * @param groups
	 * @param dependency
	 * @return
	 */
	/*public static String checkfordependency(String actionWorkbook,
			String groups, int dependency) {
		int rowstop = 2, rowRes = 2, currTestNo = 0;
		String resultCheck;
		boolean foundFail = false, foundNoRes = false, foundTCEnd = false;
		System.out.println("Groups  "+groups +"   dependency  "+dependency);
		switch (dependency) {
		case 0:
			resultCheck = TestCaseConstants.SUCCESS;
			break;
		default:
			for (int rowDepend = 1; rowDepend < excelAcs
					.Get_Total_Row_Count_In_Sheet(actionWorkbook, groups) - 1; rowDepend++) {
				int currentValue = (int) excelAcs.Get_Cell_Numeric_Value(
						actionWorkbook, groups, rowDepend, 1);
				if (currentValue != 0) {
					currTestNo = currentValue;
				}
				if (currTestNo == dependency) {
					excelAcs.Get_Cell_Value(actionWorkbook, groups, rowDepend,
							0);
					String dependentResult = excelAcs.Get_Cell_Value(
							actionWorkbook, groups, rowRes, 13);
					rowRes = rowRes + 1;
					String curTCValuedependent = excelAcs.Get_Cell_Value(
							actionWorkbook, groups, rowstop, 0);
					if (curTCValuedependent.equals(TestCaseConstants.TC_END)) {
						foundTCEnd = true;
						break;
					} else if (!(dependentResult
							.equalsIgnoreCase(TestCaseConstants.FAIL))
							&& !(dependentResult
									.equalsIgnoreCase(TestCaseConstants.PASS))) {
						foundNoRes = true;
						break;
					} else if (dependentResult
							.equalsIgnoreCase(TestCaseConstants.FAIL)) {
						foundFail = true;
						break;
					}
				}
				rowstop = rowstop + 1;
				if (foundTCEnd) {
					break;
				}
			}
			if (foundFail) {
				resultCheck = TestCaseConstants.UNSUCCESS;
			} else if (foundNoRes) {
				resultCheck = TestCaseConstants.NO_RESULT;
			} else {
				resultCheck = TestCaseConstants.SUCCESS;
			}
			break;
		}
		return resultCheck;
	}
}*/
	
	
	public String checkfordependency(String groups, int dependency) {
			  int rowstop = 2, rowRes = 2, currTestNo = 0;
			  String resultCheck;
			  boolean foundFail = false, foundNoRes = false, foundTCEnd = false;
			  switch (dependency) {
			  case 0:
			   resultCheck = TestCaseConstants.SUCCESS;
			   break;
			  default:
			   for (int rowDepend = 1; rowDepend < excelAcs
			     .getTotalRowCount(groups) - 1; rowDepend++) {
			    int currentValue = (int) excelAcs.getCellNumericValue(groups, rowDepend, 1);
			    if (currentValue != 0) {
			     currTestNo = currentValue;
			    }
			    if (currTestNo == dependency) {
			     
			     excelAcs.getCellValue(groups, rowDepend,
			       0);
			     String dependentResult = excelAcs.getCellValue(groups, rowRes, 17);
			     
			     String curTCValuedependent = excelAcs.getCellValue(groups, rowstop, 0);
			     if (curTCValuedependent.equals(TestCaseConstants.TC_END)) {
			      foundTCEnd = true;
			      break;
			     } else if (!(dependentResult
			       .equalsIgnoreCase(TestCaseConstants.FAIL))
			       && !(dependentResult
			         .equalsIgnoreCase(TestCaseConstants.PASS))) {
			      foundNoRes = true;
			      break;
			     } else if (dependentResult
			       .equalsIgnoreCase(TestCaseConstants.FAIL)) {
			      foundFail = true;
			      break;
			     }
			    }
			    rowstop = rowstop + 1;
			    rowRes = rowRes + 1;
			    if (foundTCEnd) {
			     break;
			    }
			   }
			   if (foundFail) {
			    resultCheck = TestCaseConstants.UNSUCCESS;
			   } else if (foundNoRes) {
			    resultCheck = TestCaseConstants.NO_RESULT;
			   } else {
			    resultCheck = TestCaseConstants.SUCCESS;
			   }
			   break;
			  }
			  return resultCheck;
			 }
	
}
