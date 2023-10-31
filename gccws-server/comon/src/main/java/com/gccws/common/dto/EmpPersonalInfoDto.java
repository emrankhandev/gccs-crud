package com.gccws.common.dto;


import com.gccws.base.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Emran Khan
 * * @since July 11,2023
 * @version 1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class EmpPersonalInfoDto extends BaseDto implements Serializable{
	
	private static final long serialVersionUID = 1L;


    private String empId;
    private String empName;
    private String fatherName;
    private String motherName;
    private Date dateOfBirth;

    private Integer genderId;
    private String genderName;

    private Integer bloodGroupId;
    private String bloodGroupName;

    private Integer maritalStatusId;
    private String maritalStatusName;

    private Integer religionId;
    private String religionName;

    private Integer nationalityId;
    private String nationalityName;

    private String nid;

    private String signatureFileName;
    private String signatureFileLocation;

    private String profileImageFileName;
    private String profileImageFileLocation;

    /* present address */

    private String presentLocation;

    private Integer presentDivisionId;
    private String presentDivisionName;

    private Integer presentDistrictId;
    private String presentDistrictName;

    private Integer presentUpazilaId;
    private String presentUpazilaName;

    private String presentPostCode;

    /* permanent address */

    private String permanentLocation;

    private Integer permanentDivisionId;
    private String permanentDivisionName;

    private Integer permanentDistrictId;
    private String permanentDistrictName;

    private Integer permanentUpazilaId;
    private String permanentUpazilaName;

    private String permanentPostCode;

    private Boolean sameAsPresent = false;
    private String empPhone;
    private String empEmail;
    private String emergencyContactPersonName;
    private String emergencyContactPhone;
    private String empHomePhone;

    private Integer relationTypeId;
    private String relationTypeName;

    private Integer seniorityNumber;

    //extra
    private File sigfile;
    private File profileFile;

    private String designationNameExtra;
    private String departmentNameExtra;

}
