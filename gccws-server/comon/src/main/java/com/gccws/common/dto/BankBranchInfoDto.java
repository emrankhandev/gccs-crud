package com.gccws.common.dto;


import com.gccws.base.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;


/**
 * @author Emran Khan
 * * @since February 06,2023
 * @version 1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class BankBranchInfoDto extends BaseDto {

    private Integer bankInfoId;
    private String bankInfoName;

    private Integer accountTypeId;
    private String accountTypeName;

    private String name;
    private String nameBn;
    private String routingNo;
    private String address;

    private String accountNo;
    private Date openingDate;
    private Boolean isCompanyAccount;
    private Integer moduleId;

}
