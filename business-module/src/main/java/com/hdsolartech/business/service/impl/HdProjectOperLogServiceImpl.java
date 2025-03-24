package com.hdsolartech.business.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.hdsolartech.business.domain.bo.HdProjectOperLogBo;
import com.hdsolartech.business.domain.vo.HdProjectOperLogVo;
import com.hdsolartech.business.domain.HdProjectOperLog;
import com.hdsolartech.business.mapper.HdProjectOperLogMapper;
import com.hdsolartech.business.service.IHdProjectOperLogService;

import java.util.List;
import java.util.Map;
import java.util.Collection;

/**
 * 项目操作信息Service业务层处理
 *
 * @author Lion Li
 * @date 2024-06-04
 */
@RequiredArgsConstructor
@Service
public class HdProjectOperLogServiceImpl implements IHdProjectOperLogService {

    private final HdProjectOperLogMapper baseMapper;




    /**
     * 查询项目操作信息
     */
    @Override
    public HdProjectOperLogVo queryById(Long id){
        return baseMapper.selectVoById(id);
    }

    /**
     * 查询项目操作信息列表
     */
    @Override
    public TableDataInfo<HdProjectOperLogVo> queryPageList(HdProjectOperLogBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HdProjectOperLog> lqw = buildQueryWrapper(bo);
        Page<HdProjectOperLogVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询项目操作信息列表
     */
    @Override
    public List<HdProjectOperLogVo> queryList(HdProjectOperLogBo bo) {
        LambdaQueryWrapper<HdProjectOperLog> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HdProjectOperLog> buildQueryWrapper(HdProjectOperLogBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdProjectOperLog> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getCategory() != null, HdProjectOperLog::getCategory, bo.getCategory());
        lqw.eq(bo.getProjectId() != null, HdProjectOperLog::getProjectId, bo.getProjectId());
        lqw.eq(bo.getOrderId() != null, HdProjectOperLog::getOrderId, bo.getOrderId());
        lqw.eq(StringUtils.isNotBlank(bo.getOperatorType()), HdProjectOperLog::getOperatorType, bo.getOperatorType());
        lqw.eq(bo.getResult() != null, HdProjectOperLog::getResult, bo.getResult());
        lqw.eq(StringUtils.isNotBlank(bo.getDescription()), HdProjectOperLog::getDescription, bo.getDescription());
        lqw.orderByDesc(HdProjectOperLog::getId);
        return lqw;
    }

    /**
     * 新增项目操作信息
     */
    @Override
    public Boolean insertByBo(HdProjectOperLogBo bo) {
        HdProjectOperLog add = MapstructUtils.convert(bo, HdProjectOperLog.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setId(add.getId());
        }
        return flag;
    }

    /**
     * 修改项目操作信息
     */
    @Override
    public Boolean updateByBo(HdProjectOperLogBo bo) {
        HdProjectOperLog update = MapstructUtils.convert(bo, HdProjectOperLog.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HdProjectOperLog entity){
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除项目操作信息
     */
    @Override
    public Boolean deleteWithValidByIds(Collection<Long> ids, Boolean isValid) {
        if(isValid){
            //TODO 做一些业务上的校验,判断是否需要校验
        }
        return baseMapper.deleteBatchIds(ids) > 0;
    }


    /**
     * 记录操作日志
     * @param category 类别
     * @param ProjectId 项目id
     * @param orderId 订单id
     * @param oper  操作类型
     * @param desc  操作明细
     */
    @Override
    public void recordLog(Long category, Long ProjectId, Long orderId, String oper,Long result,String desc){
        //项目操作日志
        HdProjectOperLog log = new HdProjectOperLog();
        log.setProjectId(ProjectId);
        log.setCategory(category);
        log.setOrderId(orderId);
        log.setResult(result);
        log.setOperatorType(oper);
        log.setDescription(desc);
        baseMapper.insert(log);
    }

}
