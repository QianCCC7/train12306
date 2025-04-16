package com.xiaoqian.member.domain.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 乘车人
 * </p>
 *
 * @author xiaoqian
 * @since 2025-04-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("passenger")
public class Passenger implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    @TableId(value = "id", type = IdType.NONE)
    private Long id;

    /**
     * 会员id
     */
    private Long memberId;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证
     */
    private String idCard;

    /**
     * 旅客类型
     */
    private String type;

    /**
     * 新增时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;


}
