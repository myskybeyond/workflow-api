package org.dromara.system.controller.system;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.common.satoken.utils.LoginHelper;
import org.dromara.common.web.core.BaseController;
import org.dromara.system.domain.bo.SysSocialBo;
import org.dromara.system.domain.vo.SysSocialVo;
import org.dromara.system.service.ISysSocialService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 社会化关系
 *
 * @author thiszhc
 * @date 2023-06-16
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/social")
public class SysSocialController extends BaseController {

    private final ISysSocialService socialUserService;

    /**
     * 查询社会化关系列表
     */
    @GetMapping("/list")
    @SaCheckPermission("app:social:list:my")
    public R<List<SysSocialVo>> list() {
        return R.ok(socialUserService.queryListByUserId(LoginHelper.getUserId()));
    }

    /**
     * 分页查询社会化关系列表
     */
    @SaCheckPermission("app:social:page")
    @GetMapping("/page")
    public TableDataInfo<SysSocialVo> list(SysSocialBo bo, PageQuery pageQuery) {
        return socialUserService.selectPageSocialList(bo, pageQuery);
    }

    /**
     * 查询社会化关系列表
     */
    @SaCheckPermission("app:social:list")
    @GetMapping("/list/{appType}/{appCode}")
    public R<List<SysSocialVo>> listByAppTypeAndAppCode(@PathVariable String appType, @PathVariable String appCode) {
        SysSocialBo bo = new SysSocialBo();
        bo.setAppType(appType);
        bo.setAppCode(appCode);
        return R.ok(socialUserService.selectSocialList(bo));
    }

}
