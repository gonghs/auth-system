package com.maple.server.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.maple.server.common.anno.JsonArg;
import com.maple.server.common.builder.Results;
import com.maple.server.common.entity.ReqPage;
import com.maple.server.common.entity.Result;
import com.maple.server.dto.admin.DataTablesDTO;
import com.maple.server.tool.DataTableTool;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 基础控制器
 * TODO 定制controller生成模板
 *
 * @author maple
 * @version 1.0
 * @since 2019-09-22 19:36
 */
public class BaseController<Service extends IService<T>, T> {
    @Autowired(required = false)
    protected Service service;
    @Autowired
    protected HttpServletRequest request;

    /**
     * 通用的分页查询
     *
     * @param page 查询一般传入参数为current和size, 例如/listPage?current=1&size=5,
     * @return 返回分页数据
     */
    @PostMapping(value = "/getPage")
    @ResponseBody
    @ApiOperation(value = "分页查询", notes = "分页查询")
    public Result<IPage<T>> getPage(@JsonArg ReqPage<T> page,
                                    @JsonArg T model) {
        IPage<T> pageList = service.page(page, Wrappers.lambdaQuery(model));
        return Results.success(pageList);
    }

    /**
     * dataTables 分页查询
     *
     * @param model 实体查询条件
     * @return 返回分页数据
     */
    @PostMapping(value = "/dataTablePage")
    @ResponseBody
    @ApiOperation(value = "dataTable分页查询", notes = "分页查询")
    public DataTablesDTO<T> getPage(DataTablesPage dataTablesPage, @ApiParam(name = "model") T model) {
        IPage<T> pageList = service.page(new Page<>(dataTablesPage.getStart() / dataTablesPage.getLength() + 1,
                        dataTablesPage.getLength()),
                Wrappers.lambdaQuery(model));
        return DataTableTool.convert(pageList).setDraw(dataTablesPage.getDraw());
    }

    @Data
    private static class DataTablesPage {
        @ApiModelProperty(value = "绘画次数",example = "1")
        private Long draw;

        @ApiModelProperty(value = "开始下标",example = "0")
        private Long start;

        @ApiModelProperty(value = "每页长度",example = "10")
        private Long length;

        public Long getDraw() {
            return Optional.ofNullable(draw).orElse(1L);
        }

        public Long getStart() {
            return Optional.ofNullable(start).orElse(0L);
        }

        public Long getLength() {
            return Optional.ofNullable(length).orElse(10L);
        }
    }

}
