package com.gccws.common.dto;

import com.gccws.base.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author Emran Khan
 * * @since February 06,2023
 * @version 1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class BankInfoDto extends BaseDto {

    private String name;
    private String nameBn;
    private String swiftCode;
    private Integer moduleId;

}
