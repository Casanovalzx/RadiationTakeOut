package org.example.radiation.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.radiation.entity.Category;

public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
