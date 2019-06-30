package com.sunchs.lyt.user.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.User;
import com.sunchs.lyt.db.business.entity.UserHospital;
import com.sunchs.lyt.db.business.entity.UserRole;
import com.sunchs.lyt.db.business.service.impl.UserHospitalServiceImpl;
import com.sunchs.lyt.db.business.service.impl.UserRoleServiceImpl;
import com.sunchs.lyt.db.business.service.impl.UserServiceImpl;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.constants.CacheKeys;
import com.sunchs.lyt.framework.constants.DateTimes;
import com.sunchs.lyt.framework.util.*;
import com.sunchs.lyt.user.bean.UserData;
import com.sunchs.lyt.user.bean.UserParam;
import com.sunchs.lyt.user.bean.UserRoleData;
import com.sunchs.lyt.user.enums.StatusEnum;
import com.sunchs.lyt.user.exception.UserException;
import com.sunchs.lyt.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Service
public class UserService implements IUserService {

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserRoleServiceImpl userRoleService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserHospitalServiceImpl userHospitalService;

    @Override
    public int save(UserParam param) {
        if (NumberUtil.isZero(param.getId())) {
            return insert(param);
        } else {
            return update(param);
        }
    }

    @Override
    public PagingList<UserData> getPagingList(UserParam param) {
        Wrapper<User> w = new EntityWrapper<>();
        Page<User> page = userService.selectPage(new Page<>(param.getPageNow(), param.getPageSize()), w);
        List<UserData> list = new ArrayList<>();
        page.getRecords().forEach(row->{
            UserData data = ObjectUtil.copy(row, UserData.class);
            data.setStatusName(StatusEnum.getName(data.getStatus()));
            list.add(data);
        });
        return PagingUtil.getData(list, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public UserRoleData login(UserParam param) {
        param.checkUserName();
        param.checkPassWord();
        User user = getUserByAccount(param.getUserName(), MD5Util.encode(param.getPassWord()));
        if (Objects.isNull(user)) {
            throw new UserException("账号或者密码错误！");
        }

        // 更新数据库中的token
        String token = MD5Util.encode(user.getUsername() + System.currentTimeMillis()) + "id" + user.getId();
        User data = new User();
        data.setId(user.getId());
        data.setToken(token);
        if ( ! userService.updateById(data)) {
            throw new UserException("登录失败！");
        }

        // 结果集
        UserRoleData res = new UserRoleData();
        res.setId(user.getId());
        res.setUserName(user.getUsername());
        res.setName(user.getName());
        res.setToken(token);
        res.setRoleList(getUserRoleIds(user.getId()));

        // 更新缓存
        RedisUtil.setValue(CacheKeys.USER_LOGIN + token, JsonUtil.toJson(res), DateTimes.DAY * 3);
        return res;
    }

    @Override
    public boolean logout() {
        try {
            RedisUtil.remove(CacheKeys.USER_LOGIN + UserThreadUtil.getUserToken());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void bindUserHospital(int userId, List<Integer> hospitalList) {
        // 清历史数据
        Wrapper<UserHospital> w = new EntityWrapper<>();
        w.eq(UserRole.USER_ID, userId);
        userHospitalService.delete(w);
        // 插入新数据
        hospitalList.forEach(hospitalId -> {
            UserHospital data = new UserHospital();
            data.setUserId(userId);
            data.setHospitalId(hospitalId);
            userHospitalService.insert(data);
        });
    }

    /**
     * 添加用户信息
     */
    private int insert(UserParam param) {
        param.checkUserName();
        if (userNameExist(param.getUserName())) {
            throw new UserException("用户名已存在");
        }
        param.checkPassWord();
        param.checkName();
        param.checkRole();

        User data = new User();
        data.setUsername(param.getUserName());
        data.setPassword(MD5Util.encode(param.getPassWord()));
        data.setName(param.getName());
        data.setStatus(param.getStatus());
        data.setCreateTime(new Timestamp(System.currentTimeMillis()));
        data.setPwLog(param.getPassWord());
        data.setUpdateId(UserThreadUtil.getUserId());
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        data.setCreateId(UserThreadUtil.getUserId());
        data.setCreateTime(new Timestamp(System.currentTimeMillis()));
        if (userService.insert(data)) {
            roleService.bindUserRole(data.getId(), param.getRoleList());
            bindUserHospital(data.getId(), param.getHospitalList());
            return data.getId();
        }
        return 0;
    }

    /**
     * 更新用户信息
     */
    private int update(UserParam param) {
        User data = new User();
        data.setId(param.getId());
        data.setPassword(MD5Util.encode(param.getPassWord()));
        data.setName(param.getName());
        data.setPwLog(param.getPassWord());
        data.setUpdateId(UserThreadUtil.getUserId());
        data.setUpdateTime(new Timestamp(System.currentTimeMillis()));
        if (userService.updateById(data)) {
            roleService.bindUserRole(data.getId(), param.getRoleList());
            bindUserHospital(data.getId(), param.getHospitalList());
            return data.getId();
        }
        return 0;
    }

    /**
     * 根据 账号密码 获取用户信息
     */
    private User getUserByAccount(String userName, String passWord) {
        Wrapper<User> w = new EntityWrapper<>();
        w.eq(User.USERNAME, userName);
        w.eq(User.PASSWORD, passWord);
        return userService.selectOne(w);
    }

    /**
     * 根据 用户ID 获取已绑定角色ID集合
     */
    private Set<Integer> getUserRoleIds(int userId) {
        Set<Integer> ids = new HashSet<>();
        Wrapper<UserRole> w = new EntityWrapper<>();
        w.eq(UserRole.USER_ID, userId);
        List<UserRole> roleList = userRoleService.selectList(w);
        roleList.forEach(r -> ids.add(r.getRoleId()));
        return ids;
    }

    /**
     * 判断用户名是否存在
     */
    private boolean userNameExist(String userName) {
        Wrapper<User> w = new EntityWrapper<>();
        w.eq(User.USERNAME, userName);
        return userService.selectCount(w) > 0;
    }
}