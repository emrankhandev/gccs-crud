package com.gccws.base.model;

import java.io.Serializable;


import lombok.Data;


/**
 * @Author    Md. Chabed alam
 * @Since     August 1, 2022
 * @version   1.0.0
 */


@Data
public class BaseResponse implements Serializable{
	
	private static final long serialVersionUID = 1L;

	private boolean status;
	private String message;
	private String messageBn;
	private Object data;
	
}
