package com.xiaoqian.business.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StationDTO {
    private Long id;
    @NotBlank(message = "车站名称不能为空")
    private String name;
    @NotBlank(message = "车站拼音不能为空")
    private String namePinyin;
    @NotBlank(message = "站名拼音首字母不能为空")
    private String namePy;

}
