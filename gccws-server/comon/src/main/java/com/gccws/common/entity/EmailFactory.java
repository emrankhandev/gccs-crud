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
@Table(name = "COMMON_EMAIL_FACTORY")
public class EmailFactory implements Serializable{

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
	
	@Column(name = "RECIPIENT_EMAIL", length = 200)
    private String recipient;
	
	@Column(name = "EMAIL_SUBJECT")
	private String subject;
	
	@Column(name = "EMAIL_BODY")
	private String msgBody;
	
	
}
