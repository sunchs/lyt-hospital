package com.sunchs.lyt.user.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.Node;
import com.sunchs.lyt.db.business.service.impl.NodeServiceImpl;
import com.sunchs.lyt.framework.util.ObjectUtil;
import com.sunchs.lyt.user.bean.MenuData;
import com.sunchs.lyt.user.bean.NodeData;
import com.sunchs.lyt.user.dao.ipml.NodeDaoImpl;
import com.sunchs.lyt.user.service.INodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NodeService implements INodeService {

    @Autowired
    NodeDaoImpl nodeDao;

    @Autowired
    private NodeServiceImpl nodeService;


    @Override
    public List<NodeData> getList() {
        return nodeDao.getList();
    }

    @Override
    public List<MenuData> getMenuList() {
        List<MenuData> list = new ArrayList<>();
        Wrapper<Node> wrapper = new EntityWrapper<Node>()
                .eq(Node.STATUS, 1);
        List<Node> nodeList = nodeService.selectList(wrapper);
        nodeList.forEach(n -> {
            if (n.getPid().equals(0)) {
                MenuData data = ObjectUtil.copy(n, MenuData.class);
                List<Node> nodes = nodeList.stream().filter(node -> node.getPid().equals(n.getId())).collect(Collectors.toList());
                nodes.forEach(son -> {
                    data.getChildren().add(ObjectUtil.copy(son, MenuData.class));
                });
                list.add(data);
            }
        });
        return list;
    }
}
