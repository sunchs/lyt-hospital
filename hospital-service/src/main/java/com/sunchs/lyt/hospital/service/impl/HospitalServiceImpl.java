package com.sunchs.lyt.hospital.service.impl;

import com.sunchs.lyt.hospital.bean.HospitalData;
import com.sunchs.lyt.hospital.bean.HospitalParam;
import com.sunchs.lyt.hospital.dao.ipml.HospitalDaoImpl;
import com.sunchs.lyt.hospital.service.HospitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private HospitalDaoImpl hospitalDao;

    @Override
    public HospitalData save(HospitalParam param) {


        return null;
    }
}
