package com.sunchs.lyt.item.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.sunchs.lyt.db.business.entity.Item;
import com.sunchs.lyt.db.business.entity.ItemOffice;
import com.sunchs.lyt.db.business.entity.Question;
import com.sunchs.lyt.db.business.entity.QuestionnaireExtend;
import com.sunchs.lyt.db.business.service.impl.ItemOfficeServiceImpl;
import com.sunchs.lyt.db.business.service.impl.ItemServiceImpl;
import com.sunchs.lyt.db.business.service.impl.QuestionServiceImpl;
import com.sunchs.lyt.db.business.service.impl.QuestionnaireExtendServiceImpl;
import com.sunchs.lyt.framework.enums.OfficeTypeEnum;
import com.sunchs.lyt.item.exception.ItemException;
import com.sunchs.lyt.item.service.IItemFileService;
import jxl.Workbook;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ItemFileService implements IItemFileService {

    @Autowired
    private ItemOfficeServiceImpl itemOfficeService;
    @Autowired
    private QuestionnaireExtendServiceImpl questionnaireExtendService;
    @Autowired
    private QuestionServiceImpl questionService;
    @Autowired
    private ItemServiceImpl itemService;


    @Override
    public String getItemAnswerInputTemplate(Integer itemId, Integer officeType, Integer officeId) {
        // 查询科室绑定数据
        Wrapper<ItemOffice> itemOfficeWrapper = new EntityWrapper<ItemOffice>()
                .eq(ItemOffice.ITEM_ID, itemId)
                .eq(ItemOffice.OFFICE_TYPE_ID, officeType)
                .eq(ItemOffice.OFFICE_ID, officeId);
        ItemOffice itemOffice = itemOfficeService.selectOne(itemOfficeWrapper);
        if (Objects.isNull(itemOffice)) {
            throw new ItemException("项目和科室无绑定状态，请重新选择项目和科室！");
        }
        // 判断是否有问卷
        if (itemOffice.getQuestionnaireId() > 0) {
            throw new ItemException("无绑定问卷，请绑定问卷再试！");
        }
        // 查询问卷题目
        Wrapper<QuestionnaireExtend> questionnaireExtendWrapper = new EntityWrapper<QuestionnaireExtend>()
                .setSqlSelect(QuestionnaireExtend.QUESTION_ID.concat(" AS questionId"))
                .eq(QuestionnaireExtend.QUESTIONNAIRE_ID, itemOffice.getQuestionnaireId());
        List<QuestionnaireExtend> tempQuestionnaireExtendList = questionnaireExtendService.selectList(questionnaireExtendWrapper);
        List<Integer> questionIds = tempQuestionnaireExtendList.stream().map(QuestionnaireExtend::getQuestionId).collect(Collectors.toList());
        if (questionIds.size() == 0) {
            throw new ItemException("科室绑定的问卷没有题目，请绑定题目再试！");
        }
        Wrapper<Question> questionWrapper = new EntityWrapper<Question>()
                .in(Question.ID, questionIds);
        List<Question> questionList = questionService.selectList(questionWrapper);

        // 获取项目信息
        Wrapper<Item> itemWrapper = new EntityWrapper<Item>()
                .setSqlSelect(Item.TITLE)
                .eq(Item.ID, itemId);
        Item item = itemService.selectOne(itemWrapper);
        String itemName = item.getTitle();
        String officeTypeName = OfficeTypeEnum.get(officeType);
        String officeName = (officeType.equals(3) ? "员工" : itemOffice.getTitle());
        String sheetName = itemName + "-" + officeTypeName + "-" + officeName;


        // -------------------------
        String path = "/lyt";
        initPath(path);
        String fileName = System.currentTimeMillis() +".xls";

        new Thread(()->{
            try {
                File file = new File(path + "/" + fileName);
                WritableWorkbook wb = Workbook.createWorkbook(file);
                // 改变默认颜色
                Color color = Color.decode("#EEA9B8");
                wb.setColourRGB(Colour.RED, color.getRed(), color.getGreen(), color.getBlue());
                WritableSheet sheet = wb.createSheet(sheetName, 0);
                // 写表头
                WritableCellFormat format = new WritableCellFormat();
                format.setBackground(Colour.RED);

                int columnPos = 0;
                int linePos = 0;
                sheet.addCell(new Label(columnPos++, linePos, "患者编号", format));
                sheet.addCell(new Label(columnPos++, linePos, "调查开始", format));
                sheet.addCell(new Label(columnPos++, linePos, "调查结束", format));
                for (Question question : questionList) {
                    sheet.addCell(new Label(columnPos++, linePos, question.getTitle(), format));
                }

                // 列宽度
                for (int i = 0; i < columnPos; i++) {
                    sheet.setColumnView(i, 18);
                }

                wb.write();
                wb.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        return path + "/" + fileName;
    }

    private void initPath(String path) {
        File file1 = new File(path);
        if ( ! file1.exists()) {
            file1.mkdirs();
        }
    }
}
