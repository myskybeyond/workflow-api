package org.dromara.common.excel.convert;

import cn.hutool.core.convert.Convert;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.ReadCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 公式数据转换处理
 *
 * @author Lion Li
 */
@Slf4j
public class ExcelFormulaConvert implements Converter<Object> {

    @Override
    public Class<Object> supportJavaTypeKey() {
        return Object.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return null;
    }

    @Override
    public Object convertToJavaData(ReadCellData<?> cellData, ExcelContentProperty contentProperty, GlobalConfiguration globalConfiguration) {
        if(cellData.getType() == CellDataTypeEnum.NUMBER){

            return Convert.convert(contentProperty.getField().getType(),  cellData.getNumberValue().setScale(3, RoundingMode.HALF_UP));
        }else if(cellData.getType() == CellDataTypeEnum.STRING){
            return Convert.convert(contentProperty.getField().getType(), cellData.getStringValue());
        }
        return Convert.convert(contentProperty.getField().getType(), cellData.getStringValue());
    }

}
