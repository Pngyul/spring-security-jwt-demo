package org.pngyul.jwt.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.pngyul.jwt.model.Menu;

import java.util.List;

@Mapper
public interface MenuMapper {

    List<Menu> getAllMenusWithRole();
}