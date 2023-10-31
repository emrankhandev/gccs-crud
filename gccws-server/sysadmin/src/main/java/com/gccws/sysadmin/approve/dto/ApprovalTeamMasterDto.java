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
 * @Since     February 22, 2023
 * @version   1.0.0
 */  
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalTeamMasterDto extends BaseDto implements Serializable{
	
	private static final long serialVersionUID = 1L;

    private String remarks;

    private Integer devCode;

}
