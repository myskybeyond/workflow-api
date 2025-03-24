package com.hdsolartech.business.listener;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.hdsolartech.business.domain.bo.HdComponentInfoBo;
import com.hdsolartech.business.domain.vo.HdCategoryInfoVo;
import com.hdsolartech.business.domain.vo.HdComponentInfoVo;
import com.hdsolartech.business.service.IHdCategoryInfoService;
import com.hdsolartech.business.service.IHdComponentInfoService;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.core.utils.SpringUtils;
import org.dromara.common.excel.core.ExcelListener;
import org.dromara.common.excel.core.ExcelResult;

import java.util.List;

/**
 * @ClassName ComponentImportListener
 * @Description 组件导入listener
 * @Author zyf
 * @create 2024/5/30 16:42
 * @Version 1.0
 */
@Slf4j
public class ComponentImportListener extends AnalysisEventListener<HdComponentInfoVo> implements ExcelListener<HdComponentInfoVo> {

    private final IHdComponentInfoService hdComponentInfoService;

    private final IHdCategoryInfoService hdCategoryInfoService;

    private final String category;


    private int successNum = 0;
    private int failureNum = 0;
    private final StringBuilder successMsg = new StringBuilder();
    private final StringBuilder failureMsg = new StringBuilder();

    public ComponentImportListener(String category) {
        //设置组件service
        this.hdComponentInfoService = SpringUtils.getBean(IHdComponentInfoService.class);
        //设置分类service
        this.hdCategoryInfoService = SpringUtils.getBean(IHdCategoryInfoService.class);

        this.category = category;

    }

    @Override
    public void invoke(HdComponentInfoVo componentInfoVo, AnalysisContext context) {

        try {
            if(!componentInfoVo.checkParams() || StrUtil.isEmpty(this.category)){
                log.error("参数验证失败");
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、料品 ").append(componentInfoVo.getName()).append(" 参数验证失败");
                return;
            }
            componentInfoVo.setCategory(this.category);
            // 查询所属分类是否存在
            HdCategoryInfoVo categoryInfoVo = hdCategoryInfoService.queryByCode(componentInfoVo.getCategoryCode());
            if(categoryInfoVo == null){
                log.error("所属分类不存在");
                failureNum++;
                failureMsg.append("<br/>").append(failureNum).append("、料品 ").append(componentInfoVo.getName()).append(" 所属分类不存在");
                return;
            }
            //设置分类id
            componentInfoVo.setCategoryId(categoryInfoVo.getId());
            if(StrUtil.isEmpty(componentInfoVo.getParentName())){
                //默认为顶级节点
                componentInfoVo.setParentId(0L);
            }else{
                //查询上级构件信息
                HdComponentInfoVo parent =  hdComponentInfoService.queryByName(componentInfoVo.getParentName());
                if(parent == null){
                    log.error("上级构件不存在");
                    failureNum++;
                    failureMsg.append("<br/>").append(failureNum).append("、料品 ").append(componentInfoVo.getName()).append(" 上级构件不存在");
                    return;
                }
                //设置上级构件id
                componentInfoVo.setParentId(parent.getId());
            }
            //保存
            HdComponentInfoVo current =  hdComponentInfoService.queryByName(componentInfoVo.getName());
//            if( current == null ){
                HdComponentInfoBo bo = BeanUtil.toBean(componentInfoVo, HdComponentInfoBo.class);
                //直接插入数据
                hdComponentInfoService.insertByBo(bo);
                successNum++;
                successMsg.append("<br/>").append(successNum).append("、料品 ").append(componentInfoVo.getName()).append(" 导入成功");
//            }else {
//                HdComponentInfoBo bo = BeanUtil.toBean(componentInfoVo, HdComponentInfoBo.class);
//                //直接插入数据
//                hdComponentInfoService.updateByBo(bo);
//                successNum++;
//                successMsg.append("<br/>").append(successNum).append("、料品 ").append(componentInfoVo.getName()).append(" 导入成功");
//            }
        } catch (Exception e) {
            failureNum++;
            String msg = "<br/>" + failureNum + "、料品 " +componentInfoVo.getName() + " 导入失败：";
            failureMsg.append(msg).append(e.getMessage());
            log.error(msg, e);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    @Override
    public ExcelResult<HdComponentInfoVo> getExcelResult() {
        return new ExcelResult<>() {

            @Override
            public String getAnalysis() {
                if (failureNum > 0) {
                    failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据格式不正确，错误如下：");
                    throw new ServiceException(failureMsg.toString());
                } else {
                    successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条，数据如下：");
                }
                return successMsg.toString();
            }

            @Override
            public List<HdComponentInfoVo> getList() {
                return null;
            }

            @Override
            public List<String> getErrorList() {
                return null;
            }
        };
    }


}
