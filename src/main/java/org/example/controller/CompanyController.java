package org.example.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Allergy;
import org.example.entity.Company;
import org.example.entity.User;
import org.example.entity.dto.SearchDto;
import org.example.service.CompanyService;
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
@RequestMapping("/company")//这里记得改
@CrossOrigin//跨域
@Api(tags = "厂商管理")//这里也是
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @ApiOperation("添加厂商")
    @PostMapping("/add")
    public R addCompany(@RequestBody Company company) {
        boolean save = companyService.save(company);
        if (save) {
            return R.ok().msg("添加成功");
        } else {
            return R.error().msg("添加失败");
        }
    }



    @ApiOperation("厂商删除")
    @GetMapping("/delete")
    public R deleteCompany(@PathVariable String companyId) {

        boolean b = companyService.removeById(companyId);
        if (b)
            return R.ok().msg("删除成功");
        else
            return R.error().msg("删除失败");

    }

    @ApiOperation("厂商分页")
    @GetMapping("/list")
    @ResponseBody
    public R pageCompany(SearchDto search) {
        //分页
        Page<Company> companyPage = new Page<>(search.getPage(),search.getLimit());
        //查询的配置
        QueryWrapper<Company> companyQueryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(search.getSearch())) {//判断这个属性存不存在,不存在就不搜索了
            companyQueryWrapper.like("name", search.getSearch());
        }
        companyQueryWrapper.orderByAsc("name");//按照name字段排序
        //放入查找
        companyService.page(companyPage, companyQueryWrapper);
        List<Company> records = companyPage.getRecords();


        records.forEach(System.out::println);
        return R.ok().data(records);
    }
}
