package org.example.radiation.dto;

import org.example.radiation.entity.Setmeal;
import org.example.radiation.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
