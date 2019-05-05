package com.sunchs.lyt.db.business.service.impl;

import com.sunchs.lyt.db.business.entity.Question;
import com.sunchs.lyt.db.business.mapper.QuestionMapper;
import com.sunchs.lyt.db.business.service.IQuestionService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 问题表 服务实现类
 * </p>
 *
 * @author king
 * @since 2019-05-05
 */
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

}
