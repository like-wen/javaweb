package org.example.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.entity.Column;
import org.example.service.ColumnService;
import org.example.mapper.ColumnMapper;
import org.springframework.stereotype.Service;

/**
* @author 李可文
* @description 针对表【column】的数据库操作Service实现
* @createDate 2023-02-19 11:35:33
*/
@Service
public class ColumnServiceImpl extends ServiceImpl<ColumnMapper, Column>
    implements ColumnService{

}




