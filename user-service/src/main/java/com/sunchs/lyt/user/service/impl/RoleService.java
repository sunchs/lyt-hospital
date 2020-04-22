package com.sunchs.lyt.user.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.Role;
import com.sunchs.lyt.db.business.entity.RoleNode;
import com.sunchs.lyt.db.business.entity.UserRole;
import com.sunchs.lyt.db.business.service.impl.RoleNodeServiceImpl;
import com.sunchs.lyt.db.business.service.impl.RoleServiceImpl;
import com.sunchs.lyt.db.business.service.impl.UserRoleServiceImpl;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.util.*;
import com.sunchs.lyt.user.bean.*;
import com.sunchs.lyt.user.dao.NodeDao;
import com.sunchs.lyt.user.dao.RoleDao;
import com.sunchs.lyt.user.enums.StatusEnum;
import com.sunchs.lyt.user.exception.UserException;
import com.sunchs.lyt.user.service.IRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class RoleService implements IRoleService {

    @Autowired
    RoleDao roleDao;

    @Autowired
    NodeDao nodeDao;

    @Autowired
    private UserRoleServiceImpl userRoleService;

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private RoleNodeServiceImpl roleNodeService;

    @Override
    public PagingList<RoleData> getRoleList(RoleParam param) {
        List<RoleData> list = new ArrayList<>();
        Wrapper<Role> w = new EntityWrapper<>();
        w.and("id not IN(select role_id from user_role WHERE user_id="+UserThreadUtil.getUserId()+")");
        w.orderBy(Role.ID, false);
        Page<Role> page = roleService.selectPage(new Page<>(param.getPageNow(), param.getPageSize()), w);
        page.getRecords().forEach(row -> {
            RoleData data = ObjectUtil.copy(row, RoleData.class);
            data.setStatusName(StatusEnum.getName(row.getStatus()));
            list.add(data);
        });
        return PagingUtil.getData(list, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public int save(RoleParam param) {
        this.checkTitle(param, true);
        Role role = new Role();
        role.setId(param.getId());
        role.setPid(0);
        role.setTitle(param.getTitle());
        role.setStatus(param.getStatus());
        role.setSort(100);
        role.setUpdateId(UserThreadUtil.getUserId());
        role.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        role.setCreateId(UserThreadUtil.getUserId());
        role.setCreateTime(new Timestamp(System.currentTimeMillis()));
        if (roleService.insertOrUpdate(role)) {
            Wrapper<RoleNode> wrapper = new EntityWrapper<RoleNode>()
                    .eq(RoleNode.ROLE_ID, role.getId());
            roleNodeService.delete(wrapper);
            param.getNode().forEach(n -> {
                RoleNode roleNode = new RoleNode();
                roleNode.setRoleId(role.getId());
                roleNode.setNodeId(n.getNodeId());
                roleNode.setAction(n.getAction());
                roleNodeService.insert(roleNode);
            });
        }
        return role.getId();

//        Integer roleId = 0;
//        if (NumberUtil.isZero(param.getRoleId())) {
//            roleId = this.insertRoleData(param);
//        } else {
//            roleId = this.updateRoleData(param);
//        }
//        if (roleId > 0) {
//            RoleNodeData role = roleDao.getRoleById(roleId);
//            if (role == null) {
//                throw new UserException("角色ID：" + roleId + "，不存在");
//            } else {
//                role.setNodeList(nodeDao.getNodeByRoleId(roleId));
//            }
//            return role;
//        }
//        return null;
    }

    @Override
    public void bindUserRole(int userId, UserParam param) {
        // 清历史数据
        Wrapper<UserRole> w = new EntityWrapper<>();
        w.eq(UserRole.USER_ID, userId);
        userRoleService.delete(w);
        // 插入新数据
        param.getRoleList().forEach(roleId -> {
            UserRole data = new UserRole();
            data.setUserId(userId);
            data.setRoleId(roleId);
            userRoleService.insert(data);
        });
    }

    @Override
    public List<Map<String, Object>> getSelectData() {
        List<Map<String, Object>> list = new ArrayList<>();
        Wrapper<Role> w = new EntityWrapper<>();
        w.eq(Role.STATUS, 1);
        w.orderBy(Role.SORT, true);
        List<Role> res = roleService.selectList(w);
        res.forEach(row -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", row.getId());
            m.put("title", row.getTitle());
            list.add(m);
        });
        return list;
    }

//    private Integer insertRoleData(RoleParam param) {
//        this.checkTitle(param, true);
//        Map<String, Object> opt = new HashMap<>();
//        opt.put("title", param.getTitle());
//        Integer roleId = roleDao.insertRoleData(opt);
//        if (roleId > 0) {
//            this.resetNode(roleId, param.getNode());
//        }
//        return roleId;
//    }
//
//    private Integer updateRoleData(RoleParam param) {
//        this.checkTitle(param, true);
//        Map<String, Object> opt = new HashMap<>();
//        opt.put("roleId", param.getRoleId());
//        opt.put("title", param.getTitle());
//        Integer roleId = roleDao.updateRoleData(opt);
//        if (roleId > 0) {
//            this.resetNode(roleId, param.getNode());
//        }
//        return roleId;
//    }

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
//        Integer roleId = param.getId() == null ? 0 : param.getId();
        if (isExist && roleDao.isExistTitle(param.getId(), param.getTitle())) {
            throw new UserException("角色已存在");
        }
    }
}