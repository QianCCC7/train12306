package com.xiaoqian.business.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDate;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 秒杀令牌
 * </p>
 *
 * @author xiaoqian
 * @since 2025-06-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("sk_token")
public class SkToken implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 日期
     */
    private LocalDate date;

    /**
     * 车次编号
     */
    private String trainCode;

    /**
     * 令牌余量
     */
    private Integer count;

    /**
     * 新增时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
