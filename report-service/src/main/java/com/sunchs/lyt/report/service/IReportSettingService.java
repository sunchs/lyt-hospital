package com.sunchs.lyt.report.service;

import com.sunchs.lyt.framework.bean.TitleData;
import com.sunchs.lyt.report.bean.CustomItemOfficeSettingParam;
import com.sunchs.lyt.report.bean.ItemAllSatisfySettingParam;
import com.sunchs.lyt.report.bean.TempItemOfficeSettingParam;
import com.sunchs.lyt.report.bean.TempOfficeData;

import java.util.List;
import java.util.Map;

public interface IReportSettingService {

    /**
     * 获取项目已使用的问卷
     */
    List<TitleData> getItemUseQuestionnaireList(Integer itemId);

    /**
     *
     */
    List<Map<String, Object>> getItemUseAllList(Integer itemId);

    /**
     * 根据 问卷ID 获取答卷中的指标
     */
    List<Map<String, Object>> getItemQuestionnaireUseTarget(Integer itemId, Integer id);

    /**
     * 保存自定义科室配置
     */
    void saveCustomItemOfficeSetting(CustomItemOfficeSettingParam param);

    /**
     * 删除自定义科室配置
     */
    void deleteCustomItemOfficeSetting(Integer id);

    /**
     * 保存临时科室配置
     */
    void saveTempItemOfficeSetting(TempItemOfficeSettingParam param);

    /**
     * 获取临时科室列表
     */
    List<TempOfficeData> getItemTempOfficeList(Integer itemId, Integer officeType);

    /**
     * 删除临时科室
     */
    void deleteItemTempOffice(Integer id);

    /**
     * 获取指标
     */
    List<Map<String, Object>> getItemTargetList(Integer itemId, Integer officeType);

    /**
     * 保存总体满意度设置
     */
    void saveItemAllSatisfySetting(ItemAllSatisfySettingParam param);

    /**
     * 删除总体满意度设置
     */
    void deleteItemAllSatisfySetting(Integer id);
}
