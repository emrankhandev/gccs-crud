package com.gccws.common.dto;

import java.io.Serializable;
import java.util.Date;


import com.gccws.base.dto.BaseDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author    Rima
 * @Since     August 28, 2022
 * @version   1.0.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class SmsFactoryDto extends BaseDto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Date entryDate;
	
    private String mobileNo;
	
	private String smsText;
	
	/* after response */
	private String MessageId;
	
	private String Status;
	
	private String StatusText;
	
	private String ErrorCode;
	
	private String ErrorText;
	
	private String CurrentCredit;
	
}
