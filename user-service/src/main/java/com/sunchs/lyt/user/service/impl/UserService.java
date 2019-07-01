package com.sunchs.lyt.user.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.*;
import com.sunchs.lyt.db.business.service.impl.*;
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
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private RoleServiceImpl rService;

    @Autowired
    private HospitalServiceImpl hospitalService;

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
        w.orderBy(User.ID, false);
        Page<User> page = userService.selectPage(new Page<>(param.getPageNow(), param.getPageSize()), w);
        List<UserData> list = new ArrayList<>();
        page.getRecords().forEach(row->{
            UserData data = ObjectUtil.copy(row, UserData.class);
            data.setStatusName(StatusEnum.getName(data.getStatus()));

            // 代码简洁优化
            Wrapper<UserRole> uw = new EntityWrapper<>();
            uw.eq(UserRole.USER_ID, row.getId());
            List<UserRole> userRoleList = userRoleService.selectList(uw);
            List<Integer> roleIds = userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList());
            roleIds.forEach(id -> data.setRoleId(id));// TODO:: 只取了一个，日后优化

            if (roleIds.size() > 0) {
                Wrapper<Role> rw = new EntityWrapper<>();
                rw.in(Role.ID, roleIds);
                List<Role> roleList = rService.selectList(rw);
                List<String> roleNameList = roleList.stream().map(Role::getTitle).collect(Collectors.toList());
                data.setRoleName(StringUtils.join(roleNameList, ","));
            } else {
                data.setRoleName("无绑定");
            }

            Wrapper<UserHospital> uhw = new EntityWrapper<>();
            uhw.eq(UserHospital.USER_ID, row.getId());
            List<UserHospital> userHospitalList = userHospitalService.selectList(uhw);
            List<Integer> hospitalIds = userHospitalList.stream().map(UserHospital::getHospitalId).collect(Collectors.toList());
            hospitalIds.forEach(id -> data.setHospitalId(id));// TODO:: 只取了一个，日后优化

            if (hospitalIds.size() > 0) {
                Wrapper<Hospital> hw = new EntityWrapper<>();
                hw.in(Hospital.ID, hospitalIds);
                List<Hospital> hospitalList = hospitalService.selectList(hw);
                List<String> hospitalNameList = hospitalList.stream().map(Hospital::getHospitalName).collect(Collectors.toList());
                data.setHospitalName(StringUtils.join(hospitalNameList, ","));
            } else {
                data.setHospitalName("无限制");
            }

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
        if (StringUtil.isNotEmpty(param.getPassWord())) {
            data.setPassword(MD5Util.encode(param.getPassWord()));
            data.setPwLog(param.getPassWord());
        }
        data.setName(param.getName());
        data.setStatus(param.getStatus());
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