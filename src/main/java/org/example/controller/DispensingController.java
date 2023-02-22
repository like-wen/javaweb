package org.example.controller;


import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Disease;
import org.example.entity.Dispensing;
import org.example.service.DispensingService;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/dispensing")
@CrossOrigin
@Api(tags = "配药管理")
public class DispensingController {

    @Autowired
    private DispensingService dispensingService;

    /**
     * 获取配药列表
     * @return 配药列表
     */
    @ApiOperation("获取配药列表")
    @GetMapping("/list")
    public R getDiseaseList(){
        List<Dispensing>  dispensingList = dispensingService.list();
        return R.ok().data(dispensingList);
    }

    /**
     * 添加配药记录
     * @param Dispensing 配药记录
     * @return
     */
    @ApiOperation("添加配药记录")
    @PostMapping("/add")
    public R addDispensing(@RequestBody Dispensing Dispensing){

        boolean isSuccess = dispensingService.save(Dispensing);
        if(isSuccess)
            return R.ok().msg("保存成功");
        else
            return R.error().msg("保存失败");

    }

    /**
     * 根据id删除配药记录
     * @param dispensingId
     * @return
     */
    @ApiOperation("根据id删除配药记录")
    @DeleteMapping("/{dispensingId}")
    public R deleteDispensingRecordById(@PathVariable String dispensingId){
        boolean isSuccess = dispensingService.removeById(dispensingId);
        if(isSuccess)
            return R.ok().msg("删除成功");
        else
            return R.error().msg("删除失败");


    }

    /**
     *
     * @param current
     * @param limit
     * @param medicineId
     * @return
     */
    @ApiOperation("配药信息分页")
    @GetMapping("/page/{current}/{limit}/{medicine_id}")
    public R paginationByDiseaseInfo(@PathVariable Long current,
                                     @PathVariable Long limit,
                                     @PathVariable("medicine_id") String medicineId
    ){
        Page<Dispensing> dispensingPage=new Page<>(current,limit);
        QueryWrapper<Dispensing> dispensingQueryWrapper = new QueryWrapper<>();

        dispensingQueryWrapper.like("medicine_id", medicineId)
                .orderByAsc("medicine_id");

        dispensingService.page(dispensingPage,dispensingQueryWrapper);
        List<Dispensing> records = dispensingPage.getRecords();
        return R.ok().data(records);
    }
}
