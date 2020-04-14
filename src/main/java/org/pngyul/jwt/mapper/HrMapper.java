package org.pngyul.jwt.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.pngyul.jwt.model.Hr;
import org.pngyul.jwt.model.Role;

import java.util.List;

@Mapper
public interface HrMapper {

    Hr loadUserByUsername(String username);

    List<Role> getHrRolesById(Integer id);


}