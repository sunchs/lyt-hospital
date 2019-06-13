package com.sunchs.lyt.user.service.impl;

import com.sunchs.lyt.user.bean.NodeData;
import com.sunchs.lyt.user.dao.ipml.NodeDaoImpl;
import com.sunchs.lyt.user.service.INodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeService implements INodeService {

    @Autowired
    NodeDaoImpl nodeDao;

    @Override
    public List<NodeData> getList() {
        return nodeDao.getList();
    }
}
