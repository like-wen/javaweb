package org.example.controller;


import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Results;
import org.example.entity.Allergy;
import org.example.entity.Disease;
import org.example.service.DiseaseService;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/disease")
@CrossOrigin
@Api(tags = "疾病管理")
public class DiseaseController {

    @Autowired
    private DiseaseService diseaseService;

    /**
     * 获取疾病列表
     * @return 疾病列表
     */
    @ApiOperation("疾病列表")
    @GetMapping("/list")
    public R getDiseaseList(){
        List<Disease> diseaseList = diseaseService.list();
        return R.ok().data(diseaseList);
    }

    /**
     * 添加疾病
     * @param disease 疾病
     * @return
     */
    @ApiOperation("疾病添加")
    @PostMapping("/add")
    public R addAllergy(@RequestBody Disease disease){

        boolean isSuccess = diseaseService.save(disease);
        if(isSuccess)
            return R.ok().msg("保存成功");
        else
            return R.error().msg("保存失败");

    }

    /**
     * 删除疾病记录
     * @param diseaseId 疾病id
     * @return
     */
    @ApiOperation("根据Id删除疾病记录")
    @DeleteMapping("/{diseaseId}")
    public R deleteDiseaseById(@PathVariable String diseaseId){
        boolean isSuccess = diseaseService.removeById(diseaseId);
        if(isSuccess)
            return R.ok().msg("删除成功");
        else
            return R.error().msg("删除失败");


    }

    /**
     *  疾病信息分页
     * @param current 当前页
     * @param limit 步长
     * @param userDisease 疾病信息
     * @return
     */
    @ApiOperation("疾病信息分页")
    @GetMapping("/page/{current}/{limit}/{user_disease}")
    public R paginationByDiseaseInfo(@PathVariable Long current,
                         @PathVariable Long limit,
                         @PathVariable("user_disease") String userDisease
    ){
        Page<Disease> diseasePage=new Page<>(current,limit);
        QueryWrapper<Disease> diseaseQueryWrapper = new QueryWrapper<>();

        diseaseQueryWrapper.like("user_disease", userDisease)
                .orderByAsc("user_disease");

        diseaseService.page(diseasePage,diseaseQueryWrapper);
        List<Disease> records = diseasePage.getRecords();
        return R.ok().data(records);
    }


}
