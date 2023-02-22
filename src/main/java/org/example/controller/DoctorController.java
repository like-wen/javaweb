package org.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Dispensing;
import org.example.entity.Doctor;
import org.example.service.DispensingService;
import org.example.service.DoctorService;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/doctor")
@CrossOrigin
@Api(tags = "医生管理")
public class DoctorController {


    @Autowired
    private DoctorService doctorService;

    /**
     * 获取医生列表
     * @return 医生列表
     */
    @ApiOperation("医生列表")
    @GetMapping("/list")
    public R getDoctorList(){
        List<Doctor> doctorList = doctorService.list();
        return R.ok().data(doctorList);
    }

    /**
     * 新增医生记录
     * @param doctor 医生信息
     * @return
     */
    @ApiOperation("添加医生记录")
    @PostMapping("/add")
    public R addDoctorInfo(@RequestBody Doctor doctor){

        boolean isSuccess = doctorService.save(doctor);
        if(isSuccess)
            return R.ok().msg("保存成功");
        else
            return R.error().msg("保存失败");

    }

    /**
     * 删除医生记录
     * @param doctorId
     * @return
     */
    @ApiOperation("删除医生记录")
    @DeleteMapping("/{doctorId}")
    public R deleteDoctorById(@PathVariable String doctorId){
            boolean isSuccess = doctorService.removeById(doctorId);
            if(isSuccess)
                return R.ok().msg("删除成功");
            else
                return R.error().msg("删除失败");


    }


    @ApiOperation("医生信息分页")
    @GetMapping("/page/{current}/{limit}/{name}")
    public R paginationByDiseaseInfo(@PathVariable Long current,
                                     @PathVariable Long limit,
                                     @PathVariable("name") String name
    ){
        Page<Doctor> doctorPage=new Page<>(current,limit);
        QueryWrapper<Doctor> doctorQueryWrapper = new QueryWrapper<>();

        doctorQueryWrapper.like("name", name)
                .orderByAsc("name");

        doctorService.page(doctorPage,doctorQueryWrapper);
        List<Doctor> records = doctorPage.getRecords();
        return R.ok().data(records);
    }
}
