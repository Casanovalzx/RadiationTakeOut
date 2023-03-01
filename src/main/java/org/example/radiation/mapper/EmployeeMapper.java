package org.example.radiation.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.radiation.entity.Employee;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}
