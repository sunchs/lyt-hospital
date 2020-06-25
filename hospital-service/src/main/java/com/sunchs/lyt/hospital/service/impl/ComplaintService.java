package com.sunchs.lyt.hospital.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.sunchs.lyt.db.business.entity.Hospital;
import com.sunchs.lyt.db.business.entity.HospitalComplaint;
import com.sunchs.lyt.db.business.entity.HospitalComplaintType;
import com.sunchs.lyt.db.business.entity.HospitalOffice;
import com.sunchs.lyt.db.business.service.impl.HospitalComplaintServiceImpl;
import com.sunchs.lyt.db.business.service.impl.HospitalComplaintTypeServiceImpl;
import com.sunchs.lyt.db.business.service.impl.HospitalOfficeServiceImpl;
import com.sunchs.lyt.db.business.service.impl.HospitalServiceImpl;
import com.sunchs.lyt.framework.bean.PagingList;
import com.sunchs.lyt.framework.bean.TitleValueChildrenData;
import com.sunchs.lyt.framework.bean.TitleValueData;
import com.sunchs.lyt.framework.enums.OfficeTypeEnum;
import com.sunchs.lyt.framework.util.FormatUtil;
import com.sunchs.lyt.framework.util.PagingUtil;
import com.sunchs.lyt.hospital.bean.ComplaintParam;
import com.sunchs.lyt.hospital.bean.HospitalComplaintData;
import com.sunchs.lyt.hospital.exception.HospitalException;
import com.sunchs.lyt.hospital.service.IComplaintService;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ComplaintService implements IComplaintService {

    @Autowired
    private HospitalComplaintServiceImpl hospitalComplaintService;

    @Autowired
    private HospitalComplaintTypeServiceImpl hospitalComplaintTypeService;

    @Autowired
    private HospitalServiceImpl hospitalService;

    @Autowired
    private HospitalOfficeServiceImpl hospitalOfficeService;

    @Override
    public void save(ComplaintParam param) {
        // 参数过滤
        param.filter();
        // 科室类型
        if (param.getOfficeTypeId() == 0) {
            Wrapper<HospitalOffice> wrapper = new EntityWrapper<HospitalOffice>()
                    .setSqlSelect(HospitalOffice.TYPE)
                    .eq(HospitalOffice.ID, param.getOfficeId());
            HospitalOffice hospitalOffice = hospitalOfficeService.selectOne(wrapper);
            if (Objects.nonNull(hospitalOffice)) {
                param.setOfficeTypeId(hospitalOffice.getType());
            }
        }
        // 保存数据
        try {
            HospitalComplaint data = new HospitalComplaint();
            data.setHospitalId(param.getHospitalId());
            data.setOfficeTypeId(param.getOfficeTypeId());
            data.setOfficeId(param.getOfficeId());
            data.setTypeId(param.getTypeId());
            data.setName(param.getName());
            data.setTel(param.getTel());
            data.setNumber(param.getNumber());
            data.setRespondent(param.getRespondent());
            data.setContent(param.getContent());
            data.setCreateTime(new Date());
            hospitalComplaintService.insert(data);
        } catch (Exception e) {
            throw new HospitalException("投诉信息保存失败:" + e.getMessage());
        }
    }

    @Override
    public PagingList<HospitalComplaintData> getList(ComplaintParam param) {
        Wrapper<HospitalComplaint> wrapper = new EntityWrapper<>();
        // 姓名
        if (Objects.nonNull(param.getName()) && param.getName().length() > 0) {
            wrapper.eq(HospitalComplaint.NAME, param.getName());
        }
        // 电话
        if (Objects.nonNull(param.getTel()) && param.getTel().length() > 0) {
            wrapper.eq(HospitalComplaint.TEL, param.getTel());
        }
        // 科室
        if (param.getOfficeId() != 0) {
            wrapper.eq(HospitalComplaint.OFFICE_ID, param.getOfficeId());
        }
        // 时间段
        if (Objects.nonNull(param.getStartTime()) && param.getStartTime().length() > 0) {
            wrapper.ge(HospitalComplaint.CREATE_TIME, param.getStartTime());
        }
        if (Objects.nonNull(param.getEndTime()) && param.getEndTime().length() > 0) {
            wrapper.le(HospitalComplaint.CREATE_TIME, param.getEndTime());
        }
        wrapper.orderBy(HospitalComplaint.ID, false);
        Page<HospitalComplaint> limit = new Page<>(param.getPageNow(), param.getPageSize());
        Page<HospitalComplaint> page = hospitalComplaintService.selectPage(limit, wrapper);
        List<HospitalComplaintData> list = formatData(page.getRecords());
        return PagingUtil.getData(list, page.getTotal(), page.getCurrent(), page.getSize());
    }

    @Override
    public List<HospitalComplaintType> getTypeList(Integer hospitalId) {
        Wrapper<HospitalComplaintType> wrapper = new EntityWrapper<HospitalComplaintType>()
                .setSqlSelect(
                        HospitalComplaintType.ID,
                        HospitalComplaintType.TITLE
                )
                .in(HospitalComplaintType.HOSPITAL_ID, Arrays.asList(hospitalId, 0));
        return hospitalComplaintTypeService.selectList(wrapper);
    }

    @Override
    public String outputFile(ComplaintParam param) {
        String path = "temp";
        initPath(path);
        String fileName = System.currentTimeMillis() +".xls";
        try {
            File file = new File(path + "/" + fileName);
            WritableWorkbook wb = jxl.Workbook.createWorkbook(file);
            // 改变默认颜色
            Color color = Color.decode("#EEA9B8");
            wb.setColourRGB(Colour.RED, color.getRed(), color.getGreen(), color.getBlue());
            // 写表头
            WritableCellFormat format = new WritableCellFormat();
            format.setBackground(Colour.RED);

            WritableSheet sheet = wb.createSheet("投诉报表", 0);

            int column = 0;
            int line = 0;
            // 标题
            sheet.addCell(new Label(column++, line, "上传日期", format));sheet.setColumnView(column, 20);
            sheet.addCell(new Label(column++, line, "投诉人", format));sheet.setColumnView(column, 20);
            sheet.addCell(new Label(column++, line, "联系方式", format));sheet.setColumnView(column, 20);
            sheet.addCell(new Label(column++, line, "就诊卡号", format));sheet.setColumnView(column, 20);
            sheet.addCell(new Label(column++, line, "投诉类型", format));sheet.setColumnView(column, 20);
            sheet.addCell(new Label(column++, line, "投诉科室", format));sheet.setColumnView(column, 20);
            sheet.addCell(new Label(column++, line, "投诉对象", format));sheet.setColumnView(column, 20);
            sheet.addCell(new Label(column++, line, "投诉建议", format));sheet.setColumnView(column, 60);

            // 获取数据
            PagingList<HospitalComplaintData> list = getList(param);
            for (HospitalComplaintData row : list.getList()) {
                line++;
                column = 0;

                sheet.addCell(new Label(column++, line, row.getCreateTime()));
                sheet.addCell(new Label(column++, line, row.getName()));
                sheet.addCell(new Label(column++, line, row.getTel()));
                sheet.addCell(new Label(column++, line, row.getNumber()));
                sheet.addCell(new Label(column++, line, row.getTypeName()));
                sheet.addCell(new Label(column++, line, row.getOfficeName()));
                sheet.addCell(new Label(column++, line, row.getRespondent()));
                sheet.addCell(new Label(column++, line, row.getContent()));
            }

            wb.write();
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        return path + "/" + fileName;
    }

    private List<HospitalComplaintData> formatData(List<HospitalComplaint> pageList) {
        List<HospitalComplaintData> list = new ArrayList<>();
        // 医院
        List<Integer> hospitalIds = pageList.stream().map(HospitalComplaint::getHospitalId).distinct().collect(Collectors.toList());
        Wrapper<Hospital> hospitalWrapper = new EntityWrapper<Hospital>()
                .setSqlSelect(
                        Hospital.ID,
                        Hospital.HOSPITAL_NAME.concat(" as hospitalName")
                )
                .in(Hospital.ID, hospitalIds);
        Map<Integer, String> hospitalMap = hospitalService.selectList(hospitalWrapper).stream().collect(Collectors.toMap(Hospital::getId, Hospital::getHospitalName));

        // 科室
        List<Integer> officeIds = pageList.stream().map(HospitalComplaint::getOfficeId).distinct().collect(Collectors.toList());
        Wrapper<HospitalOffice> officeWrapper = new EntityWrapper<HospitalOffice>()
                .setSqlSelect(
                        HospitalOffice.ID,
                        HospitalOffice.TITLE
                )
                .in(HospitalOffice.ID, officeIds);
        Map<Integer, String> hospitalOfficeMap = hospitalOfficeService.selectList(officeWrapper).stream().collect(Collectors.toMap(HospitalOffice::getId, HospitalOffice::getTitle));

        // 投诉类型
        List<Integer> typeIds = pageList.stream().map(HospitalComplaint::getTypeId).distinct().collect(Collectors.toList());
        Wrapper<HospitalComplaintType> typeWrapper = new EntityWrapper<HospitalComplaintType>()
                .setSqlSelect(
                        HospitalComplaintType.ID,
                        HospitalComplaintType.TITLE
                )
                .in(HospitalComplaintType.ID, typeIds);
        Map<Integer, String> typeMap = hospitalComplaintTypeService.selectList(typeWrapper).stream().collect(Collectors.toMap(HospitalComplaintType::getId, HospitalComplaintType::getTitle));

        pageList.forEach(row -> {
            HospitalComplaintData data = new HospitalComplaintData();
            data.setId(row.getId());
            data.setHospitalId(row.getHospitalId());
            data.setHospitalName(hospitalMap.get(row.getHospitalId()));
            data.setOfficeTypeId(row.getOfficeTypeId());
            data.setOfficeTypeName(OfficeTypeEnum.get(row.getOfficeTypeId()));
            data.setOfficeId(row.getOfficeId());
            data.setOfficeName(hospitalOfficeMap.get(row.getOfficeId()));
            data.setTypeId(row.getTypeId());
            data.setTypeName(typeMap.get(row.getTypeId()));
            data.setName(row.getName());
            data.setTel(row.getTel());
            data.setNumber(row.getNumber());
            data.setRespondent(row.getRespondent());
            data.setContent(row.getContent());
            data.setCreateTime(FormatUtil.dateTime(row.getCreateTime()));
            list.add(data);
        });
        return list;
    }

    private void initPath(String path) {
        File file1 = new File(path);
        if ( ! file1.exists()) {
            file1.mkdirs();
        }
    }
}
