package org.dromara.system.service.impl;

import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.common.core.utils.StringUtils;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.mybatis.core.page.PageQuery;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.satoken.utils.LoginHelper;
import org.springframework.stereotype.Service;
import org.dromara.system.domain.bo.HdUserWfBo;
import org.dromara.system.domain.vo.HdUserWfVo;
import org.dromara.system.domain.HdUserWf;
import org.dromara.system.mapper.HdUserWfMapper;
import org.dromara.system.service.IHdUserWfService;

import java.util.List;
import java.util.Map;

/**
 * 用户于流程关联
 * Service业务层处理
 *
 * @author Lion Li
 * @date 2024-06-14
 */
@RequiredArgsConstructor
@Service
public class HdUserWfServiceImpl implements IHdUserWfService {

    private final HdUserWfMapper baseMapper;

    /**
     * 查询用户于流程关联
     */
    @Override
    public HdUserWfVo queryById(Long userId) {
        return baseMapper.selectVoById(userId);
    }

    /**
     * 查询用户于流程关联
     * 列表
     */
    @Override
    public TableDataInfo<HdUserWfVo> queryPageList(HdUserWfBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<HdUserWf> lqw = buildQueryWrapper(bo);
        Page<HdUserWfVo> result = baseMapper.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    /**
     * 查询用户于流程关联
     * 列表
     */
    @Override
    public List<HdUserWfVo> queryList(HdUserWfBo bo) {
        LambdaQueryWrapper<HdUserWf> lqw = buildQueryWrapper(bo);
        return baseMapper.selectVoList(lqw);
    }

    private LambdaQueryWrapper<HdUserWf> buildQueryWrapper(HdUserWfBo bo) {
        Map<String, Object> params = bo.getParams();
        LambdaQueryWrapper<HdUserWf> lqw = Wrappers.lambdaQuery();
        lqw.eq(bo.getWfId() != null, HdUserWf::getWfId, bo.getWfId());
        lqw.like(StringUtils.isNotBlank(bo.getWfName()), HdUserWf::getWfName, bo.getWfName());
        lqw.eq(StringUtils.isNotBlank(bo.getProcessKey()), HdUserWf::getProcessKey, bo.getProcessKey());
        lqw.eq(bo.getUserId()!=null, HdUserWf::getUserId, bo.getUserId());
        lqw.orderByAsc(HdUserWf::getUpdateTime);
        return lqw;
    }

    /**
     * 新增用户于流程关联
     */
    @Override
    public Boolean insertByBo(HdUserWfBo bo) {
        HdUserWf add = MapstructUtils.convert(bo, HdUserWf.class);
        validEntityBeforeSave(add);
        boolean flag = baseMapper.insert(add) > 0;
        if (flag) {
            bo.setUserId(add.getUserId());
        }
        return flag;
    }

    /**
     * 修改用户于流程关联
     */
    @Override
    public Boolean updateByBo(HdUserWfBo bo) {
        HdUserWf update = MapstructUtils.convert(bo, HdUserWf.class);
        validEntityBeforeSave(update);
        return baseMapper.updateById(update) > 0;
    }

    /**
     * 保存前的数据校验
     */
    private void validEntityBeforeSave(HdUserWf entity) {
        //TODO 做一些数据校验,如唯一约束
    }

    /**
     * 批量删除用户于流程关联
     */
    @Override
    public Boolean deleteWithValidByIds(String ids, Boolean isValid) {
        return baseMapper.delete(new LambdaQueryWrapper<HdUserWf>().eq(HdUserWf::getWfId, ids).eq(HdUserWf::getUserId, LoginHelper.getUserId())) > 0;
    }
}
