package com.gccws.common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.gccws.common.model.CommonPageableData;
import com.gccws.common.model.SmsFactoryModel;
import com.gccws.common.service.SmsFactoryService;
import com.gccws.common.utils.CommonUtils;
import com.gccws.base.controller.BaseController;
import com.gccws.base.model.BaseResponse;
import com.gccws.base.utils.BaseConstants;
import com.gccws.base.utils.UserTokenRequestUtils;
import com.gccws.common.utils.CommonConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

/**
 * @Author    Rima
 * @Since     August 28, 2022
 * @version   1.0.0
 */

@AllArgsConstructor
@RestController
@RequestMapping(CommonConstants.COMMON_END_POINT + "sms-factory")
public class SmsFactoryController
		implements BaseController<BaseResponse, SmsFactoryModel, CommonPageableData, HttpServletRequest> {
	

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(SmsFactoryController.class);

	private final SmsFactoryService service;

    /* utils */
    private final CommonUtils commonUtils;
    private final UserTokenRequestUtils authTokenUtils;
    
    @Override
	@PostMapping
	public BaseResponse save(@Valid @RequestBody SmsFactoryModel body, HttpServletRequest request) {
    	try {
    		return commonUtils.generateSuccessResponse(service.save(body, authTokenUtils.getUserIdFromRequest(request)), BaseConstants.SMS_MESSAGE, BaseConstants.SMS_MESSAGE_BN);
		} catch (Exception e) {
			e.printStackTrace();
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@PutMapping
	public BaseResponse update(@Valid @RequestBody  SmsFactoryModel body, HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.update(body, authTokenUtils.getUserIdFromRequest(request)), BaseConstants.UPDATE_MESSAGE, BaseConstants.UPDATE_MESSAGE_BN);
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@DeleteMapping
	public BaseResponse delete(@Valid @RequestBody SmsFactoryModel body, HttpServletRequest request) {
    	try {
    		if(service.delete(body, authTokenUtils.getUserIdFromRequest(request))) {
    			return commonUtils.generateSuccessResponse(null, BaseConstants.DELETE_MESSAGE, BaseConstants.DELETE_MESSAGE_BN);
    		}else {
    			return commonUtils.generateSuccessResponse(null, BaseConstants.DELETE_MESSAGE_FAILED, BaseConstants.DELETE_MESSAGE_FAILED_BN);
    		}
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@GetMapping( value = BaseConstants.GET_OBJECT_BY_ID, produces = BaseConstants.EXTERNAL_MEDIA_TYPE)
	public BaseResponse getById(@PathVariable(BaseConstants.OBJECT_ID) int id, HttpServletRequest request) {
    	try {
    		return commonUtils.generateSuccessResponse(service.getById(id, authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@GetMapping(value = BaseConstants.DROPDOWN_LIST_PATH, produces = BaseConstants.EXTERNAL_MEDIA_TYPE)
    public BaseResponse getDropdownList(HttpServletRequest request){
    	try {
    		return commonUtils.generateSuccessResponse(service.getDropdownList(authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
    }

	@Override
	@PutMapping( value = BaseConstants.PAGEABLE_DATA_PATH, produces = BaseConstants.EXTERNAL_MEDIA_TYPE)
	public BaseResponse getPageableListData(@Valid @RequestBody CommonPageableData pageableData, HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.getPageableListData(pageableData, authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

//	@GetMapping( value = PAGEABLE_PATH, produces = EXTERNAL_MEDIA_TYPE)
//	public BaseResponse getPageableList(@PathVariable(PAGEABLE_PAGE) int page, @PathVariable(PAGEABLE_SIZE) int size, @PathVariable(PAGEABLE_SEARCH_VALUE) String searchValue, HttpServletRequest request) {
//    	try {
//    		return commonUtils.generateSuccessResponse(service.getPageableList(page, size, searchValue, authTokenUtils.getUserIdFromRequest(request)));
//		} catch (Exception e) {
//			return commonUtils.generateErrorResponse(e);
//		}
//	}
    
}
