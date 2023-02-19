package org.example.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Allergy;
import org.example.service.AllergyService;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j//日志
@RestController//Controller组件
@RequestMapping("/allergy")//这里记得改
@CrossOrigin//跨域
@Api(tags = "过敏管理")//这里也是
public class AllergyController {
    //自动注入
    @Autowired
    AllergyService allergyService;


    //数据列表
    @ApiOperation("过敏列表")
    @GetMapping("/list")
    public R listAllergy(){
        List<Allergy> list = allergyService.list();
        return R.ok().data("list",list);//ok是指成功,data里放返回的数据,数据类型是键值对,键是String类型,值是对象
    }

    //数据添加
    @ApiOperation("过敏添加")
    @PostMapping("/add")
    public R addAllergy(@RequestBody Allergy allergy){//请求体到的位置

        boolean save = allergyService.save(allergy);
        if(save)
            return R.ok().msg("保存成功");
        else
            return R.error().msg("保存失败");

    }


    //删除
    @ApiOperation("过敏删除")
    @DeleteMapping("/{allergyId}")
    public R deleteAllergy(@PathVariable String allergyId){
        boolean b = allergyService.removeById(allergyId);
        if(b)
            return R.ok().msg("删除成功");
        else
            return R.error().msg("删除失败");


    }
    //分页查询列表

    @ApiOperation("过敏分页")
    @GetMapping("/page/{current}/{limit}/{type}")
    public R pageAllergy(@PathVariable Long current,
                         @PathVariable Long limit,
                         @PathVariable String type){
        //创建分页条件
        Page<Allergy> allergyPage=new Page<>(current,limit);
        //构造搜索条件
        QueryWrapper<Allergy> allergyQueryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty((type))) {//如果type不为空
            allergyQueryWrapper.like("type", type);//相似字段查询,,"type"是sql里的过敏类型对应的字段
        }
        allergyQueryWrapper.orderByAsc("type");//按照type字段对结果排序

        allergyService.page(allergyPage,allergyQueryWrapper);//开始加入搜索
        //获取list集合
        List<Allergy> records = allergyPage.getRecords();

        return R.ok().data("page",records);
    }




}
