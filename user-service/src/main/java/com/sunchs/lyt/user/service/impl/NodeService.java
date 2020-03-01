package com.sunchs.lyt.user.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.Node;
import com.sunchs.lyt.db.business.entity.Role;
import com.sunchs.lyt.db.business.entity.RoleNode;
import com.sunchs.lyt.db.business.entity.UserRole;
import com.sunchs.lyt.db.business.service.impl.NodeServiceImpl;
import com.sunchs.lyt.db.business.service.impl.RoleNodeServiceImpl;
import com.sunchs.lyt.db.business.service.impl.RoleServiceImpl;
import com.sunchs.lyt.db.business.service.impl.UserRoleServiceImpl;
import com.sunchs.lyt.framework.util.ObjectUtil;
import com.sunchs.lyt.framework.util.UserThreadUtil;
import com.sunchs.lyt.user.bean.MenuData;
import com.sunchs.lyt.user.bean.NodeData;
import com.sunchs.lyt.user.dao.ipml.NodeDaoImpl;
import com.sunchs.lyt.user.service.INodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NodeService implements INodeService {

    @Autowired
    NodeDaoImpl nodeDao;

    @Autowired
    private NodeServiceImpl nodeService;

    @Autowired
    private UserRoleServiceImpl userRoleService;

    @Autowired
    private RoleServiceImpl roleService;

    @Autowired
    private RoleNodeServiceImpl roleNodeService;


    @Override
    public List<NodeData> getList() {
        return nodeDao.getList();
    }

    @Override
    public List<MenuData> getMenuList() {
        List<Integer> userNodeIds = getUserNodeIds();
        if (userNodeIds.size() == 0) {
            return new ArrayList<>();
        }

        List<MenuData> list = new ArrayList<>();
        Wrapper<Node> wrapper = new EntityWrapper<Node>()
                .eq(Node.STATUS, 1)
                .in(Node.ID, userNodeIds)
                .orderBy(Node.SORT, true);
        List<Node> nodeList = nodeService.selectList(wrapper);
        nodeList.forEach(n -> {
            if (n.getPid().equals(0)) {
                MenuData data = ObjectUtil.copy(n, MenuData.class);
                List<MenuData> childList = new ArrayList<>();
                nodeList.forEach(son -> {
                    if (son.getPid().equals(n.getId())) {
                        MenuData sonData = ObjectUtil.copy(son, MenuData.class);
                        childList.add(sonData);
                    }
                });
                data.setChildren(childList);
                list.add(data);
            }
        });
        return list;
    }

    @Override
    public List<Integer> getRoleNodeIds(int releId) {
        List<Integer> rnIds = new ArrayList<>();
        Wrapper<RoleNode> roleNodeWrapper = new EntityWrapper<RoleNode>()
                .eq(RoleNode.ROLE_ID, releId);
        roleNodeService.selectList(roleNodeWrapper).forEach(roleNode -> rnIds.add(roleNode.getNodeId()));
        if (rnIds.size() == 0) {
            return new ArrayList<>();
        }
        return rnIds;
    }

    private List<Integer> getUserNodeIds() {
        Integer userId = UserThreadUtil.getUserId();

        List<Integer> ruIds = new ArrayList<>();
        Wrapper<UserRole> userRoleWrapper = new EntityWrapper<UserRole>()
                .eq(UserRole.USER_ID, userId);
        userRoleService.selectList(userRoleWrapper).forEach(userRole -> ruIds.add(userRole.getRoleId()));
        if (ruIds.size() == 0) {
            return new ArrayList<>();
        }

        List<Integer> rIds = new ArrayList<>();
        Wrapper<Role> roleWrapper = new EntityWrapper<Role>()
                .in(Role.ID, ruIds)
                .eq(Role.STATUS, 1);
        roleService.selectList(roleWrapper).forEach(role -> rIds.add(role.getId()));
        if (rIds.size() == 0) {
            return new ArrayList<>();
        }

        List<Integer> rnIds = new ArrayList<>();
        Wrapper<RoleNode> roleNodeWrapper = new EntityWrapper<RoleNode>()
                .in(RoleNode.ROLE_ID, rIds);
        roleNodeService.selectList(roleNodeWrapper).forEach(roleNode -> rnIds.add(roleNode.getNodeId()));
        if (rnIds.size() == 0) {
            return new ArrayList<>();
        }

        return rnIds;
    }
}
