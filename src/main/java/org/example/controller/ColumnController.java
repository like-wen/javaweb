package org.example.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Columntable;
import org.example.service.ColumntableService;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Keat-Jie
 * @version 1.0
 * @date 2023/2/20
 */
@Slf4j//日志
@RestController//Controller组件
@RequestMapping("/column")//这里记得改
@CrossOrigin//跨域
@Api(tags = "取药位管理")//这里也是
public class ColumnController {

    @Autowired
    private ColumntableService columnService;

    @ApiOperation("添加取药位")
    @PostMapping("/add")
    public R addColumn(@RequestBody Columntable column) {
        boolean save = columnService.save(column);
        if (save) {
            return R.ok().msg("添加成功");
        } else {
            return R.error().msg("添加失败");
        }
    }

    @ApiOperation("取药位列表")
    @GetMapping("/list")
    public R listUser() {
        List<Columntable> list = columnService.list();
        return R.ok().data("list", list);
    }

    @ApiOperation("取药位删除")
    @DeleteMapping("/{columnId}")
    public R deleteUser(@PathVariable String columnId) {

        boolean b = columnService.removeById(columnId);
        if (b)
            return R.ok().msg("删除成功");
        else
            return R.error().msg("删除失败");

    }

    @ApiOperation("取药位分页")
    @GetMapping("/page/{current}/{limit}/{name}")
    public R pageAllergy(@PathVariable Long current,
                         @PathVariable Long limit,
                         @PathVariable String type) {
        //创建分页条件
        Page<Columntable> columnPage=new Page<>(current,limit);
        //构造搜索条件
        QueryWrapper<Columntable> columnQueryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty((type))) {//如果type不为空
            columnQueryWrapper.like("type", type);//相似字段查询,,"type"是sql里的过敏类型对应的字段
        }
        columnQueryWrapper.orderByAsc("type");//按照type字段对结果排序

        columnService.page(columnPage,columnQueryWrapper);//开始加入搜索
        //获取list集合
        List<Columntable> records = columnPage.getRecords();

        return R.ok().data("page",records);


    }
}
