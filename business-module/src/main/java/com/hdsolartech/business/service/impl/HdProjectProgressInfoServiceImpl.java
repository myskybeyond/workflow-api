package com.hdsolartech.business.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hdsolartech.business.common.util.DecomposeUtil;
import com.hdsolartech.business.common.util.luckysheet.XlsUtil;
import com.hdsolartech.business.domain.HdProjectProgressInfo;
import com.hdsolartech.business.domain.bo.HdProjectProgressExtendInfoBo;
import com.hdsolartech.business.domain.bo.HdProjectProgressInfoBo;
import com.hdsolartech.business.domain.vo.HdOrderInfoVo;
import com.hdsolartech.business.domain.vo.HdProjectFileInfoVo;
import com.hdsolartech.business.domain.vo.HdProjectProgressExtendInfoVo;
import com.hdsolartech.business.domain.vo.HdProjectProgressInfoVo;
import com.hdsolartech.business.mapper.HdOrderInfoMapper;
import com.hdsolartech.business.mapper.HdProjectFileInfoMapper;
import com.hdsolartech.business.mapper.HdProjectFileVersionInfoMapper;
import com.hdsolartech.business.mapper.HdProjectProgressInfoMapper;
import com.hdsolartech.business.service.IHdProjectProgressExtendInfoService;
import com.hdsolartech.business.service.IHdProjectProgressInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellReference;
import org.dromara.common.core.constant.UserConstants;
import org.dromara.common.core.service.ConfigService;
import org.dromara.common.core.service.LuckySheetService;
import org.dromara.common.core.service.ProcessService;
import org.dromara.common.core.service.ProgressService;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.SpringUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 项目进度信息Service业务层处理
 *
 * @author Lion Li
 * @date 2024-06-19
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class HdProjectProgressInfoServiceImpl implements IHdProjectProgressInfoService, ProgressService {

    private final HdProjectProgressInfoMapper baseMapper;

    /**
     * 订单mapper
     */
    private final HdOrderInfoMapper orderInfoMapper;


    /**
     * 项目文件mapper
     */
    private final HdProjectFileInfoMapper projectFileInfoMapper;


    /**
     * 项目mapper
     */
    private final HdProjectFileVersionInfoMapper projectFileVersionInfoMapper;


    /**
     * 进度扩展信息
     */
    private final IHdProjectProgressExtendInfoService extendInfoService;

    /**
     * 缓存路径
     */
    @Value("${business.tmpPath}")
    private String  tmpPath;
    /**
     * 查询项目进度信息
     */
    @Override
    public HdProjectProgressInfoVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询项目进度信息列表
     */
    @Override
    public TableDataInfo<HdProjectProgressInfoVo> queryPageList(HdProjectProgressInfoBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HdProjectProgressInfo> lqw = buildQueryWrapper(bo);
        Page<HdProjectProgressInfoVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询项目进度信息列表
     */
    @Override
    public List<HdProjectProgressInfoVo> queryList(HdProjectProgressInfoBo bo) {
        LambdaQueryWrapper<HdProjectProgressInfo> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HdProjectProgressInfo> buildQueryWrapper(HdProjectProgressInfoBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdProjectProgressInfo> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getProjectId() != null, HdProjectProgressInfo::getProjectId, bo.getProjectId());
        lqw.eq(bo.getOrderId() != null, HdProjectProgressInfo::getOrderId, bo.getOrderId());
        lqw.like(StringUtils.isNotBlank(bo.getName()), HdProjectProgressInfo::getName, bo.getName());
        lqw.eq(bo.getIsSync() != null, HdProjectProgressInfo::getIsSync, bo.getIsSync());
        lqw.eq(bo.getProjectFileId() != null, HdProjectProgressInfo::getProjectFileId, bo.getProjectFileId());
        lqw.eq(bo.getCreateBy() != null, HdProjectProgressInfo::getCreateBy, bo.getCreateBy());
        lqw.eq(bo.getCreateTime() != null, HdProjectProgressInfo::getCreateTime, bo.getCreateTime());
        lqw.eq(bo.getUpdateBy() != null, HdProjectProgressInfo::getUpdateBy, bo.getUpdateBy());
        lqw.eq(bo.getUpdateTime() != null, HdProjectProgressInfo::getUpdateTime, bo.getUpdateTime());

        lqw.between(params.get("beginUpdateTime") != null && params.get("endUpdateTime") != null,
            HdProjectProgressInfo::getUpdateTime ,params.get("beginUpdateTime"), params.get("endUpdateTime"));
        return lqw;
    }

    /**
     * 新增项目进度信息
     */
    @Override
    public Boolean insertByBo(HdProjectProgressInfoBo bo) {
        HdProjectProgressInfo add = MapstructUtils.convert(bo, HdProjectProgressInfo.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改项目进度信息
     */
    @Override
    public Boolean updateByBo(HdProjectProgressInfoBo bo) {
        HdProjectProgressInfo update = MapstructUtils.convert(bo, HdProjectProgressInfo.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HdProjectProgressInfo entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除项目进度信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }

    /**
     * 同步发送流程
     */
    @Override
    public Boolean sync(HdProjectProgressInfoBo bo) {
        //查询流程key
        String modelKey = SpringUtils.getBean(ConfigService.class).getConfigValue(UserConstants.CONFIG_FLOWABLE_PROJECT_PROGRESS);
        //组织同步流程参数
        Map<String, Object> variables = new HashMap<>();
        HdProjectProgressInfoVo progressInfoVo = baseMapper.selectVoById(bo.getId());
        if(progressInfoVo == null){
            return Boolean.FALSE;
        }
        //订单信息
        HdOrderInfoVo orderInfoVo = orderInfoMapper.selectVoById(progressInfoVo.getOrderId());
        if(orderInfoVo == null){
            return Boolean.FALSE;
        }
        variables.put(UserConstants.ORDER_ID_KEY,orderInfoVo.getId()+StringUtils.SEPARATOR+orderInfoVo.getName());
        variables.put(UserConstants.REMARK_KEY,progressInfoVo.getRemark());
        HdProjectFileInfoVo projectFileInfoVo =  projectFileInfoMapper.selectVoById(progressInfoVo.getProjectFileId());
        if(projectFileInfoVo == null){
            return Boolean.FALSE;
        }
        //查询
        //组织发货进度文件信息
        variables.put(UserConstants.ONLINE_COMMON_FILE_ID_KEY,projectFileInfoVo.getOnlineFileId()+StringUtils.SEPARATOR+progressInfoVo.getName()+StringUtils.SEPARATOR+progressInfoVo.getVersion()+StringUtils.SEPARATOR+progressInfoVo.getRemark());
        Boolean flag = SpringUtils.getBean(ProcessService.class).startProcessByModelKey(modelKey,variables);
        if( !flag ){
            return Boolean.FALSE;
        }
        //修改文件同步状态
        projectFileVersionInfoMapper.updateSyncStatus(progressInfoVo.getProjectFileId(),progressInfoVo.getVersion(),UserConstants.SYNC_STATUS_1);
        //修改发货同步状态
        bo.setIsSync(UserConstants.SYNC_STATUS_1);
        HdProjectProgressInfo update = MapstructUtils.convert(bo, HdProjectProgressInfo.class);
        baseMapper.updateById(update);
        //更新进度扩展信息
        updateExtendInfo(progressInfoVo,projectFileInfoVo.getOnlineFileId());
        return Boolean.TRUE;
    }

    /**
     * 更新进度扩展信息
     * @param progressInfoVo
     * @param onlineFileId
     */
    public void  updateExtendInfo(HdProjectProgressInfoVo progressInfoVo ,Long onlineFileId){
        try {
            //项目进度扩展信息
            HdProjectProgressExtendInfoBo bo = new HdProjectProgressExtendInfoBo();
            bo.setProjectProgressId(progressInfoVo.getId());
            HdProjectProgressExtendInfoVo extendInfoVo = extendInfoService.queryByBo(bo);
            //获取excel的信息
            List<JSONObject> rawData = SpringUtils.getBean(LuckySheetService.class).getLuckyDataS(onlineFileId, progressInfoVo.getVersion());
            String fileName = "Progress" + System.currentTimeMillis();
            OutputStream out = new FileOutputStream(tmpPath + fileName + ".xlsx");
            XlsUtil.exportXlsFile(out, true, rawData);
            //关闭文件输出流
            out.close();
            File  rawFile =  new File(tmpPath + fileName + ".xlsx");
            ZipSecureFile.setMinInflateRatio(-1.0d);
            Workbook workbook = WorkbookFactory.create(rawFile);
            //取出第一个工作表
            Sheet sheet = workbook.getSheetAt(0);
            //解析sheet
            DecomposeUtil util = new DecomposeUtil();
            //将公示转化为实际值
            util.transFormulaToVal(sheet);
            //获取需要确定的指定列信息
            int colNum = CellReference.convertColStringToIndex(progressInfoVo.getCol());
            String[] proLabel = {"到货率","完成率","发货率","发货率"};
            List<String> proLabelList = Arrays.asList(proLabel);
            //处理阶段 0:初始阶段 1:到货开始 2:到货结束 、生产开始 3:生产结束，镀锌开始 4:镀锌结束,发货开始
            int step = 0;
            //记录结果
            BigDecimal res = new BigDecimal(0);
            //计数
            int num = 0;
            //记录结果
            BigDecimal white = new BigDecimal(0);
            //计数
            int whiteNum = 0;
            for (int i = 3; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Cell cell = row.getCell(colNum);
                    if(step == 0){
                        //初始阶段
                        if(cell!= null && cell.getCellType()== CellType.STRING && cell.getStringCellValue().equals(proLabelList.get(0))){
                            //到货开始
                            step = 1;
                            num = 0;
                            res = new BigDecimal(0);
                        }
                    }
                    if(step == 1) {
                        if (cell != null) {
                            if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().equals(proLabelList.get(1))) {
                                //到货结束 生产开始 记录
                                bo.setArrival( res.multiply(new BigDecimal(100)).divide(new BigDecimal(num),3,RoundingMode.HALF_UP).toPlainString()+"%");
                                //初始化参数 修改阶段
                                num = 0;
                                res = new BigDecimal(0);
                                step = 2;
                            } else {
                               if(cell.getCellType() ==CellType.NUMERIC ){
                                   res = res.add(new BigDecimal(cell.getNumericCellValue()));
                                   num++;
                               }
                            }
                        }

                    }else if(step == 2 ) {
                        if (cell != null) {
                            if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().equals(proLabelList.get(2))) {
                                //到货结束 生产开始 记录
                                bo.setProduction( res.multiply(new BigDecimal(100)).divide(new BigDecimal(num),3,RoundingMode.HALF_UP).toPlainString()+"%");
                                //初始化参数 修改阶段
                                num = 0;
                                res = new BigDecimal(0);
                                step = 3;
                            } else {
                                if(cell.getCellType() ==CellType.NUMERIC ){
                                    res = res.add(new BigDecimal(cell.getNumericCellValue()));
                                    num++;
                                }
                            }
                        }

                    }else if(step == 3 ) {
                        //镀锌进度
                        if (cell != null) {
                            if (cell.getCellType() == CellType.STRING && cell.getStringCellValue().equals(proLabelList.get(3))) {
                                //到货结束 生产开始 记录
                                bo.setBlack( res.multiply(new BigDecimal(100)).divide(new BigDecimal(num),3,RoundingMode.HALF_UP).toPlainString()+"%");
                                bo.setWhite( white.multiply(new BigDecimal(100)).divide(new BigDecimal(whiteNum),3,RoundingMode.HALF_UP).toPlainString()+"%");
                                //初始化参数 修改阶段
                                num = 0;
                                res = new BigDecimal(0);
                                step = 4;
                            } else {
                                if(cell.getCellType() ==CellType.NUMERIC ){
                                    Cell rCell = row.getCell(colNum-3);
                                    if(rCell.getCellType() ==CellType.STRING && rCell.getStringCellValue().equals("白件")){
                                        white = white.add(new BigDecimal(cell.getNumericCellValue()));
                                        whiteNum++;
                                    }else{
                                        res = res.add(new BigDecimal(cell.getNumericCellValue()));
                                        num++;
                                    }
                                }
                            }
                        }
                    }else{
                        if (cell != null) {
                            if (i == sheet.getLastRowNum()) {
                                //到货结束 生产开始 记录
                                bo.setDelivery( res.multiply(new BigDecimal(100)).divide(new BigDecimal(num),3,RoundingMode.HALF_UP).toPlainString()+"%");
                            } else {
                                if(cell.getCellType() ==CellType.NUMERIC ){
                                    res = res.add(new BigDecimal(cell.getNumericCellValue()));
                                    num++;
                                }
                            }
                        }
                    }
                }
            }
            //删除生成的文件信息
            rawFile.delete();
            if (extendInfoVo == null) {
                //执行插入
                extendInfoService.insertByBo(bo);
            } else {
                //执行更新
                extendInfoService.updateByBo(bo);
            }
        }catch(Exception e){
            log.error("更新进度信息出现异常",e);
        }
    }
    /**
     * 查询所有的进度信息列表
     * @param bo
     * @return
     */
    @Override
    public List<HdProjectProgressInfoVo>  getAllList(HdProjectProgressInfoBo bo){
        LambdaQueryWrapper<HdProjectProgressInfo> lqw = buildQueryWrapper(bo);
        List<HdProjectProgressInfoVo> result = baseMapper.selectVoList(lqw);
        return result;
    }


    @Override
    public String selectNameByProgressId(Long progressId) {
        HdProjectProgressInfoVo progressInfoVo = baseMapper.selectVoById(progressId);
        if(progressInfoVo !=null){
            return progressInfoVo.getName();
        }else{
            return StrUtil.EMPTY;
        }
    }
}
