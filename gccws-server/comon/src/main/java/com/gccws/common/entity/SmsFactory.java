package com.gccws.common.entity;


import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.Table;

import lombok.Data;


/**
 * @Author    Md. Chabed Alam
 * @Since     September 28, 2022
 * @version   1.0.0
 */

@Data
@Entity
@Table(name = "COMMON_SMS_FACTORY")
public class SmsFactory implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ID")
	private Integer id;
	
	@Column(name = "ENTRY_USER", updatable = false)
	private Integer entryUser;
	
	@Column(name = "ENTRY_DATE", updatable = false, nullable = false)
	private Date entryDate;
	
	@Column(name = "TRANSACTION_ID")
	private Integer transactionId;
	
	@Column(name = "TRANSACTION_TABLE", length = 100)
	private String transactionTable;
	
	@Column(name = "MOBILE_NO", length = 30, nullable = false)
    private String mobileNo;
	
	@Column(name = "SMS_TEXT", nullable = false)
	private String smsText;
	
	/* after response */
	@Column(name = "MESSAGE_ID", length = 40)
	private String MessageId;
	
	@Column(name = "STATUS", length = 10)
	private String Status;
	
	@Column(name = "STATUS_TEXT")
	private String StatusText;
	
	@Column(name = "ERROR_CODE", length = 20)
	private String ErrorCode;
	
	@Column(name = "ERROR_TEXT")
	private String ErrorText;
	
	@Column(name = "CURRENT_CREDIT", length = 50)
	private String CurrentCredit;
	
}
