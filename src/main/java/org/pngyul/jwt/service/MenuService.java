package org.pngyul.jwt.service;

import org.pngyul.jwt.mapper.MenuMapper;
import org.pngyul.jwt.model.Menu;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MenuService {
    @Autowired
    MenuMapper menuMapper;

    public List<Menu> getAllMenusWithRole() {
        return menuMapper.getAllMenusWithRole();
    }


}
