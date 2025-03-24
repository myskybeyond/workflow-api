package org.dromara.system.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Param;
import org.dromara.common.mybatis.core.mapper.BaseMapperPlus;
import org.dromara.system.domain.SysSocial;
import org.dromara.system.domain.bo.SysSocialBo;
import org.dromara.system.domain.vo.SysSocialVo;

import java.util.List;

/**
 * 社会化关系Mapper接口
 *
 * @author thiszhc
 */
public interface SysSocialMapper extends BaseMapperPlus<SysSocial, SysSocialVo> {

    /**
     * 按参数分页查询
     * @param page 分页对象
     * @param bo 参数对象
     * @return
     */
    List<SysSocialVo> queryByParams(IPage<SysSocialVo> page, @Param("bo") SysSocialBo bo);

    List<SysSocialVo> queryByParams( @Param("bo") SysSocialBo bo);

}
