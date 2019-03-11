package com.sunchs.lyt.user.service.impl;

import com.sunchs.lyt.user.bean.RoleData;
import com.sunchs.lyt.user.dao.RoleDao;
import com.sunchs.lyt.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleDao roleDao;

    @Override
    public List<RoleData> getRoleList() {
        return roleDao.getRoleList();
    }
}
