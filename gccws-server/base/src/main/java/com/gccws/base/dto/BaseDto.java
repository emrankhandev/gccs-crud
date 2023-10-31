package com.gccws.base.dto;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * @Author    Md. Chabed Alam
 * @Since     August 1, 2022
 * @version   1.0.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseDto implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private String name;
	private String banglaName;
	private Boolean active;

}
