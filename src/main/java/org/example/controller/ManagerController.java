package org.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Manager;
import org.example.entity.Medicine;
import org.example.service.ManagerService;
import org.example.utils.Md5Utils;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author Keat-Jie
 * @version 1.0
 * @date 2023/2/21
 */
@Slf4j
@RestController
@RequestMapping("/manager")
@CrossOrigin
@Api(tags = "用户管理")
public class ManagerController {

    @Autowired
    private ManagerService managerService;

    @ApiOperation("添加用户信息")
    @PostMapping("/add")
    @ResponseBody
    public R addMedicineInfo(@RequestBody Manager manager) {
        String salt = Md5Utils.createSalt();
        manager.setSalt(salt);
        String md5Password = Md5Utils.md5Password(manager.getPwd(), salt);
        manager.setPwd(md5Password);
        boolean isSuccess = managerService.save(manager);
        if (isSuccess)
            return R.ok().msg("保存成功");
        else
            return R.error().msg("保存失败");

    }

    @ApiOperation("检查用户信息")
    @PostMapping("/query")
    @ResponseBody
    public R check(Manager manager){
        String id = manager.getId();
        Manager managerById = managerService.getById(id);
        if (managerById == null){
            return R.error().msg("该用户不存在");
        }
        String salt = managerById.getSalt();
        manager.setSalt(salt);
        manager.setPwd(Md5Utils.md5Password(manager.getPwd(), salt));
        boolean isSuccess = manager.getPwd().equals(managerById.getPwd());
        if (isSuccess)
            return R.ok().msg("密码正确");
        else
            return R.error().msg("密码错误");

    }


}
