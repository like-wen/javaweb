package org.example.controller;

import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.example.entity.Allergy;
import org.example.entity.User;
import org.example.service.AllergyService;
import org.example.service.UserService;
import org.example.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
@CrossOrigin
@Api(tags = "患者管理")
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private AllergyService allergyService;

    /**
     * 添加患者
     *
     * @param user
     * @return
     */
    @ApiOperation("添加患者")
    @PostMapping("/add")
    public R addUser(@RequestBody User user) {
        boolean save = userService.save(user);
        if (save) {
            return R.ok().msg("添加成功");
        } else {
            return R.error().msg("添加失败");
        }
    }

    /**
     * 患者列表
     */
    @ApiOperation("患者列表")
    @GetMapping("/list")
    public R listUser() {
        List<User> list = userService.list();
        for (User user : list) {
            //使用allergy的id查询allergy的对象
            Allergy allergy = allergyService.getById(user.getAllergyId());
            //将id换成type字段
            user.setAllergyId(allergy.getType());

        }
        return R.ok().data("list", list);
    }

    /**
     * 患者删除
     */
    @ApiOperation("用户删除")
    @DeleteMapping("/{userId}")
    public R deleteUser(@PathVariable String userId) {

        boolean b = userService.removeById(userId);
        if (b)
            return R.ok().msg("删除成功");
        else
            return R.error().msg("删除失败");

    }


    /**
     * 患者分页查询
     */


    @ApiOperation("患者分页")
    @GetMapping("/page/{current}/{limit}/{name}")
    public R pageAllergy(@PathVariable Long current,
                         @PathVariable Long limit,
                         @PathVariable String name) {
        //创建分页条件
        Page<User> userPage = new Page<>(current, limit);
        //构造搜索条件
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        if (StringUtils.isEmpty((name))) {//如果name不为空
            userQueryWrapper.like("name", name);
        }
        userQueryWrapper.orderByAsc("name");
        userService.page(userPage, userQueryWrapper);

        List<User> records = userPage.getRecords();
        for (User user : records) {
            //使用allergy的id查询allergy的对象
            Allergy allergy = allergyService.getById(user.getAllergyId());
            //将id换成type字段
            user.setAllergyId(allergy.getType());

        }
        return R.ok().data("page", records);


    }

}









