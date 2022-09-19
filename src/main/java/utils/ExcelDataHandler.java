package utils;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * The ExcelDataHandler class enables you to access
 * XSSFSheet functionalities to test broker portal
 * application using data driven approach.
 */
public class ExcelDataHandler {

    private static Logger logger = Logger.getLogger(ExcelDataHandler.class);
    static XSSFWorkbook workbook;
    static XSSFSheet sheet;
    static FileInputStream file;
    boolean isSheetExist = false;
    static String fileName;

    public ExcelDataHandler(String excelPath, String sheetName) {

        try{
            fileName = excelPath;
            file = new FileInputStream(excelPath);
            workbook = new XSSFWorkbook(file);

            // Check if the workbook is empty or not
            if (workbook.getNumberOfSheets() != 0) {
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    if (workbook.getSheetName(i).equals(sheetName)) {
                        sheet = workbook.getSheet(sheetName);
                        isSheetExist = true;
                        break;
                    }
                }
                if(isSheetExist == false){
                    logger.info(sheetName + " does not exist. Creating " + sheetName + " sheet");
                    sheet = workbook.createSheet(sheetName);
                }
            }
            else {
                // Create new sheet to the workbook if empty
                sheet = workbook.createSheet(sheetName);
            }
            file.close();
        }catch (Exception e){
            logger.error("Failed to read the excel sheet in:: ExcelDataReader" + e.getMessage());
            e.getStackTrace();
        }
    }

    public static Object getData(int rowNumber, int colNumber){

        Object value = null;
        try {
            DataFormatter formatter = new DataFormatter();
            value = formatter.formatCellValue(sheet.getRow(rowNumber).getCell(colNumber));
        }catch (Exception e){
            logger.error("Failed to get data of the excel sheet in:: getData" + e.getMessage());
            e.getStackTrace();
        }
        return value;
    }

    public static int getRowCount(){

        int rowCount = 0;
        try {
            rowCount = sheet.getPhysicalNumberOfRows();
        }catch (Exception e){
            logger.error("Failed to get row count of the excel sheet in:: getRowCount" + e.getMessage());
            e.getStackTrace();
        }
        return rowCount;
    }

    public static int getColCount(){

        int colCount = 0;
        try {
            XSSFRow row = sheet.getRow(0); //default row = 0
            colCount = row.getLastCellNum();
        }catch (Exception e){
            logger.error("Failed to get column count of the excel sheet in:: getColCount" + e.getMessage());
            e.getStackTrace();
        }
        return colCount;
    }

    public static Object[][] getTableArray() {

        Object[][] tabArray = null;
        try {

            int startRow = 1;
            int startCol = 0;
            int ci, cj;

            int noOfRows = getRowCount();
            int noOfCols  = getColCount();

            tabArray = new Object[noOfRows-1][noOfCols];
            ci = 0;
            for (int i = startRow; i < noOfRows; i++, ci++) {
                cj = 0;
                for (int j = startCol; j < noOfCols; j++, cj++) {
                    tabArray[ci][cj] = getData(i, j);
                }
            }
        } catch (Exception e){
            logger.error("Failed to get table array of the excel sheet in:: getTableArray - " + e.getMessage());
            e.getStackTrace();
        }
        return tabArray;
    }

    public Object[][] getTableDataInMap(){

        int lastRowNumber = sheet.getLastRowNum();
        int lastCellNumber = sheet.getRow(0).getLastCellNum();
        Object[][] obj = new Object[lastRowNumber][1];

        try {
            for (int i = 0; i < lastRowNumber; i++) {
                Map<Object, Object> dataMap = new HashMap<>();
                for (int j = 0; j < lastCellNumber; j++) {
                    dataMap.put(getData(0, j), getData(i+1, j));
                }
                obj[i][0] = dataMap;
            }
        }catch (Exception e){
            logger.error("Failed to get table array of the excel sheet in:: getTableDataInMap - " + e.getMessage());
        }
        return  obj;
    }

    public void writeDataIntoExcelSheet(Map<String, Object[]> dataValue) throws IOException {

        Set<String> keyId = dataValue.keySet();

        int rowId = 0;

        // writing the data into the sheets...
        for (String key : keyId) {

            XSSFRow row = sheet.createRow(rowId++);
            Object[] objectArr = dataValue.get(key);
            int cellId = 0;

            for (Object obj : objectArr) {
                Cell cell = row.createCell(cellId++);
                cell.setCellValue((String)obj);
            }
        }
        // .xlsx is the format for Excel Sheets...
        // writing the workbook into the file...
        FileOutputStream out = new FileOutputStream(
                new File(fileName));

        workbook.write(out);
        out.close();
        logger.info(fileName + " written successfully");
    }

    public void updateExcelCell(int row, int col, String setValue) throws IOException {

        Cell cell = null;

        //Retrieve the row and check for null
        XSSFRow sheetrow = sheet.getRow(row);
        if(sheetrow == null){
            sheetrow = sheet.createRow(row);
        }
        //Update the value of cell
        cell = sheetrow.getCell(col);
        if(cell == null){
            cell = sheetrow.createCell(col);
        }
        cell.setCellValue(setValue);

        FileOutputStream outFile = new FileOutputStream(
                new File(fileName));

        workbook.write(outFile);
        outFile.close();
        logger.info(fileName + " updated successfully");
    }
}
