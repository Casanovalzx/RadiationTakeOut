package org.example.radiation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.radiation.entity.AddressBook;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
