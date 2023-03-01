package org.example.radiation.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.radiation.entity.AddressBook;
import org.example.radiation.mapper.AddressBookMapper;
import org.example.radiation.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
