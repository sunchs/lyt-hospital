package com.sunchs.lyt.user.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.*;
import com.sunchs.lyt.db.business.service.impl.*;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.constants.CacheKeys;
import com.sunchs.lyt.framework.constants.DateTimes;
import com.sunchs.lyt.framework.enums.UserTypeEnum;
import com.sunchs.lyt.framework.util.*;
import com.sunchs.lyt.user.bean.UserData;
import com.sunchs.lyt.user.bean.UserParam;
import com.sunchs.lyt.user.bean.UserRoleData;
import com.sunchs.lyt.user.enums.StatusEnum;
import com.sunchs.lyt.user.exception.UserException;
import com.sunchs.lyt.user.service.IUserService;
import org.apache.commons.collections.CollectionUtils;
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
        if (UserThreadUtil.getType() == 1) {
            // 超级管理员账号操作
            if (NumberUtil.isZero(param.getId())) {
                return bossInsert(param);
            } else {
                return bossUpdate(param);
            }
        } else {
            // 普通账号操作
            if (NumberUtil.isZero(param.getId())) {
                return otherInsert(param);
            } else {
                return bossUpdate(param);
            }
        }
    }

    @Override
    public PagingList<UserData> getPagingList(UserParam param) {
        Wrapper<User> wrapper = new EntityWrapper<>();
        wrapper.ne(User.ID, 9);
        wrapper.orderBy(User.ID, false);
        // 医院之间账号独立
        if (UserThreadUtil.getType() != 1) {
            Wrapper<UserHospital> userHospitalWrapper = new EntityWrapper<UserHospital>()
                    .setSqlSelect(UserHospital.USER_ID.concat(" as userId"))
                    .eq(UserHospital.HOSPITAL_ID, UserThreadUtil.getHospitalId());
            List<UserHospital> userHospitals = userHospitalService.selectList(userHospitalWrapper);
            List<Integer> userIds = userHospitals.stream().map(UserHospital::getUserId).collect(Collectors.toList());
            if (Objects.nonNull(userIds)) {
                wrapper.in(User.ID, userIds);
            } else {
                return PagingUtil.Empty(param.getPageNow(), param.getPageSize());
            }
        }

        Page<User> page = userService.selectPage(new Page<>(param.getPageNow(), param.getPageSize()), wrapper);
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

        Wrapper<UserHospital> userHospitalWrapper = new EntityWrapper<UserHospital>()
                .eq(UserHospital.USER_ID, user.getId());
        UserHospital userHospital = userHospitalService.selectOne(userHospitalWrapper);

        // 结果集
        UserRoleData res = new UserRoleData();
        res.setId(user.getId());
        res.setType(user.getType());
        res.setUserName(user.getUsername());
        res.setName(user.getName());
        res.setToken(token);
        res.setRoleList(getUserRoleIds(user.getId()));
        res.setHospitalId(Objects.nonNull(userHospital) ? userHospital.getHospitalId() : 0);

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
    public void bindUserHospital(int userId, UserParam param) {
        // 清历史数据
        Wrapper<UserHospital> w = new EntityWrapper<>();
        w.eq(UserRole.USER_ID, userId);
        userHospitalService.delete(w);
        // 插入新数据
        if (param.getType() != 1) {
            param.getHospitalList().forEach(hospitalId -> {
                UserHospital data = new UserHospital();
                data.setUserId(userId);
                data.setHospitalId(hospitalId);
                userHospitalService.insert(data);
            });
        } else {
            UserHospital data = new UserHospital();
            data.setUserId(userId);
            data.setHospitalId(0);
            userHospitalService.insert(data);
        }
    }

    @Override
    public List<Map<String, Object>> getUsableList() {
        Wrapper<User> wrapper = new EntityWrapper<>();
        wrapper.setSqlSelect(User.ID, User.USERNAME, User.NAME);
        wrapper.eq(User.STATUS, 1);

        // 非全局账号
        if (UserThreadUtil.getType() != UserTypeEnum.ADMIN.value) {
            Wrapper<UserHospital> userHospitalWrapper = new EntityWrapper<UserHospital>()
                    .setSqlSelect(UserHospital.USER_ID.concat(" as userId"))
                    .eq(UserHospital.HOSPITAL_ID, UserThreadUtil.getHospitalId());
            List<UserHospital> userHospitals = userHospitalService.selectList(userHospitalWrapper);
            List<Integer> userIds = userHospitals.stream().map(UserHospital::getUserId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(userIds)) {
                wrapper.in(User.ID, userIds);
            }
        }

        wrapper.orderBy(User.ID, false);
        List<User> userList = userService.selectList(wrapper);
        List<Map<String, Object>> list = new ArrayList<>();
        userList.forEach(row -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", row.getId());
            m.put("username", row.getUsername());
            m.put("name", row.getName());
            list.add(m);
        });
        return list;
    }

    /**
     * 添加用户信息
     */
    private int bossInsert(UserParam param) {
        // 参数判断
        param.checkUserName();
        if (userNameExist(param.getUserName())) {
            throw new UserException("用户名被占用，请使用其他用户名！");
        }
        param.checkPassWord();
        param.checkName();
        param.checkRole();
        // 非超级管理员判断
        if (UserThreadUtil.getType() == 1 && param.getType() != 1) {
            param.checkHospital();
        }
        if (UserThreadUtil.getType() != 1) {
            param.checkAccess();
        }

        User data = new User();
        data.setType(param.getType()>0 ? param.getType() : 2);
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
            roleService.bindUserRole(data.getId(), param);
            bindUserHospital(data.getId(), param);
            return data.getId();
        }
        return 0;
    }
    private int otherInsert(UserParam param) {
        // 参数判断
        param.checkUserName();
        if (userNameExist(param.getUserName())) {
            throw new UserException("用户名被占用，请使用其他用户名！");
        }
        param.checkPassWord();
        param.checkName();
        param.checkRole();

        User data = new User();
        data.setType(2);
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
            // 插入角色数据
            param.getRoleList().forEach(roleId -> {
                UserRole userRole = new UserRole();
                userRole.setUserId(data.getId());
                userRole.setRoleId(roleId);
                userRoleService.insert(userRole);
            });
            // 插入医院数据
            UserHospital userHospital = new UserHospital();
            userHospital.setUserId(data.getId());
            userHospital.setHospitalId(UserThreadUtil.getHospitalId());
            userHospitalService.insert(userHospital);
            return data.getId();
        }
        return 0;
    }

    /**
     * 更新用户信息
     */
    private int bossUpdate(UserParam param) {
        // 参数判断
        param.checkName();
        param.checkRole();
        // 非超级管理员判断
//        if (UserThreadUtil.getType() == 1 && param.getType() != 1) {
//            param.checkHospital();
//        }
//        if (UserThreadUtil.getType() != 1) {
//            param.checkAccess();
//        }

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
            roleService.bindUserRole(data.getId(), param);
//            bindUserHospital(data.getId(), param);
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