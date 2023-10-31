package com.gccws.sysadmin.control.controller.noauth;

import com.gccws.base.model.BaseResponse;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.control.service.FileValidatorService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import static com.gccws.base.utils.BaseConstants.EXTERNAL_MEDIA_TYPE;
import static com.gccws.common.utils.CommonConstants.SYSTEM_PUBLIC_ADMIN_END_POINT;


/**
 * @Author    Md. Chabed Alam
 * @Since     September 26, 2022
 * @version   1.0.0
 */

@AllArgsConstructor
@RestController
@RequestMapping(SYSTEM_PUBLIC_ADMIN_END_POINT + "file-validator")
public class FileValidatorPublicController {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(FileValidatorPublicController.class);

	private final FileValidatorService service;

	/* utils */
	private final CommonUtils commonUtils;

	@GetMapping( value = "get-by-dev-code/{devCode}", produces = EXTERNAL_MEDIA_TYPE)
	public BaseResponse getByDevCode(@PathVariable("devCode") int devCode) {
		try {
			return commonUtils.generateSuccessResponse(service.getByDevCode(devCode));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}


}
