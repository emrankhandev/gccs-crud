package com.gccws.common.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Data
@XmlRootElement(name = "ServiceClass")
public class SmsResponse {
	
	@XmlElement(name = "MessageId")
    private String MessageId;
	
	@XmlElement(name = "Status")
    private String Status;
	
	@XmlElement(name = "StatusText")
    private String StatusText;
	
	@XmlElement(name = "ErrorCode")
	private String ErrorCode;

	@XmlElement(name = "ErrorText")
	private String ErrorText;
	
	@XmlElement(name = "CurrentCredit")
	private String CurrentCredit;
	
}
