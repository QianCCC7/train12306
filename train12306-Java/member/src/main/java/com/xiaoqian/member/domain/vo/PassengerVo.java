package com.xiaoqian.member.domain.vo;

import com.xiaoqian.member.enums.PassengerTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerVo {
    private Long id;

    private Long memberId;

    private String name;

    private String idCard;

    private PassengerTypeEnum type;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
