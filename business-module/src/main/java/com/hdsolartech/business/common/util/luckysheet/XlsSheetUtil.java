package com.hdsolartech.business.common.util.luckysheet;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.Units;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;

import java.math.BigDecimal;
import java.util.*;

/**
 * sheet操作
 * @author Administrator
 */
@Slf4j
public class XlsSheetUtil {
    /**
     * 导出sheet
     * @param wb
     * @param sheetNum
     * @param dbObject
     */
    public static void exportSheet(Workbook wb, int sheetNum, JSONObject dbObject){
        Sheet sheet=wb.createSheet();

        //设置sheet位置，名称
        if(dbObject.containsKey("name")&&dbObject.get("name")!=null){
            wb.setSheetName(sheetNum,dbObject.get("name").toString());
        }else{
            wb.setSheetName(sheetNum,"sheet"+sheetNum);
        }
        //是否隐藏
        if(dbObject.containsKey("hide") && dbObject.get("hide").toString().equals("1")){
            wb.setSheetHidden(sheetNum,true);
        }
        //是否当前选中页
        if(dbObject.containsKey("status") && dbObject.get("status").toString().equals("1")){
            sheet.setSelected(true);
        }
        //循环数据
        if(dbObject.containsKey("celldata")&&dbObject.get("celldata")!=null){
            //取到所有单元格集合
            List<JSONObject> cells_json = ( List<JSONObject> )dbObject.get("celldata");
            Map<Integer,List<JSONObject>> cellMap=cellGroup(cells_json);
            //循环每一行
            for(Integer r:cellMap.keySet()){
                Row row=sheet.createRow(r);
                //循环每一列
                for(JSONObject col:cellMap.get(r)){
                    createCell(wb,sheet,row,col);
                }
            }
        }
        //默认高
        short defaultRowHeight = dbObject.getShort("defaultRowHeight") == null ?20:dbObject.getShort("defaultRowHeight");
        //默认宽
        short defaultColWidth = dbObject.getShort("defaultColWidth") == null ?74:dbObject.getShort("defaultColWidth");
        JSONObject columnlenObject = null;//表格列宽
        JSONObject rowlenObject = null;//表格行高
        JSONObject config = dbObject.getJSONObject("config");
        if (config != null){
            columnlenObject = dbObject.getJSONObject("config").getJSONObject("columnlen");//表格列宽
            rowlenObject = dbObject.getJSONObject("config").getJSONObject("rowlen");//表格行高
        }
        //图片信息
        if(dbObject.containsKey("images") && dbObject.get("images")!=null){
            JSONObject images = dbObject.getJSONObject("images");
            //图片插入
            setImages(wb,sheet,images,columnlenObject,rowlenObject,defaultRowHeight,defaultColWidth);
        }

        setColumAndRow(dbObject,sheet);

    }

    /**
     * 每一个单元格
     * @param row
     * @param dbObject
     */
    private static void createCell(Workbook wb,Sheet sheet,Row row,JSONObject dbObject){
        if(dbObject.containsKey("c")) {
            Integer c = getStrToInt(dbObject.get("c"));
            if (c != null) {
                Cell cell=row.createCell(c);
                //取单元格中的v_json
                if(dbObject.containsKey("v")) {
                    //获取v对象
                    Object obj = dbObject.get("v");
                    if (obj == null) {
                        //没有内容
                        return;
                    }
                    //如果v对象直接是字符串
                    if(obj instanceof String){
                        if(((String) obj).length()>0){
                            cell.setCellValue(obj.toString());
                        }
                        return;
                    }

                    //转换v为对象(v是一个对象)
                    JSONObject v_json = (JSONObject)obj;
                    //样式
                    CellStyle style= wb.createCellStyle();
                    cell.setCellStyle(style);

                    //bs 边框样式 //bc 边框颜色
                    setBorderStyle(style,v_json,"bs","bc");
                    //bs_t 上边框样式   bc_t  上边框颜色
                    setBorderStyle(style,v_json,"bs_t","bc_t");
                    //bs_b 下边框样式   bc_b  下边框颜色
                    setBorderStyle(style,v_json,"bs_b","bc_b");
                    //bs_l 左边框样式   bc_l  左边框颜色
                    setBorderStyle(style,v_json,"bs_l","bc_l");
                    //bs_r 右边框样式   bc_r  右边框颜色
                    setBorderStyle(style,v_json,"bs_r","bc_r");


                    //合并单元格
                    //参数1：起始行 参数2：终止行 参数3：起始列 参数4：终止列
                    //CellRangeAddress region1 = new CellRangeAddress(rowNumber, rowNumber, (short) 0, (short) 11);

                    //mc 合并单元格
                    if(v_json.containsKey("mc")){
                        //是合并的单元格
                        JSONObject mc=v_json.getJSONObject("mc");
                        if(mc.containsKey("rs") && mc.containsKey("cs")){
                            //合并的第一个单元格
                            if(mc.containsKey("r") && mc.containsKey("c")){
                                Integer _rs=getIntByDBObject(mc,"rs")-1;
                                Integer _cs=getIntByDBObject(mc,"cs")-1;
                                Integer _r=getIntByDBObject(mc,"r");
                                Integer _c=getIntByDBObject(mc,"c");

                                CellRangeAddress region = new CellRangeAddress(_r.shortValue(),(_r.shortValue()+_rs.shortValue()), _c.shortValue(),(_c.shortValue()+_cs.shortValue()));
                                sheet.addMergedRegion(region);
                            }
                        }else{
                            //不是合并的第一个单元格
                            return;
                        }
                    }


                    //取v值 (在数据类型中处理)
                    //ct 单元格值格式 (fa,t)
                    setFormatByCt(wb,cell,style,v_json);

                    //font设置
                    setCellStyleFont(wb,style,v_json);

                    //bg 背景颜色
                    if(v_json.containsKey("bg")){
                        String _v=getByDBObject(v_json,"bg");
                        Short _color=ColorUtil.getColorByStr(_v);
                        if(_color!=null) {
                            style.setFillBackgroundColor(_color);
                        }
                    }

                    //vt 垂直对齐    垂直对齐方式（0=居中，1=上，2=下）
                    if(v_json.containsKey("vt")){
                        Integer _v=getIntByDBObject(v_json, "vt");
                        if(_v!=null && _v>=0 && _v<=2){
                            style.setVerticalAlignment(ConstantUtil.getVerticalType(_v));
                        }
                    }

                    //ht 水平对齐   水平对齐方式（0=居中，1=左对齐，2=右对齐）
                    if(v_json.containsKey("ht")){
                        Integer _v=getIntByDBObject(v_json,"ht");
                        if(_v!=null && _v>=0 && _v<=2){
                            style.setAlignment(ConstantUtil.getHorizontaltype(_v));
                        }
                    }

                    //tr 文字旋转 文字旋转角度（0=0,1=45，2=-45，3=竖排文字，4=90，5=-90）
                    if(v_json.containsKey("tr")){
                        Integer _v=getIntByDBObject(v_json, "tr");
                        if(_v!=null){
                            style.setRotation(ConstantUtil.getRotation(_v));
                        }
                    }

                    //tb  文本换行    0 截断、1溢出、2 自动换行
                    //   2：setTextWrapped     0和1：IsTextWrapped = true
                    if(v_json.containsKey("tb")){
                        Integer _v=getIntByDBObject(v_json,"tb");
                        if(_v!=null){
                            if(_v>=0 && _v<=1){
                                style.setWrapText(false);
                            }else{
                                style.setWrapText(true);
                            }
                        }
                    }

                    //f  公式
                    if(v_json.containsKey("f")){
                        String _v=getByDBObject(v_json,"f");
                        if(_v.length()>0){
                            try {
                                if(_v.startsWith("=")){
                                    cell.setCellFormula(_v.substring(1));
                                }else{
                                    cell.setCellFormula(_v);
                                }
                            }catch (Exception ex){
                                log.error("公式 {};Error:{}",_v,ex.toString());
                            }
                        }
                    }


                }

            }
        }
    }

    /**
     * 设置单元格，宽、高
     * @param dbObject
     * @param sheet
     */
    private static void setColumAndRow(JSONObject dbObject,Sheet sheet){
        if(dbObject.containsKey("config")){
            JSONObject config = dbObject.getJSONObject("config");
            BigDecimal excleWid=new BigDecimal(33);
            if(config.containsKey("columnlen")){
                JSONObject columlen = config.getJSONObject("columnlen");
                if(columlen!=null){
                    for(String k:columlen.keySet()){
                        Integer _i=getStrToInt(k);
                        Integer _v=getStrToInt(columlen.get(k).toString());
                        if(_i!=null && _v!=null){
//                            sheet.setColumnWidth(_i,_v.shortValue());
                            sheet.setColumnWidth(_i,new BigDecimal(_v).multiply(excleWid).setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
                        }
                    }
                }
            }
            if(config.containsKey("rowlen")){
                JSONObject rowlen = config.getJSONObject("rowlen");
                BigDecimal excleHei1=new BigDecimal(72);
//                //转化excle行高参数2
                BigDecimal excleHei2=new BigDecimal(96);
                if(rowlen!=null){
                    for(String k:rowlen.keySet()){
                        Integer _i=getStrToInt(k);
                        Integer _v=getStrToInt(rowlen.get(k).toString());
                        if(_i!=null && _v!=null){
                            Row row=sheet.getRow(_i);
                            if(row!=null) {
                              //  row.setHeightInPoints(_v.shortValue());
                                row.setHeightInPoints(new BigDecimal(_v).multiply(excleHei1).divide(excleHei2).floatValue());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 单元格字体相关样式
     * @param wb
     * @param style
     * @param dbObject
     */
    private static void setCellStyleFont(Workbook wb,CellStyle style,JSONObject dbObject){
        Font font = wb.createFont();
        style.setFont(font);

        //ff 字体
        if(dbObject.containsKey("ff")){
            if(dbObject.get("ff") instanceof Integer){
                Integer _v=getIntByDBObject(dbObject,"ff");
                if(_v!=null && ConstantUtil.ff_IntegerToName.containsKey(_v)){
                    font.setFontName(ConstantUtil.ff_IntegerToName.get(_v));
                }
            }else if(dbObject.get("ff") instanceof String){
                font.setFontName(getByDBObject(dbObject,"ff"));
            }
        }
        //fc 字体颜色
        if(dbObject.containsKey("fc")){
            String _v=getByDBObject(dbObject,"fc");
            Short _color=ColorUtil.getColorByStr(_v);
            if(_color!=null) {
                font.setColor(_color);
            }
        }
        //bl 粗体
        if(dbObject.containsKey("bl")){
            Integer _v=getIntByDBObject(dbObject,"bl");
            if(_v!=null){
                if(_v.equals(1)) {
                    //是否粗体显示
                    font.setBold(true);
                }else{
                    font.setBold(false);
                }
            }
        }
        if (dbObject.get("un") != null){
            Integer _v=getIntByDBObject(dbObject,"un");
            if(_v.equals(1)){
                font.setUnderline(Font.U_SINGLE);
            }else {

                font.setUnderline(Font.U_NONE);
            };
        }
        //it 斜体
        if(dbObject.containsKey("it")){
            Integer _v=getIntByDBObject(dbObject,"it");
            if(_v!=null){
                if(_v.equals(1)) {
                    font.setItalic(true);
                }else{
                    font.setItalic(false);
                }
            }
        }
        //fs 字体大小
        if(dbObject.containsKey("fs")){
            Integer _v=getStrToInt(getObjectByDBObject(dbObject,"fs"));
            if(_v!=null){
                font.setFontHeightInPoints(_v.shortValue());
            }
        }
        //cl 删除线 (导入没有)   0 常规 、 1 删除线
        if(dbObject.containsKey("cl")){
            Integer _v=getIntByDBObject(dbObject,"cl");
            if(_v!=null){
                if(_v.equals(1)) {
                    font.setStrikeout(true);
                }
            }
        }
        //ul 下划线
        if(dbObject.containsKey("ul")){
            Integer _v=getIntByDBObject(dbObject,"ul");
            if(_v!=null){
                if(_v.equals(1)) {
                    font.setUnderline(Font.U_SINGLE);
                }else{
                    font.setUnderline(Font.U_NONE);
                }
            }
        }
        //背景颜色
        if (dbObject.get("bg") != null){
            String bg = dbObject.get("bg").toString();
            style.setFillForegroundColor(toColorFromString(bg));
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        }

    }
    /**
     * 字符串转换成Color对象
     * @param colorStr 16进制颜色字符串
     * @return Color对象
     * */
    public static XSSFColor toColorFromString(String colorStr) {
        if (colorStr.contains("#")){
            colorStr = colorStr.substring(1);
            java.awt.Color color = new java.awt.Color(Integer.parseInt(colorStr, 16));
            XSSFColor xssfColor = new XSSFColor(color,null);
            return xssfColor;
        }else {
            int strStartIndex = colorStr.indexOf("(");
            int strEndIndex = colorStr.indexOf(")");
            String[] strings = colorStr.substring(strStartIndex+1,strEndIndex).split(",");
            String R = Integer.toHexString(Integer.parseInt(strings[0].replaceAll(" ","")));
            R = R.length() < 2 ? ('0' + R) : R;
            String B = Integer.toHexString(Integer.parseInt(strings[1].replaceAll(" ","")));
            B = B.length() < 2 ? ('0' + B) : B;
            String G = Integer.toHexString(Integer.parseInt(strings[2].replaceAll(" ","")));
            G = G.length() < 2 ? ('0' + G) : G;
            String  cStr=  R + B + G;
            java.awt.Color color1 = new java.awt.Color(Integer.parseInt(cStr, 16));
            XSSFColor xssfColor = new XSSFColor(color1,null);
            return xssfColor;
        }
    }

    /**
     * 设置cell边框颜色样式
     * @param style 样式
     * @param dbObject json对象
     * @param bs 样式
     * @param bc 样式
     */
    private static void setBorderStyle(CellStyle style,JSONObject dbObject,String bs,String bc ){
        //bs 边框样式
        if(dbObject.containsKey(bs)){
            Integer _v=getStrToInt(getByDBObject(dbObject,bs));
            if(_v!=null){
                //边框没有，不作改变
                if(bs.equals("bs") || bs.equals("bs_t")){
                    style.setBorderTop(BorderStyle.valueOf(_v.shortValue()));
                }
                if(bs.equals("bs") || bs.equals("bs_b")){
                    style.setBorderBottom(BorderStyle.valueOf(_v.shortValue()));
                }
                if(bs.equals("bs") || bs.equals("bs_l")){
                    style.setBorderLeft(BorderStyle.valueOf(_v.shortValue()));
                }
                if(bs.equals("bs") || bs.equals("bs_r")){
                    style.setBorderRight(BorderStyle.valueOf(_v.shortValue()));
                }

                //bc 边框颜色
                String _vcolor=getByDBObject(dbObject,bc);
                if(_vcolor!=null){
                    Short _color=ColorUtil.getColorByStr(_vcolor);
                    if(_color!=null){
                        if(bc.equals("bc") || bc.equals("bc_t")){
                            style.setTopBorderColor(_color);
                        }
                        if(bc.equals("bc") || bc.equals("bc_b")){
                            style.setBottomBorderColor(_color);
                        }
                        if(bc.equals("bc") || bc.equals("bc_l")){
                            style.setLeftBorderColor(_color);
                        }
                        if(bc.equals("bc") || bc.equals("bc_r")){
                            style.setRightBorderColor(_color);
                        }
                    }
                }
            }
        }
    }


    /**
     * 设置单元格格式  ct 单元格值格式 (fa,t)
     * @param cell
     * @param style
     * @param dbObject
     */
    private static void setFormatByCt(Workbook wb,Cell cell,CellStyle style,JSONObject dbObject){

        if(!dbObject.containsKey("v") && dbObject.containsKey("ct")){
            /* 处理以下数据结构
             {
                "celldata": [{
                    "c": 0,
                    "r": 8,
                    "v": {
                        "ct": {
                            "s": [{
                                "v": "sdsdgdf\r\ndfgdfg\r\ndsfgdfgdf\r\ndsfgdfg"
                            }],
                            "t": "inlineStr",
                            "fa": "General"
                        }
                    }
                }]
            }
             */
            JSONObject ct=dbObject.getJSONObject("ct");
            if(ct.containsKey("s")){
                Object s=ct.get("s");
                if(s instanceof List && ((List) s).size()>0){
                    JSONObject _s1=(JSONObject)((List) s).get(0);
                    if(_s1.containsKey("v") && _s1.get("v")instanceof String){
                        dbObject.put("v",_s1.get("v"));
                        style.setWrapText(true);
                    }
                }

            }
        }

        //String v = "";  //初始化
        if(dbObject.containsKey("v")){
            //v = v_json.get("v").toString();
            //取到v后，存到poi单元格对象
            //设置该单元格值
            //cell.setValue(v);

            //String v=getByDBObject(v_json,"v");
            //cell.setValue(v);
            Object obj=getObjectByDBObject(dbObject,"v");
            if(obj instanceof Number){
                cell.setCellValue(Double.valueOf(obj.toString()));
            }else if(obj instanceof Double){
                cell.setCellValue((Double) obj);
            }else if(obj instanceof Date){
                cell.setCellValue((Date)obj);
            }else if(obj instanceof Calendar){
                cell.setCellValue((Calendar) obj);
            }else if(obj instanceof RichTextString){
                cell.setCellValue((RichTextString) obj);
            }else if(obj instanceof String){
                cell.setCellValue((String) obj);
            }else{
                cell.setCellValue(obj.toString());
            }

        }

        if(dbObject.containsKey("ct")){
            JSONObject ct=dbObject.getJSONObject("ct");
            if(ct.containsKey("fa") && ct.containsKey("t")){
                //t 0=bool，1=datetime，2=error，3=null，4=numeric，5=string，6=unknown
                String fa=getByDBObject(ct,"fa"); //单元格格式format定义串
                String t=getByDBObject(ct,"t"); //单元格格式type类型

                Integer _i=ConstantUtil.getNumberFormatMap(fa);
                switch(t){
                    case "s":{
                        //字符串
                        if(_i>=0){
                            style.setDataFormat(_i.shortValue());
                        }else{
                            style.setDataFormat((short)0);
                        }
                        cell.setCellType(CellType.STRING);
                        break;
                    }
                    case "d":{
                        //日期
                        Date _d=null;
                        String v=getByDBObject(dbObject,"m");
                        if(v.length()==0){
                            v=getByDBObject(dbObject,"v");
                        }
                        if(v.length()>0){
                            if(v.indexOf("-")>-1){
                                if(v.indexOf(":")>-1){
                                    _d= ConstantUtil.stringToDateTime(v);
                                }else{
                                    _d= ConstantUtil.stringToDate(v);
                                }
                            }else{
                                _d= ConstantUtil.toDate(v);
                            }
                        }
                        if(_d!=null){
                            //能转换为日期
                            cell.setCellValue(_d);
                            DataFormat format= wb.createDataFormat();
                            style.setDataFormat(format.getFormat(fa));

                        }else{
                            //不能转换为日期
                            if(_i>=0){
                                style.setDataFormat(_i.shortValue());
                            }else{
                                style.setDataFormat((short)0);
                            }
                        }
                        break;
                    }
                    case "b":{
                        //逻辑
                        cell.setCellType(CellType.BOOLEAN);
                        if(_i>=0){
                            style.setDataFormat(_i.shortValue());
                        }else{
                            DataFormat format= wb.createDataFormat();
                            style.setDataFormat(format.getFormat(fa));
                        }
                        break;
                    }
                    case "n":{
                        //数值
                        cell.setCellType(CellType.NUMERIC);
                        if(_i>=0){
                            style.setDataFormat(_i.shortValue());
                        }else{
                            DataFormat format= wb.createDataFormat();
                            style.setDataFormat(format.getFormat(fa));
                        }
                        break;
                    }
                    case "u":
                    case "g":{
                        //general 自动类型
                        //cell.setCellType(CellType._NONE);
                        if(_i>=0){
                            style.setDataFormat(_i.shortValue());
                        }else{
                            DataFormat format= wb.createDataFormat();
                            style.setDataFormat(format.getFormat(fa));
                        }
                        break;
                    }
                    case "e":{
                        //错误
                        cell.setCellType(CellType.ERROR);
                        if(_i>=0){
                            style.setDataFormat(_i.shortValue());
                        }else{
                            DataFormat format= wb.createDataFormat();
                            style.setDataFormat(format.getFormat(fa));
                        }
                        break;
                    }

                }

            }

        }
    }

    /**
     * 内容按行分组
     * @param cells
     * @return
     */
    private static Map<Integer,List<JSONObject>> cellGroup(List<JSONObject> cells){
        Map<Integer,List<JSONObject>> cellMap=new HashMap<>(100);
        for(JSONObject dbObject:cells){
            //行号
            if(dbObject.containsKey("r")){
                Integer r =getStrToInt(dbObject.get("r"));
                if(r!=null){
                    if(cellMap.containsKey(r)){
                        cellMap.get(r).add(dbObject);
                    }else{
                        List<JSONObject> list=new ArrayList<>(10);
                        list.add(dbObject);
                        cellMap.put(r,list);
                    }
                }
            }

        }
        return cellMap;
    }


    /**
     * 获取一个k的值
     * @param b
     * @param k
     * @return
     */
    public static String getByDBObject(JSONObject b,String k){
        if(b.containsKey(k)){
            if(b.get(k)!=null&&b.get(k)instanceof String){
                return b.getString(k);
            }
        }
        return null;
    }

    /**
     * 获取一个k的值
     * @param b
     * @param k
     * @return
     */
    public static Object getObjectByDBObject(JSONObject b,String k){
        if(b.containsKey(k)){
            if(b.get(k)!=null){
                return b.get(k);
            }
        }
        return "";
    }

    /**
     * 没有/无法转换 返回null
     * @param b
     * @param k
     * @return
     */
    public static Integer getIntByDBObject(JSONObject b,String k){
        if(b.containsKey(k)){
            if(b.get(k)!=null){
                try{
                    String _s=b.getString(k).replace("px", "");
                    Double _d=Double.parseDouble(_s);
                    return _d.intValue();
                }catch (Exception ex){
                    log.error(ex.getMessage());
                    return null;
                }
            }
        }
        return null;
    }
    /**
     * 转int
     * @param str
     * @return
     */
    private static Integer getStrToInt(Object str){
        try{
            if(str!=null) {
                return Integer.parseInt(str.toString());
            }
            return null;
        }catch (Exception ex){
            log.error("String:{};Error:{}",str,ex.getMessage());
            return null;
        }
    }
    private static Short getStrToShort(Object str){
        try{
            if(str!=null) {
                return Short.parseShort(str.toString());
            }
            return null;
        }catch (Exception ex){
            log.error("String:{};Error:{}",str,ex.getMessage());
            return null;
        }
    }

    /**
     *
     * @param wb
     * @param sheet
     * @param images 所有图片
     * @param columnlenObject
     * @param rowlenObject
     * @param defaultRowHeight
     * @param defaultColWidth
     */
    private static void setImages(Workbook wb,Sheet sheet, JSONObject images,JSONObject columnlenObject,JSONObject rowlenObject,short defaultRowHeight,short defaultColWidth){
        //图片插入
        if (images != null){
            Map<String, Object> map = images.getInnerMap();
            JSONObject finalColumnlenObject = columnlenObject;
            JSONObject finalRowlenObject = rowlenObject;
            for(Map.Entry<String, Object> entry : map.entrySet()) {
                Drawing patriarch = sheet.createDrawingPatriarch();
                //图片信息
                JSONObject iamgeData = (JSONObject) entry.getValue();
                //图片的位置宽 高 距离左 距离右
                JSONObject imageDefault = ((JSONObject) iamgeData.get("default"));
                //算坐标
                Map<String, Integer> colrowMap = getColRowMap(imageDefault, defaultRowHeight, defaultColWidth, finalColumnlenObject, finalRowlenObject);
                XSSFClientAnchor anchor = new XSSFClientAnchor(colrowMap.get("dx1"), colrowMap.get("dy1"), colrowMap.get("dx2"), colrowMap.get("dy2"), colrowMap.get("col1"), colrowMap.get("row1"), colrowMap.get("col2"), colrowMap.get("row2"));
                anchor.setAnchorType( ClientAnchor.AnchorType.byId(Integer.parseInt(iamgeData.get("type").toString())));
                byte[] decoderBytes = new byte[0];
                boolean flag = true;
                if (iamgeData.get("src") != null) {
                    decoderBytes = Base64.getDecoder().decode(iamgeData.get("src").toString().split(";base64,")[1]);
                    flag = iamgeData.get("src").toString().split(";base64,")[0].contains("png");
                }
                if (flag) {
                    patriarch.createPicture(anchor, wb.addPicture(decoderBytes, HSSFWorkbook.PICTURE_TYPE_PNG));
                } else {
                    patriarch.createPicture(anchor, wb.addPicture(decoderBytes, HSSFWorkbook.PICTURE_TYPE_JPEG));
                }
            }
        }
    }

    /**
     * 获取图片位置
     * dx1：起始单元格的x偏移量，如例子中的255表示直线起始位置距A1单元格左侧的距离；
     * dy1：起始单元格的y偏移量，如例子中的125表示直线起始位置距A1单元格上侧的距离；
     * dx2：终止单元格的x偏移量，如例子中的1023表示直线起始位置距C3单元格左侧的距离；
     * dy2：终止单元格的y偏移量，如例子中的150表示直线起始位置距C3单元格上侧的距离；
     * col1：起始单元格列序号，从0开始计算；竖
     * row1：起始单元格行序号，从0开始计算，如例子中col1=0,row1=0就表示起始单元格为A1；横
     * col2：终止单元格列序号，从0开始计算；
     * row2：终止单元格行序号，从0开始计算，如例子中col2=2,row2=2就表示起始单元格为C3；
     * @param imageDefault
     * @param defaultRowHeight
     * @param defaultColWidth
     * @param columnlenObject
     * @param rowlenObject
     */
    public static Map<String, Integer> getColRowMap(JSONObject imageDefault,short defaultRowHeight , short defaultColWidth ,JSONObject columnlenObject, JSONObject rowlenObject){

        BigDecimal left =   new BigDecimal(imageDefault.get("left")+"");
        BigDecimal top =   new BigDecimal(imageDefault.get("top")+"");
        BigDecimal width =  new BigDecimal( imageDefault.get("width") +"");
        BigDecimal height =   new BigDecimal( imageDefault.get("height")+"");

        BigDecimal dx1 = left;//宽 行
        BigDecimal dy1 = top; //高 列
        BigDecimal dx2 = left.add(width);
        BigDecimal dy2 = top.add(height);
        //算起始最大列
        BigDecimal bigDefaultColWidth = new BigDecimal(defaultColWidth);

        BigDecimal bigDefaultRowHeight = new BigDecimal(defaultRowHeight);

        int colMax1 = left.divide(bigDefaultColWidth,BigDecimal.ROUND_CEILING).intValue();

        //算起始最大行
        int rowMax1 = top.divide(bigDefaultRowHeight,BigDecimal.ROUND_CEILING).intValue();

        //算终止最大列
        int colMax2 =   dx2.divide(bigDefaultColWidth,BigDecimal.ROUND_CEILING).intValue();
        //算终止最大行
        int rowMax2 =  dy2.divide(bigDefaultRowHeight,BigDecimal.ROUND_CEILING).intValue();

        int col1 = 0;
        int row1 = 0;
        int col2 = 0;
        int row2 = 0;
        //算起始列的序号和偏移量
        for (int index = 0;index <= colMax1;index++){
            BigDecimal col = null;
            if (columnlenObject != null && columnlenObject.getString(Integer.toString(index)) != null){
                col = new BigDecimal(columnlenObject.getString(Integer.toString(index)));//看当前列是否重新赋值
            }
            //算起始列
            if (col == null && dx1.compareTo(new BigDecimal(defaultColWidth)) < 0){
                col1 = index;
                break;
            }
            //算起始X偏移
            if (col == null && dx1.compareTo(new BigDecimal(defaultColWidth)) >= 0){
                dx1 =dx1.subtract(new BigDecimal(defaultColWidth)) ;
            }

            //算起始列
            if (col != null && dx1.compareTo(col) < 0){
                col1 = index;
                break;
            }
            //算起始X偏移
            if (col != null ){
                dx1 = dx1.subtract(col) ;
            }

        }

        //算起始行的序号和偏移量
        for (int index = 0;index <= rowMax1;index++){
            BigDecimal row = null;
            if (rowlenObject != null && rowlenObject.getString(Integer.toString(index)) != null){
                row = new BigDecimal(rowlenObject.getString(Integer.toString(index)));//看当前行是否重新赋值
            }
            //算起始行
            if (row == null && dy1.compareTo(new BigDecimal(defaultRowHeight)) < 0){
                row1 = index;
                break;
            }
            //算起始y偏移
            if (row == null && dy1.compareTo(new BigDecimal(defaultRowHeight)) >= 0){
                dy1 =dy1.subtract(new BigDecimal(defaultRowHeight));
            }
            //算起始行
            if (row != null && dy1.compareTo(row) < 0){
                row1 = index;
                break;
            }
            //算起始y偏移
            if (row != null){
                dy1 = dy1.subtract(row) ;
            }
        }

        //算最终列的序号和偏移量
        for (int index = 0;index <= colMax2;index++){
            BigDecimal col = null;
            if (columnlenObject != null && columnlenObject.getString(Integer.toString(index)) != null){
                col = new BigDecimal(columnlenObject.getString(Integer.toString(index)));//看当前列是否重新赋值
            }
            //算最终列
            if (col == null && dx2.compareTo(new BigDecimal(defaultColWidth)) < 0){
                col2 = index;
                break;
            }
            //算最终X偏移
            if (col == null && dx2.compareTo(new BigDecimal(defaultColWidth)) >= 0){
                dx2 =dx2.subtract(new BigDecimal(defaultColWidth)) ;
            }

            //算最终列
            if (col != null && dx2.compareTo(col) < 0){
                col2 = index;
                break;
            }
            //算最终X偏移
            if (col != null ){
                dx2 = dx2.subtract(col) ;
            }

        }

        //算最终行的序号和偏移量
        for (int index = 0;index <= rowMax2;index++){
            //行高
            BigDecimal row = null;
            if (rowlenObject != null && rowlenObject.getString(Integer.toString(index)) != null){
                row = new BigDecimal(rowlenObject.getString(Integer.toString(index)));//看当前行是否重新赋值
            }
            //算最终行
            if (row == null && dy2.compareTo(new BigDecimal(defaultRowHeight)) < 0){
                row2 = index;
                break;
            }
            //算最终y偏移
            if (row == null && dy2.compareTo(new BigDecimal(defaultRowHeight)) >= 0){
                dy2 =dy2.subtract(new BigDecimal(defaultRowHeight));
            }
            //算最终行
            if (row != null && dy2.compareTo(row) < 0){
                row2 = index;
                break;
            }
            //算最终Y偏移
            if (row != null){
                dy2 = dy2.subtract(row) ;
            }
        }
        Map<String, Integer> map =new HashMap<>();
        map.put("dx1",dx1.multiply(new BigDecimal(Units.EMU_PER_PIXEL)).setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
        map.put("dy1",dy1.multiply(new BigDecimal(Units.EMU_PER_PIXEL)).setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
        map.put("dx2",dx2.multiply(new BigDecimal(Units.EMU_PER_PIXEL)).setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
        map.put("dy2",dy2.multiply(new BigDecimal(Units.EMU_PER_PIXEL)).setScale(0,BigDecimal.ROUND_HALF_UP).intValue());
        map.put("col1",col1);
        map.put("row1",row1);
        map.put("col2",col2);
        map.put("row2",row2);
        return map;
    }

}
