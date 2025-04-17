package com.xiaoqian.member.domain.dto;

import com.xiaoqian.member.enums.PassengerTypeEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDTO {
    private Long id;
    @NotBlank(message = "乘客姓名不能为空")
    private String name;
    @NotBlank(message = "乘客身份证号不能为空")
    private String idCard;
    @NotNull(message = "乘客类型不能为空")
    private PassengerTypeEnum type;

}
