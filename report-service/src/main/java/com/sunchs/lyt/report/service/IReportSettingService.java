package com.sunchs.lyt.report.service;

import com.sunchs.lyt.framework.bean.SelectChildData;
import com.sunchs.lyt.framework.bean.TitleData;

import java.util.List;

public interface IReportSettingService {

    /**
     * 获取项目已使用的问卷
     */
    List<TitleData> getItemUseQuestionnaireList(Integer itemId);

    /**
     *
     */
    List<SelectChildData> getItemUseAllList(Integer itemId);
}
