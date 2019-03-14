package com.sunchs.lyt.user.service.impl;

import com.sunchs.lyt.framework.util.StringUtil;
import com.sunchs.lyt.user.bean.RoleData;
import com.sunchs.lyt.user.bean.RoleParam;
import com.sunchs.lyt.user.dao.RoleDao;
import com.sunchs.lyt.user.exception.UserException;
import com.sunchs.lyt.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleDao roleDao;

    @Override
    public List<RoleData> getRoleList() {
        return roleDao.getRoleList();
    }

    @Override
    public Integer addRoleData(RoleParam param) {
        if (StringUtil.isEmpty(param.getTitle())) {
            throw new UserException("标题不能为空");
        }
        Map<String, Object> opt = new HashMap<>();
        opt.put("title", param.getTitle());
        Integer roleId = roleDao.insertRoleData(opt);
        if (roleId > 0) {
            this.resetAccess(roleId, param.getAccess());
        }
        return roleId;
    }

    @Override
    public Integer updateRoleData(RoleParam param) {
        if (StringUtil.isEmpty(param.getTitle())) {
            throw new UserException("标题不能为空");
        }
        Map<String, Object> opt = new HashMap<>();
        opt.put("nodeId", param.getRoleId());
        opt.put("title", param.getTitle());
        roleDao.updateRoleData(opt);
        this.resetAccess(param.getRoleId(), param.getAccess());
        return param.getRoleId();
    }

    private void resetAccess(Integer roleId, List<Map<String, Integer>> access) {
        roleDao.deleteRoleNode(roleId);
        // TODO: access需去重
        access.forEach(node->{
            Integer nodeId = node.get("nodeId");
            Integer action = node.get("action");
            roleDao.addRoleNode(roleId, nodeId, action);
        });
    }
}