package com.gccws.common.model;

import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;


@Data
@XmlRootElement(name = "ArrayOfServiceClass")
@XmlAccessorType(XmlAccessType.FIELD)
public class SmsResponseCollection {
	
    @XmlElement(name = "ServiceClass")
    private List<SmsResponse> reserveInfoList;

}

