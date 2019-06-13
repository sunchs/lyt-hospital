package com.sunchs.lyt.user.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.UserRole;
import com.sunchs.lyt.db.business.service.impl.UserRoleServiceImpl;
import com.sunchs.lyt.framework.util.NumberUtil;
import com.sunchs.lyt.framework.util.StringUtil;
import com.sunchs.lyt.user.bean.NodeActionParam;
import com.sunchs.lyt.user.bean.RoleNodeData;
import com.sunchs.lyt.user.bean.RoleData;
import com.sunchs.lyt.user.bean.RoleParam;
import com.sunchs.lyt.user.dao.NodeDao;
import com.sunchs.lyt.user.dao.RoleDao;
import com.sunchs.lyt.user.exception.UserException;
import com.sunchs.lyt.user.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RoleService implements IRoleService {

    @Autowired
    RoleDao roleDao;

    @Autowired
    NodeDao nodeDao;

    @Autowired
    private UserRoleServiceImpl userRoleService;

    @Override
    public List<RoleData> getRoleList() {
        return roleDao.getRoleList();
    }

    @Override
    public RoleNodeData save(RoleParam param) {
        Integer roleId = 0;
        if (NumberUtil.isZero(param.getRoleId())) {
            roleId = this.insertRoleData(param);
        } else {
            roleId = this.updateRoleData(param);
        }
        if (roleId > 0) {
            RoleNodeData role = roleDao.getRoleById(roleId);
            if (role == null) {
                throw new UserException("角色ID：" + roleId + "，不存在");
            } else {
                role.setNodeList(nodeDao.getNodeByRoleId(roleId));
            }
            return role;
        }
        return null;
    }

    @Override
    public void bindUserRole(int userId, List<Integer> roleList) {
        // 清历史数据
        Wrapper<UserRole> w = new EntityWrapper<>();
        w.eq(UserRole.USER_ID, userId);
        userRoleService.delete(w);
        // 插入新数据
        roleList.forEach(roleId -> {
            UserRole data = new UserRole();
            data.setUserId(userId);
            data.setRoleId(roleId);
            userRoleService.insert(data);
        });
    }

    private Integer insertRoleData(RoleParam param) {
        this.checkTitle(param, true);
        Map<String, Object> opt = new HashMap<>();
        opt.put("title", param.getTitle());
        Integer roleId = roleDao.insertRoleData(opt);
        if (roleId > 0) {
            this.resetNode(roleId, param.getNode());
        }
        return roleId;
    }

    private Integer updateRoleData(RoleParam param) {
        this.checkTitle(param, true);
        Map<String, Object> opt = new HashMap<>();
        opt.put("roleId", param.getRoleId());
        opt.put("title", param.getTitle());
        Integer roleId = roleDao.updateRoleData(opt);
        if (roleId > 0) {
            this.resetNode(roleId, param.getNode());
        }
        return roleId;
    }

    private void resetNode(Integer roleId, List<NodeActionParam> nodeList) {
        roleDao.deleteRoleNode(roleId);
        Set<Integer> ids = new HashSet<>();
        nodeList.forEach(node -> ids.add(node.getNodeId()));
        if (nodeList.size() > ids.size()) {
            throw new UserException("节点不能有重复数据");
        }
        nodeList.forEach(node->{
            Integer nodeId = node.getNodeId();
            Integer action = node.getAction();
            roleDao.addRoleNode(roleId, nodeId, action);
        });
    }

    private void checkTitle(RoleParam param, boolean isExist) {
        if (StringUtil.isEmpty(param.getTitle())) {
            throw new UserException("标题不能为空");
        }
        Integer roleId = param.getRoleId() == null ? 0 : param.getRoleId();
        if (isExist && roleDao.isExistTitle(roleId, param.getTitle())) {
            throw new UserException("角色已存在");
        }
    }
}