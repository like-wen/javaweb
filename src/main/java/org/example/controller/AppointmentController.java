package org.example.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.*;
import org.example.entity.dto.SearchDto;
import org.example.service.*;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Keat-Jie
 * @version 1.0
 * @date 2023/2/20
 */
@Slf4j//日志
@RestController//Controller组件
@RequestMapping("/appointment")//这里记得改
@CrossOrigin//跨域
@Api(tags = "预约管理")//这里也是
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private UserService userService;

    @Autowired
    private DiseaseService diseaseService;





    /**
     * 预约添加
     * @param appointment
     * @return
     */
    @ApiOperation("预约添加")
    @PostMapping("/add")
    public R addAppointment(@RequestBody Appointment appointment) {//请求体到的位置

        boolean save = appointmentService.save(appointment);
        if (save)
            return R.ok().msg("保存成功");
        else
            return R.error().msg("保存失败");

    }


    /**
     * 预约删除
     * @param appointmentId
     * @return
     */
    @ApiOperation("预约删除")
    @GetMapping("/delete")
    public R deleteAppointment(@PathVariable String appointmentId) {
        boolean b = appointmentService.removeById(appointmentId);
        if (b)
            return R.ok().msg("删除成功");
        else
            return R.error().msg("删除失败");


    }


    /**
     * 预约分页查询
     * @param search
     * @return
     */
    @ApiOperation("预约分页")
    @GetMapping("/list")
    @ResponseBody
    public R pageAppointment(SearchDto search) {
        //分页
        Page<Appointment> appointmentPage=new Page<>(search.getPage(),search.getLimit());
        //查询的配置
        QueryWrapper<Appointment>  appointmentQueryWrapper = new QueryWrapper<>();
        if(!StringUtils.isEmpty(search.getSearch())) {//判断这个属性存不存在,不存在就不搜索了
            appointmentQueryWrapper.like("id", search.getSearch());
        }
        appointmentQueryWrapper.orderByAsc("id");//按照name字段排序
        //放入查找
        appointmentService.page(appointmentPage,appointmentQueryWrapper);
        List<Appointment> records = appointmentPage.getRecords();

        //记得替换掉有id的东西
        for (Appointment record : records) {
            //没注入的记得在上面@Autowired 注入
            String departmentId = record.getDepartmentId();
            if(!StringUtils.isEmpty(departmentId)) {
                record.setDepartmentId(departmentService.getById(departmentId).getName());
            }
            //没注入的记得在上面@Autowired 注入
            String doctorId = record.getDoctorId();
            if(!StringUtils.isEmpty(doctorId)) {
                record.setDoctorId(doctorService.getById(doctorId).getName());
            }

            String userId = record.getUserId();
            if(!StringUtils.isEmpty(userId)) {
                record.setDepartmentId(userService.getById(userId).getName());
            }

            String diseaseId = record.getDiseaseId();
            if(!StringUtils.isEmpty(diseaseId)) {
                record.setDepartmentId(diseaseService.getById(userId).getUserDisease());
            }
        }


        records.forEach(System.out::println);
        return R.ok().data(records);
    }

}
