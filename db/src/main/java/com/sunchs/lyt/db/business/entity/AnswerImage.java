package com.sunchs.lyt.db.business.entity;

import com.baomidou.mybatisplus.enums.IdType;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

/**
 * <p>
 * 答案图片表
 * </p>
 *
 * @author king
 * @since 2019-07-29
 */
@TableName("answer_image")
public class AnswerImage extends Model<AnswerImage> {

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 答案ID
     */
    @TableField("answer_id")
    private Integer answerId;

    /**
     * 图片路径
     */
    private String path;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public Integer getAnswerId() {
        return answerId;
    }

    public void setAnswerId(Integer answerId) {
        this.answerId = answerId;
    }
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static final String ID = "id";

    public static final String ANSWER_ID = "answer_id";

    public static final String PATH = "path";

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "AnswerImage{" +
        "id=" + id +
        ", answerId=" + answerId +
        ", path=" + path +
        "}";
    }
}
