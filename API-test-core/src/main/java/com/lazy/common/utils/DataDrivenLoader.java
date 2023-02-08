package com.lazy.common.utils;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// This class is used to load test data from json file, and provide result as data provider for test case
public class DataDrivenLoader {
    public static Object[][] loadExcelData(String filePath, String suiteName, String caseName) {
        Object[][] data;
        ResourceFileUtil resourceFileUtil = new ResourceFileUtil();
        InputStream inputStream = resourceFileUtil.getInputStream("testdata", filePath);
        XSSFSheet sheet;

        try {
//            String wholePath = System.getProperty("user.dir") + File.separator +"testdata" + File.separator + filePath;
//            System.out.println("file path in loadExcelData: " + wholePath);
//            FileInputStream inputStream = new FileInputStream(wholePath);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            // by default get first sheet
            if(suiteName == null || suiteName.isEmpty()) {
                sheet = workbook.getSheetAt(0);
            } else {
                sheet = workbook.getSheet(suiteName);
            }
            if(sheet == null) {
                throw new IllegalArgumentException(String.format("can not get sheet: %s in excel data file", suiteName));
            }

            // get row index list
            List<Integer> caseRowIndexList = getRowIndexList(sheet, caseName);
            int rowCount = caseRowIndexList.size();

            // get column count
            int columnCount = getColumnCount(sheet, caseRowIndexList);

            data = new Object[rowCount][columnCount];
            // reade data from excel to data provider object
            int i = 0;
            FormulaEvaluator formulaEvaluator = workbook.getCreationHelper().createFormulaEvaluator();
            for(Integer index : caseRowIndexList) {
                XSSFRow tempRow2 = sheet.getRow(index);
                for(int j = 1;j <= columnCount;j++) {
                    Cell cell = tempRow2.getCell(j);
                    if(cell != null) {
                        switch(formulaEvaluator.evaluateInCell(cell).getCellType()) {
                            case Cell.CELL_TYPE_NUMERIC:
                                data[i][j-1] = String.valueOf(cell.getNumericCellValue());
                                break;
                            case Cell.CELL_TYPE_STRING:
                                data[i][j-1] = cell.getStringCellValue();
                                break;
                            default:
                                data[i][j-1] = cell.getStringCellValue();
                                break;
                        }
                    }
                }
                i++;
            }
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void updateTestData(String filePath, String suiteName, String caseName, String columnName, String value) {
        ResourceFileUtil resourceFileUtil = new ResourceFileUtil();
        InputStream inputStream = resourceFileUtil.getInputStream("testdata", filePath);
        XSSFSheet sheet;

        try {
//            String wholePath = System.getProperty("user.dir") + File.separator +"testdata" + File.separator + filePath;
//            System.out.println("file path in updateTestData: " +wholePath);
//            FileInputStream inputStream = new FileInputStream(wholePath);
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

            // by default get first sheet
            if (suiteName == null || suiteName.isEmpty()) {
                sheet = workbook.getSheetAt(0);
            } else {
                sheet = workbook.getSheet(suiteName);
            }

            // get row index list
            List<Integer> caseRowIndexList = getRowIndexList(sheet, caseName);

            // get column count
            int columnCount = getColumnCount(sheet, caseRowIndexList);

            // get specific column index
            int columnIndex = 0;
            for(int i = 1;i <= columnCount;i++) {
                XSSFRow row = sheet.getRow(caseRowIndexList.get(0) - 1);
                if(row.getCell(i).getStringCellValue().equals(columnName)) {
                    columnIndex = i;
                }
            }

            //update test data in excel
            for(Integer rowIndex : caseRowIndexList) {
                XSSFRow tempRow2 = sheet.getRow(rowIndex);
                Cell cell = tempRow2.getCell(columnIndex);
                if(cell.getStringCellValue().equals("{}"))
                {
                    cell.setCellValue(value);
                    break;
                }
            }

            FileOutputStream outputStream = resourceFileUtil.getOutPutStream("testdata", filePath);
//            FileOutputStream outputStream = new FileOutputStream(wholePath);
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //record the row index of specific case
    private static List<Integer> getRowIndexList(XSSFSheet sheet, String caseName) {
        int totalRowCount = sheet.getPhysicalNumberOfRows();
        List<Integer> rowIndexList = new ArrayList<>();
        for (int i = 0; i < totalRowCount; i++) {
            Cell cell = sheet.getRow(i).getCell(0);
            if (cell == null || cell.getStringCellValue() == null || cell.getStringCellValue().isEmpty()) {
                continue;
            }

            if (cell.getStringCellValue().equals(caseName)) {
                rowIndexList.add(i);
            }
        }
        return rowIndexList;
    }

    //get the column count, which is th parameter count
    private static int getColumnCount(XSSFSheet sheet, List<Integer> rowIndexList) {
        int columnCount = 0;
        XSSFRow tempRow1 = sheet.getRow(rowIndexList.get(0));
        for(int i = 0;i < tempRow1.getPhysicalNumberOfCells();i++) {
            Cell cell = tempRow1.getCell(i);
            if(cell == null || cell.getStringCellValue() == null || cell.getStringCellValue().isEmpty()){
                columnCount = i - 1;
                break;
            }
        }
        if(columnCount == 0) {
            columnCount = tempRow1.getPhysicalNumberOfCells() - 1;
        }
        return columnCount;
    }
}
