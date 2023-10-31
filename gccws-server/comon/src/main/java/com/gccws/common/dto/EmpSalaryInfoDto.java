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
public class EmpSalaryInfoDto extends BaseDto {

    private Integer empPersonalInfoId;
    private String empPersonalInfoName;

    private Integer salaryHeadId;
    private String salaryHeadName;
    private Boolean isDeduction;

    private Double amount;
    private Integer totalInstallmentAmount;
    private Integer startInstallmentAmount;

}
