package com.myskybeyond.flowable.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.myskybeyond.flowable.domain.WfForm;
import com.myskybeyond.flowable.domain.vo.WfFormVo;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 流程表单Mapper接口
 *
 * @author KonBAI
 * @createTime 2022/3/7 22:07
 */
public interface WfFormMapper extends BaseMapperPlus<WfForm, WfFormVo> {

    List<WfFormVo> selectFormVoList(@Param(Constants.WRAPPER) Wrapper<WfForm> queryWrapper);
}
