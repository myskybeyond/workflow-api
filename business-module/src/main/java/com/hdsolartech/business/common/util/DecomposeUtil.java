package com.hdsolartech.business.common.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.alibaba.fastjson.JSONObject;
import com.hdsolartech.business.common.util.luckysheet.XlsUtil;
import com.hdsolartech.business.common.util.luckysheet.vo.GridRecordDataModel;
import com.hdsolartech.business.common.util.vo.DecomposeOutVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.util.*;

/**
 * @ClassName DecomposeUtil
 * @Description  分解清单为资料清单与材料清单
 * @Author zyf
 * @create 2024/6/25 15:07
 * @Version 1.0
 */
@Slf4j
public class DecomposeUtil {
    /**
     * 解析bom清单信息
     * @param rawData
     * @param  filePath 文件路径
     * @param  name 名称
     * @return
     */
    public DecomposeOutVo decomposeBom(List<JSONObject> rawData, String filePath ,String name){
        try{
            DecomposeOutVo outVo = new DecomposeOutVo();
            //生成本地excel
            String bomName = name+"_材料清单"+ System.currentTimeMillis();
            OutputStream out = new FileOutputStream(filePath+bomName+".xlsx");
            XlsUtil.exportXlsFile(out,true,rawData);
            //关闭文件输出流
            out.close();
            //poi 解析
            String deliverName = name+"_发货清单"+ System.currentTimeMillis();
            File deliverFile =  FileUtil.copy(filePath+bomName+".xlsx", filePath+deliverName+".xlsx", true);
            List<JSONObject> deliverRes =  genDeliverData(deliverFile);
            //删除excel
            deliverFile.delete();
            outVo.setDeliverName(deliverName);
            outVo.setDeliverFileJson(deliverRes);
            File bomFile = new File(filePath+bomName+".xlsx");
            List<JSONObject> bomRes = genBomData(bomFile);
            bomFile.delete();
            outVo.setBomName(bomName);
            outVo.setBomFileJson(bomRes);
            return outVo;
        }catch(Exception e){
            log.error("生成材料与发货清单出现异常",e);
            return null;
        }
    }


    /**
     * 解析发货清单
     * @param deliverFile
     * @return
     * @throws Exception
     */
    public List<JSONObject> genDeliverData(File deliverFile) throws  Exception{
        ZipSecureFile.setMinInflateRatio(-1.0d);
        Workbook workbook = WorkbookFactory.create(deliverFile);
        //取出第一个工作表
        Sheet sheet = workbook.getSheetAt(0);
        //转化sheet中的cell公式修改为值
        transFormulaToVal(sheet);
        //修改第一个工作表的名称
        workbook.setSheetName(0, "发货清单");
        for (int i = 3; i <= sheet.getLastRowNum(); i++) {
            Boolean  flag = true;
            while(flag) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell cell = row.getCell(1);
                    if (cell == null || cell.getCellType() == CellType.BLANK || StrUtil.isEmpty(cell.getStringCellValue())) {
                        int shiftRowNum = row.getRowNum() + 1;
                        sheet.removeRow(row);
//                        log.info(" sheet.getLastRowNum():"+ sheet.getLastRowNum());
//                        log.info(" shiftRowNum:"+ shiftRowNum);
                        if(shiftRowNum <=sheet.getLastRowNum() ){
                            sheet.shiftRows(shiftRowNum, sheet.getLastRowNum(), -1);
                        }else {
                            flag = false;
                        }

                    }else {
                        flag = false;
                    }
                }
            }
        }
        //删除第三列与第四列
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                //获取第三列
                Cell cell2 = row.getCell(2);
                //获取第四列
                Cell cell3 = row.getCell(3);
                row.removeCell(cell2);
                row.removeCell(cell3);
                row.shiftCellsLeft(4,  row.getLastCellNum(), 2);
            }
        }
        //修改名称
        sheet.getRow(2).getCell(1).setCellValue("名称");
        //重新生成序号
        for (int i = 3; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                row.getCell(0).setCellValue(i-2);
            }
        }
        GridRecordDataModel modal =  XlsUtil.readExcel(workbook);
        if(modal == null){
            throw new Exception("发货清单生成luckySheet出现异常");
        }
        List<JSONObject> list = new ArrayList<>();
        list.add(modal.getJson_data());
        return list;
    }
    /**
     * 解析材料清单
     * @param bomFile
     * @return
     * @throws Exception
     */
    public List<JSONObject> genBomData(File bomFile) throws  Exception{
        ZipSecureFile.setMinInflateRatio(-1.0d);
        Workbook workbook = WorkbookFactory.create(bomFile);
        //取出第一个工作表
        Sheet sheet = workbook.getSheetAt(0);
        //转化sheet中的cell公式修改为值
        transFormulaToVal(sheet);
        //修改第一个工作表的名称
        workbook.setSheetName(0, "材料清单");
        //修改名称
        sheet.getRow(2).getCell(1).setCellValue("名称");

        //第一种条件 当前行 第一列有值  下一行 第一列无值 需要删除当前行
        //第二种条件 当前行 第一列无值  第二列有值  下一行 第三列有值 需要删除当前行  下一行第三列无值 需要更新第二列的值到第一行中
        //第三种条件 当前行 第二列有值  下一列无值
        for (int i = 3; i <= sheet.getLastRowNum(); i++) {
            Row row1 = sheet.getRow(i);
            //第一列
            Cell cell1 = row1.getCell(1);
            //第二列
            Cell cell2 = row1.getCell(2);
            //第三列
            //第二列
            Cell cell3 = row1.getCell(3);
            //第一列有值
            if (cell1 != null && cell1.getCellType() != CellType.BLANK && StrUtil.isNotEmpty(cell1.getStringCellValue())) {
                //需要判断下一行
                if(i != sheet.getLastRowNum()){
                    //不为最后一行
                    Row row2 = sheet.getRow(i+1);
                    Cell cell22  = row2.getCell(2);
                    if (cell22 != null && cell22.getCellType() != CellType.BLANK && StrUtil.isNotEmpty(cell22.getStringCellValue())){
                         //删除当前行
                        sheet.removeRow(row1);
                    }
                }
            }else{
                //第二列有值
                if (cell2 != null && cell2.getCellType() != CellType.BLANK && StrUtil.isNotEmpty(cell2.getStringCellValue())){
                    //需要判断下一行
                    if(i != sheet.getLastRowNum()){
                        //不为最后一行
                        Row row2 = sheet.getRow(i+1);
                        Cell cell23  = row2.getCell(3);
                        if (cell23 != null && cell23.getCellType() != CellType.BLANK && StrUtil.isNotEmpty(cell23.getStringCellValue())){
                            //删除当前行
                            sheet.removeRow(row1);
                        }else {
                            //更新当前值到第一列中
                            cell1.setCellValue(cell2.getStringCellValue());
                        }
                    }else{
                        //更新当前值到第一列中
                        cell1.setCellValue(cell2.getStringCellValue());
                    }
                }else {
                    cell1.setCellValue(cell3.getStringCellValue());
                }
            }
        }
        //删除空白行
        for (int i = 3; i <= sheet.getLastRowNum(); i++) {
            Boolean  flag = true;
            while(flag) {
                Row row = sheet.getRow(i);
                if (row == null ||  row.getCell(1)== null || StrUtil.isEmpty(row.getCell(1).getStringCellValue())){
                    int shiftRowNum =  i+ 1;
                    if(shiftRowNum <=sheet.getLastRowNum() ){
                        sheet.shiftRows(shiftRowNum, sheet.getLastRowNum(), -1);
                    }else {
                        flag = false;
                    }
                }else {
                    flag = false;
                }
            }
        }
        //删除第三列与第四列
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                //获取第三列
                Cell cell2 = row.getCell(2);
                //获取第四列
                Cell cell3 = row.getCell(3);
                row.removeCell(cell2);
                row.removeCell(cell3);
                row.shiftCellsLeft(4,  row.getLastCellNum(), 2);
            }
        }
        //重新生成序号
        for (int i = 3; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                row.getCell(0).setCellValue(i-2);
            }
        }
        GridRecordDataModel modal =  XlsUtil.readExcel(workbook);
        if(modal == null){
            throw new Exception("材料清单生成luckySheet出现异常");
        }
        List<JSONObject> list = new ArrayList<>();
        list.add(modal.getJson_data());
        return list;
    }


    /**
     * 转换数据为数值类型
     * @param sheet
     */
    public void transFormulaToVal( Sheet sheet) {
        // 创建一个 FormulaEvaluator
        FormulaEvaluator evaluator = sheet.getWorkbook().getCreationHelper().createFormulaEvaluator();
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            for (int j = 0; j <= row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                if (cell != null && cell.getCellType() == CellType.FORMULA) {
                    CellValue cellValue = evaluator.evaluate(cell);
                    switch (cellValue.getCellType()) {
                        case NUMERIC:
                            cell.setCellValue(cellValue.getNumberValue());
                            cell.setCellType(CellType.NUMERIC);
                            break;
                        case STRING:
                            cell.setCellValue(cellValue.getStringValue());
                            cell.setCellType(CellType.STRING);
                            break;
                        case BOOLEAN:
                            cell.setCellValue(cellValue.getBooleanValue());
                            cell.setCellType(CellType.BOOLEAN);
                            break;
                        case ERROR:
                            // 处理错误情况，例如可以选择记录错误或设置特殊值
                            break;
                        // 其他类型可以根据需要处理
                    }
                }

            }
        }
    }


//    public static void main(String args[])  {
//        try {
//
//            File file = new File("D:/excel/ 测试销售订单_BOM+1719368529857.xlsx");
//            DecomposeUtil util = new DecomposeUtil();
//
//
////            List<JSONObject> list = util.genDeliverData(file);
////
////            //生成本地excel
////            OutputStream out = new FileOutputStream("D:/excel/test1.xlsx");
////            XlsUtil.exportXlsFile(out,true,list);
////            File file = new File("D:/excel/HDWM240004关于巴西196MW跟踪支架下单事宜构件清单.xlsx");
////            Workbook workbook = WorkbookFactory.create(file);
////            GridRecordDataModel modal =  XlsUtil.readExcel(workbook);
////            List<JSONObject> list = new ArrayList<>();
////            list.add(modal.getJson_data());
////
//            //生成本地excel
////            List<JSONObject> list = util.genBomData(file);
////            OutputStream out = new FileOutputStream("D:/excel/test2.xlsx");
////            XlsUtil.exportXlsFile(out,true,list);
//
//            log.info("ssddssd");
//        }catch(Exception e){
//            log.error("出现异常",e);
//
//        }
//
//
////        OutputStream out = new FileOutputStream("D:/excel/test2.xlsx");
////        workbook.write(out);
//////        try (FileOutputStream fos = new FileOutputStream("example_modified.xlsx")) {
//////
//////        }
//
//
//    }
//        public static void main(String args[])  {
//            KeyPair pair = SecureUtil.generateKeyPair("RSA");
//
//            String privateKey =   cn.hutool.core.codec.Base64.encode(pair.getPrivate().getEncoded());
//            String publicKey =   cn.hutool.core.codec.Base64.encode(pair.getPublic().getEncoded());
//            System.out.println(" web客户端privateKey = " + privateKey);
//            System.out.println("  web 客户端publicKey = " + publicKey);
//
//
//            KeyPair serverPair = SecureUtil.generateKeyPair("RSA");
//
//            String serverPrivateKey =   cn.hutool.core.codec.Base64.encode(serverPair.getPrivate().getEncoded());
//            String  serverPublicKey =   cn.hutool.core.codec.Base64.encode(serverPair.getPublic().getEncoded());
//
//            System.out.println(" 服务端privateKey = " + serverPrivateKey);
//            System.out.println("  服务端publicKey = " + serverPublicKey);
//
//
//        }

    public static void main(String[] args) {
        String column = "A"; // 例子: "A", "B", "AA", "AB", ...
        int columnNumber = CellReference.convertColStringToIndex(column);
        System.out.println("列序号: " + columnNumber);
    }



}
