package com.gccws.common.dto;

import com.gccws.base.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author    Emran Khan
 * @Since     July 11, 2022
 * @version   1.0.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class EmpOfficialInfoDto extends BaseDto implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer empPersonalInfoId;
    private String empPersonalInfoName;

    private Integer designationId;
    private String designationName;

    private Integer officeId;
    private String officeName;

    private Integer gradeId;
    private String gradeName;

    private Integer organizationId;
    private String organizationName;

    private Integer departmentId;
    private String departmentName;

    private Integer sectionId;
    private String sectionName;

    private Integer subSectionId;
    private String subSectionName;

    private Date joiningDate;
    private Date prlDate;
    private Date prlEndDate;
    private Date retiredDate;

    private Integer empTypeId;
    private String empTypeName;

    private Integer empCatTypeId;
    private String empCatTypeName;

    private Double basicSalary;
    private Boolean isRestrict;
    private String isRestrictRemarks;

    private Integer bankBranchId;
    private String bankBranchName;

    private String bankAccountName;
    private String bankAccountNumber;
    private Integer educationChild;

    private Boolean gpf;
    private Double gpfPercentage;

    private Date inactiveDate;

    private Integer inactiveTypeId;
    private String inactiveTypeName;

    private String inactiveReason;
    private Boolean freedomFighter;

}
