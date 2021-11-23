package com.oa.core.util;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @ClassName:ExcleUtil
 * @author:wsx
 * @Date:2018/12/5
 * @Time:上午 8:41
 * @Version V1.0
 * @Explain Excle导入导出
 */
public class ExcelUtil {

    public static void main(String[] args){
        importTable("C:/Users/Administrator/Desktop/低值易耗品.xls");
    }

    //导出Excel表格的方法
    public static void exportTable(String filePath, LinkedHashMap<String,Object> map){
        //创建Excel文件对应的对象
        HSSFWorkbook hwk = new HSSFWorkbook();
        //创建一个sheet表名
        //HSSFSheet hssfSheet = hwk.createSheet("工作账单");
        for(String s1 : map.keySet()){
            HSSFSheet hssfSheet = hwk.createSheet(s1);
            List<List<String>> list = (List<List<String>>)map.get(s1);
            for(int i=0;i<list.size();i++){
                HSSFRow hssfRow = hssfSheet.createRow(i);
                for(int j=0;j<list.get(i).size();j++){
                    HSSFCell cell = hssfRow.createCell(j);
                    cell.setCellValue(list.get(i).get(j));
                }

            }
        }
        //通过sheet创建一行row（行） 范围0-65535
        /*HSSFRow hssfRow1 = hssfSheet.createRow(0);
        HSSFRow hssfRow2 = hssfSheet.createRow(1);
        HSSFRow hssfRow3= hssfSheet.createRow(2);
        HSSFRow hssfRow4= hssfSheet.createRow(3);
        //通过row创建一个cell 一个cell就是一个单元格 范围0-255
        HSSFCell cell1 = hssfRow1.createCell(0);
        HSSFCell cell2 = hssfRow1.createCell(1);
        HSSFCell cell3 = hssfRow1.createCell(2);
        HSSFCell cell4 = hssfRow2.createCell(1);
        HSSFCell cell5 = hssfRow2.createCell(2);
        HSSFCell cell6 = hssfRow3.createCell(0);
        HSSFCell cell7 = hssfRow3.createCell(2);
        HSSFCell cell8 = hssfRow3.createCell(3);
        HSSFCell cell9 = hssfRow4.createCell(3);
        HSSFCell cell10 = hssfRow4.createCell(4);
        //给单元格里写入类容
        cell1.setCellValue("第1行第1列");
        cell2.setCellValue("第1行第2列");
        cell3.setCellValue("第1行第3列");
        cell4.setCellValue("第2行第2列");
        cell5.setCellValue("第2行第3列");
        cell6.setCellValue("第3行第1列");
        cell7.setCellValue("第3行第3列");
        cell8.setCellValue("第3行第4列");
        cell9.setCellValue("第4行第4列");
        cell10.setCellValue("第4行第5列");*/
        FileOutputStream fos=null;
        try {
            //fos = new FileOutputStream("e:/Excel.xls");
            fos = new FileOutputStream(filePath);
            hwk.write(fos);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //导入Excel表格的方法
    public static void importTable(){

        FileInputStream fis = null;
        try {
            fis = new FileInputStream("e:/Excel.xls");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            HSSFWorkbook hwk = new HSSFWorkbook(fis);
            HSSFSheet sheet = hwk.getSheetAt(0);
            //遍历表格中所有的行 sheet.getLastCellNum 是获取最后一个不为空的行是第几个。
            for (int i = 0; i<sheet.getLastRowNum()+1; i++) {
                if(sheet.getRow(i)==null){
                    continue;
                }

                //遍历表格中所有的单元格 sheet.getRow(i).getLastCellNum() 是获取最后一个不为空的列是第几个。

                for (int j = 0; j<sheet.getRow(i).getLastCellNum() ; j++) {
                    if(sheet.getRow(i).getCell(j)==null){
                        continue;
                    }
                    System.out.println(sheet.getRow(i).getCell(j).getStringCellValue());
                }
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //导入Excel数据
    public static LinkedHashMap<String,Object> importTable(String filePath){
        LinkedHashMap<String,Object> map = new LinkedHashMap<>();
        FileInputStream fis = null;
        try {
            //fis = new FileInputStream("e:/Excel.xls");
            fis = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            HSSFWorkbook hwk = new HSSFWorkbook(fis);
            FormulaEvaluator formulaEvaluator = new HSSFFormulaEvaluator((HSSFWorkbook) hwk);
            for(int x=0;x<hwk.getNumberOfSheets();x++){
                HSSFSheet sheet = hwk.getSheetAt(x);
                //HSSFSheet sheet = hwk.getSheetAt(0);
                //获取表的Title
                String tableTitle = sheet.getSheetName();
                //获取第一行第一列表名数据
                String tableName = String.valueOf(sheet.getRow(0).getCell(0));
                List<Map<String,Object>> list = new ArrayList<>();
                Map<String,Object> map0 = new HashMap<>();
                map0.put("tableTitle",tableTitle);
                list.add(map0);
                //主表主键
                String zbId = "";
                int row = 0;
                //遍历表格中所有的行 sheet.getLastCellNum 是获取最后一个不为空的行是第几个。
                for (int i = 0; i<sheet.getLastRowNum()+1; i++) {
                    if(sheet.getRow(i)==null){
                        zbId = "";
                        row = 0;
                        continue;
                    }

                    LinkedHashMap<String,Object> map1 = new LinkedHashMap<>();

                    Boolean flag = false;
                    if(x==0){
                        //遍历表格中所有的单元格 sheet.getRow(i).getLastCellNum() 是获取最后一个不为空的列是第几个。
                        for (int j = 0; j<sheet.getRow(1).getLastCellNum() ; j++) {
                            Cell cellKey = sheet.getRow(1).getCell(j);
                            if(cellKey!=null){
                                cellKey.setCellType(Cell.CELL_TYPE_STRING);
                            }
                            Cell cellValue = sheet.getRow(i).getCell(j);
                            if(cellValue!=null){
                                cellValue.setCellType(Cell.CELL_TYPE_STRING);
                            }

                            //获取主表所需数据，从第4行开始为所需数据
                            if(i>2){
                                if(cellValue!=null){
                                    flag = true;
                                    map1.put(cellKey.getStringCellValue(),getCellValueFormula(cellValue, formulaEvaluator));
                                }else{
                                    map1.put(cellKey.getStringCellValue(),"");
                                }
                            }
                        }
                        if(i>2 && flag){
                            list.add(map1);
                        }
                    }else{
                        for (int j = 0; j<sheet.getRow(2).getLastCellNum() ; j++) {
                            Cell cellKey = sheet.getRow(2).getCell(j);
                            if(cellKey!=null){
                                cellKey.setCellType(Cell.CELL_TYPE_STRING);
                            }
                            Cell cellValue = sheet.getRow(i).getCell(j);
                            if(cellValue!=null){
                                cellValue.setCellType(Cell.CELL_TYPE_STRING);
                                if(cellValue.getStringCellValue().equals("主表主键")){
                                    Cell cell = sheet.getRow(i).getCell(j+1);
                                    cell.setCellType(Cell.CELL_TYPE_STRING);
                                    zbId = String.valueOf(cell.getStringCellValue());
                                    row = i+2;
                                }
                            }

                            //获取子表所需数据
                            if(i>row&&!zbId.equals("")){
                                if(cellValue!=null){
                                    flag = true;
                                    map1.put(cellKey.getStringCellValue(),cellValue.getStringCellValue());
                                }else{
                                    map1.put(cellKey.getStringCellValue(),"");
                                }
                            }
                        }
                        if(i>row && !zbId.equals("") && flag){
                            map1.put("linkRecorderNO",zbId);
                            list.add(map1);
                        }
                    }
                }
                map.put(tableName,list);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(map);
        return map;
    }

    public static void downloadLocal(HttpServletResponse response, String file, String fileName) throws FileNotFoundException {
        try {
            //1)、设置响应的头文件，会自动识别文件内容
            response.setContentType("multipart/form-data");
            //2)、设置Content-Disposition
            fileName = new String(fileName.getBytes(), "ISO-8859-1");
            response.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
            //3)、输出流
            OutputStream out = response.getOutputStream();
            //4)、获取服务端生成的excel文件，这里的path等于4.8中的path
            InputStream in = new FileInputStream(new File(file));
            //5)、输出文件
            int b;
            while((b=in.read())!=-1){
                out.write(b);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //未处理公式
    public static String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getRichStringCellValue().getString().trim();
            case Cell.CELL_TYPE_NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");//非线程安全
                    return sdf.format(cell.getDateCellValue());
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case Cell.CELL_TYPE_FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    //处理公式
    public static String getCellValueFormula(Cell cell, FormulaEvaluator formulaEvaluator) {
        if (cell == null || formulaEvaluator == null) {
            return null;
        }

        if (cell.getCellType() == Cell.CELL_TYPE_FORMULA) {
            return String.valueOf(formulaEvaluator.evaluate(cell).getNumberValue());
        }
        return getCellValue(cell);
    }
}
