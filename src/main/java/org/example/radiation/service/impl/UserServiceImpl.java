package org.example.radiation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.radiation.entity.User;
import org.example.radiation.mapper.UserMapper;
import org.example.radiation.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
