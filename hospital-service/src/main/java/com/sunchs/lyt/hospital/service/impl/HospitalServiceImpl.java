package com.sunchs.lyt.hospital.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.Question;
import com.sunchs.lyt.db.business.service.IQuestionService;
import com.sunchs.lyt.db.business.service.impl.QuestionServiceImpl;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.util.JsonUtil;
import com.sunchs.lyt.framework.util.PagingUtil;
import com.sunchs.lyt.hospital.bean.HospitalBranchParam;
import com.sunchs.lyt.hospital.bean.HospitalData;
import com.sunchs.lyt.hospital.bean.HospitalParam;
import com.sunchs.lyt.hospital.dao.ipml.HospitalDaoImpl;
import com.sunchs.lyt.hospital.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalDaoImpl hospitalDao;

    @Autowired
    QuestionServiceImpl questionService;

    @Override
    public void save(HospitalParam param) {
        Wrapper<Question> where = new EntityWrapper<>();
        Page<Question> questionPage = questionService.selectPage(new Page<>(1, 5), where);
        List<Question> records = questionPage.getRecords();
        records.forEach(row-> System.out.println(row));
        System.out.println(questionPage);

//        Map<String, Object> opt = new HashMap<>();
//        opt.put("hospitalName", param.getHospitalName());
//        opt.put("hospitalType", param.getHospitalType());
//        opt.put("hospitalProperty", param.getHospitalProperty());
//        opt.put("subjection", param.getSubjection());
//        opt.put("address", param.getAddress());
//        opt.put("contacts", param.getContacts());
//        opt.put("contactInfo", param.getContactInfo());
//        opt.put("operationName", param.getOperationName());
//        opt.put("operationPhone", param.getOperationPhone());
//        opt.put("openBeds", param.getOpenBeds());
//        opt.put("remarks", param.getRemarks());
//        int hospitalId = 0;
//        if (param.getId() > 0) {
//            opt.put("id", param.getId());
//            hospitalId = hospitalDao.update(opt);
//        } else {
//            hospitalId = hospitalDao.insert(opt);
//        }
//        if (hospitalId > 0) {
//            setOutpatientOffice(hospitalId, param);
//            setInpatientOffice(hospitalId, param);
//            setSpecialOffice(hospitalId, param);
//            setHospitalBranch(hospitalId, param);
//            setOutpatientType(hospitalId, param);
//            setRegistrationMode(hospitalId, param);
//        }
    }

    @Override
    public PagingList<HospitalData> getPageList(HospitalParam param) {
        int total = hospitalDao.getCount(param);
        List<HospitalData> pageList = hospitalDao.getPageList(param);
        return PagingUtil.getData(pageList, total, param.getPageNow(), param.getPageSize());
    }

    @Override
    public HospitalData getById(int hospitalId) {
        return hospitalDao.getById(hospitalId);
    }

    /**
     * 门诊科室
     */
    private void setOutpatientOffice(int hospitalId, HospitalParam param) {
        // 删除历史数据
        String type = "outpatient_office";
        deleteExtend(hospitalId, type);
        // 添加新数据
        List<String> list = param.getOutpatientOffice();
        if (list != null && list.size() > 0) {
            for (String value : list) {
                insertExtend(hospitalId, type, value);
            }
        }
    }

    /**
     * 住院科室
     */
    private void setInpatientOffice(int hospitalId, HospitalParam param) {
        // 删除历史数据
        String type = "inpatient_office";
        deleteExtend(hospitalId, type);
        // 添加新数据
        List<String> list = param.getInpatientOffice();
        if (list != null && list.size() > 0) {
            for (String value : list) {
                insertExtend(hospitalId, type, value);
            }
        }
    }

    /**
     * 特殊科室
     */
    private void setSpecialOffice(int hospitalId, HospitalParam param) {
        // 删除历史数据
        String type = "special_office";
        deleteExtend(hospitalId, type);
        // 添加新数据
        List<String> list = param.getSpecialOffice();
        if (list != null && list.size() > 0) {
            for (String value : list) {
                insertExtend(hospitalId, type, value);
            }
        }
    }

    /**
     * 分院信息
     */
    private void setHospitalBranch(int hospitalId, HospitalParam param) {
        // 删除历史数据
        String type = "hospital_branch";
        deleteExtend(hospitalId, type);
        // 添加新数据
        List<HospitalBranchParam> list = param.getHospitalBranch();
        if (list != null && list.size() > 0) {
            for (HospitalBranchParam branchParam : list) {
                String value = JsonUtil.toJson(branchParam);
                insertExtend(hospitalId, type, value);
            }
        }
    }

    /**
     * 门诊类别
     */
    private void setOutpatientType(int hospitalId, HospitalParam param) {
        // 删除历史数据
        String type = "outpatient_type";
        deleteExtend(hospitalId, type);
        // 添加新数据
        List<String> list = param.getOutpatientType();
        if (list != null && list.size() > 0) {
            for (String value : list) {
                insertExtend(hospitalId, type, value);
            }
        }
    }

    /**
     * 挂号方式
     */
    private void setRegistrationMode(int hospitalId, HospitalParam param) {
        // 删除历史数据
        String type = "registration_mode";
        deleteExtend(hospitalId, type);
        // 添加新数据
        List<String> list = param.getRegistrationMode();
        if (list != null && list.size() > 0) {
            for (String value : list) {
                insertExtend(hospitalId, type, value);
            }
        }
    }

    /**
     * 插入扩展信息
     */
    private void insertExtend(int hospitalId, String type, String content) {
        Map<String, Object> opt = new HashMap<>();
        opt.put("hospitalId", hospitalId);
        opt.put("type", type);
        opt.put("content", content);
        hospitalDao.insertExtend(opt);
    }

    /**
     * 删除扩展信息
     */
    private void deleteExtend(int hospitalId, String type) {
        Map<String, Object> opt = new HashMap<>();
        opt.put("hospitalId", hospitalId);
        opt.put("type", type);
        hospitalDao.deleteExtend(opt);
    }
}
