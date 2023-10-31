/**
 * 
 */
package com.gccws.sysadmin.approve.dto;

import com.gccws.base.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author    Rima
 * @Since     February 23, 2023
 * @version   1.0.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalConfigurationDto extends BaseDto implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private Integer departmentId;
	private String  departmentName;

	private Integer transactionTypeId;
	private String transactionTypeName;

	private Integer moduleId;
	private String  moduleName;
	private Integer serialNo;
    private Integer fromTeamId;
    private String  fromTeamName;
    private Integer toTeamId;
    private String  toTeamName;
    private Integer notifyAppUserId;
	private String  notifyAppUserName;
	private Double  fromAmount;
	private Double  toAmount;

	private Boolean approvalPermission;
	private Boolean backPermission;
	private Boolean changePermission;

}
