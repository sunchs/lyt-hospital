package com.sunchs.lyt.db.business.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author king
 * @since 2019-06-11
 */
@TableName("role_node")
public class RoleNode extends Model<RoleNode> {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableId("role_id")
    private Integer roleId;

    /**
     * 节点ID
     */
    @TableField("node_id")
    private Integer nodeId;

    /**
     * 权限
     */
    private Integer action;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }
    public Integer getNodeId() {
        return nodeId;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }
    public Integer getAction() {
        return action;
    }

    public void setAction(Integer action) {
        this.action = action;
    }

    public static final String ROLE_ID = "role_id";

    public static final String NODE_ID = "node_id";

    public static final String ACTION = "action";

    @Override
    protected Serializable pkVal() {
        return this.roleId;
    }

    @Override
    public String toString() {
        return "RoleNode{" +
        "roleId=" + roleId +
        ", nodeId=" + nodeId +
        ", action=" + action +
        "}";
    }
}
