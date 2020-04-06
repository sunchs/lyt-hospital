package com.sunchs.lyt.report.service;

import com.sunchs.lyt.db.business.entity.ItemTempOffice;
import com.sunchs.lyt.framework.bean.TitleData;
import com.sunchs.lyt.framework.bean.TitleValueChildrenData;
import com.sunchs.lyt.framework.bean.TitleValueData;
import com.sunchs.lyt.report.bean.*;

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
    TempOfficeDataVO getItemTempOfficeList(Integer itemId, Integer officeType);

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
    void deleteItemAllSatisfySetting(Integer itemId, Integer officeType);

    /**
     * 获取总体满意度设置
     */
    List<Map<String, Object>> getItemAllSatisfySettingList(Integer itemId, Integer officeType);

    /**
     * 根据 项目ID 获取临床科室
     */
    List<TitleChildrenVO> getItemTempOfficeChildren(Integer itemId);

    /**
     * 获取临床科室配置信息
     */
    List<TitleChildrenVO> getItemTempOfficeSettingV2(Integer itemId, Integer officeType);

    /**
     * 获取临床科室满意度
     */
    List<TitleValueChildrenData> getItemTempOfficeSatisfyList(Integer itemId, Integer officeType);

    /**
     * 获取临床科室满意度（带排名）
     */
    Map<String, Object> getItemTempOfficeSatisfyAndRankingList(Integer itemId, Integer officeType);

    /**
     * 从 临时科室表 提取配置数据
     */
    List<ItemTempOffice> getItemTempOfficeSettingList(Integer itemId, Integer officeType);

    /**
     * 根据 科室ID集合 获取答卷指标列表
     */
    List<TitleData> getTargetListByOfficeIds(ItemSettingParam param);
}
