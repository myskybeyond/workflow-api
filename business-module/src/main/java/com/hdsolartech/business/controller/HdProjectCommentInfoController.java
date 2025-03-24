package com.hdsolartech.business.controller;

import java.util.List;

import cn.dev33.satoken.annotation.SaMode;
import com.hdsolartech.business.domain.vo.CommentInfoVo;
import lombok.RequiredArgsConstructor;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.*;
import cn.dev33.satoken.annotation.SaCheckPermission;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.web.core.BaseController;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.core.domain.R;
import org.dromara.common.core.validate.AddGroup;
import org.dromara.common.core.validate.EditGroup;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.excel.utils.ExcelUtil;
import com.hdsolartech.business.domain.vo.HdProjectCommentInfoVo;
import com.hdsolartech.business.domain.bo.HdProjectCommentInfoBo;
import com.hdsolartech.business.service.IHdProjectCommentInfoService;
import org.dromara.common.mybatis.core.page.TableDataInfo;

/**
 * 项目评论信息
 *
 * @author Lion Li
 * @date 2024-06-03
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/business/projectCommentInfo")
public class HdProjectCommentInfoController extends BaseController {

    private final IHdProjectCommentInfoService hdProjectCommentInfoService;

    /**
     * 查询项目评论信息列表
     */
    @SaCheckPermission(value = {"business:projectCommentInfo:list", "business:projectInfo:detail","business:orderInfo:detail"}, mode = SaMode.OR)
    @GetMapping("/list")
    public TableDataInfo<HdProjectCommentInfoVo> list(HdProjectCommentInfoBo bo, PageQuery pageQuery) {
        return hdProjectCommentInfoService.queryPageList(bo, pageQuery);
    }

    @GetMapping("/page")
    public TableDataInfo<CommentInfoVo> page(HdProjectCommentInfoBo bo, PageQuery pageQuery){
        return hdProjectCommentInfoService.queryCommentPageList(bo, pageQuery);
    }


    /**
     * 导出项目评论信息列表
     */
    @SaCheckPermission("business:projectCommentInfo:export")
    @Log(title = "项目评论信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HdProjectCommentInfoBo bo, HttpServletResponse response) {
        List<HdProjectCommentInfoVo> list = hdProjectCommentInfoService.queryList(bo);
        ExcelUtil.exportExcel(list, "项目评论信息", HdProjectCommentInfoVo.class, response);
    }

    /**
     * 获取项目评论信息详细信息
     *
     * @param id 主键
     */
    @SaCheckPermission(value = {"business:projectCommentInfo:query", "business:projectInfo:detail","business:orderInfo:detail"}, mode = SaMode.OR)
    @GetMapping("/{id}")
    public R<HdProjectCommentInfoVo> getInfo(@NotNull(message = "主键不能为空")
                                     @PathVariable Long id) {
        return R.ok(hdProjectCommentInfoService.queryById(id));
    }

    /**
     * 新增项目评论信息
     */
    @SaCheckPermission(value = {"business:projectCommentInfo:add", "business:projectInfo:detail","business:orderInfo:detail"}, mode = SaMode.OR)
    @Log(title = "项目评论信息", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping()
    public R<Void> add(@Validated(AddGroup.class) @RequestBody HdProjectCommentInfoBo bo) {
        return toAjax(hdProjectCommentInfoService.insertByBo(bo));
    }

    /**
     * 修改项目评论信息
     */
    @SaCheckPermission("business:projectCommentInfo:edit")
    @Log(title = "项目评论信息", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping()
    public R<Void> edit(@Validated(EditGroup.class) @RequestBody HdProjectCommentInfoBo bo) {
        return toAjax(hdProjectCommentInfoService.updateByBo(bo));
    }

    /**
     * 删除项目评论信息
     *
     * @param ids 主键串
     */
    @SaCheckPermission("business:projectCommentInfo:remove")
    @Log(title = "项目评论信息", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public R<Void> remove(@NotEmpty(message = "主键不能为空")
                          @PathVariable Long[] ids) {
        return toAjax(hdProjectCommentInfoService.deleteWithValidByIds(List.of(ids), true));
    }


    @SaCheckPermission(value = {"business:projectInfo:detail","business:orderInfo:detail"}, mode = SaMode.OR)
    @GetMapping("/reply")
    public TableDataInfo<CommentInfoVo> reply(HdProjectCommentInfoBo bo, PageQuery pageQuery){
        return TableDataInfo.build(hdProjectCommentInfoService.replyPage(bo.getParentId(),pageQuery));
    }

}
