package com.gccws.sysadmin.control.dto;

import com.gccws.base.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author    Md. Chabed Alam
 * @Since     September 26, 2022
 * @version   1.0.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FileValidatorDto extends BaseDto {

	private String name;

	private String fileExtensions;

	private Integer fileSize;

	private Integer fileHeight;

	private Integer fileWidth;
	private Boolean isFixed;
	private Integer devCode;
}
