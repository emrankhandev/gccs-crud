package com.gccws.sysadmin.control.controller.auth;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.gccws.base.controller.BaseController;
import com.gccws.base.model.BaseResponse;
import com.gccws.base.utils.UserTokenRequestUtils;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.control.model.UserRoleAssignModel;
import com.gccws.sysadmin.control.service.UserRoleAssignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

import static com.gccws.base.utils.BaseConstants.*;
import static com.gccws.common.utils.CommonConstants.SYSTEM_ADMIN_END_POINT;

/**
 * @Author    Rima
 * @Since     September 1, 2022
 * @version   1.0.0
 */

@AllArgsConstructor
@RestController
@RequestMapping(SYSTEM_ADMIN_END_POINT +"user-role-assign")
public class UserRoleAssignController implements BaseController<BaseResponse, UserRoleAssignModel, CommonPageableData, HttpServletRequest> {

	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(UserRoleAssignController.class);
	
	private final UserRoleAssignService service;
	
	/* utils */
    private final CommonUtils commonUtils;
    private final UserTokenRequestUtils authTokenUtils;

	@Override
	@PostMapping
	public BaseResponse save(@Valid @RequestBody UserRoleAssignModel body, HttpServletRequest request) {
    	try {
    		return commonUtils.generateSuccessResponse(service.save(body, authTokenUtils.getUserIdFromRequest(request)), SAVE_MESSAGE, SAVE_MESSAGE_BN);
		} catch (Exception e) {
			e.printStackTrace();
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@PutMapping
	public BaseResponse update(@Valid @RequestBody UserRoleAssignModel body, HttpServletRequest request) {
    	try {
    		return commonUtils.generateSuccessResponse(service.update(body, authTokenUtils.getUserIdFromRequest(request)), UPDATE_MESSAGE, UPDATE_MESSAGE_BN);
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@DeleteMapping
	public BaseResponse delete(@Valid @RequestBody UserRoleAssignModel body,HttpServletRequest request) {
    	try {
    		if(service.delete(body,authTokenUtils.getUserIdFromRequest(request))) {
    			return commonUtils.generateSuccessResponse(null, DELETE_MESSAGE, DELETE_MESSAGE_BN);
    		}else {
    			return commonUtils.generateSuccessResponse(null, DELETE_MESSAGE_FAILED, DELETE_MESSAGE_FAILED_BN);
    		}
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@GetMapping(value = GET_OBJECT_BY_ID, produces = EXTERNAL_MEDIA_TYPE)
	public BaseResponse getById(@PathVariable(OBJECT_ID) int id, HttpServletRequest request) {
    	try {
    		return commonUtils.generateSuccessResponse(service.getById(id,authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@GetMapping(value = DROPDOWN_LIST_PATH, produces = EXTERNAL_MEDIA_TYPE)
    public BaseResponse getDropdownList(HttpServletRequest request){
    	try {
    		return commonUtils.generateSuccessResponse(service.getDropdownList(authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
    }

	@Override
	@PutMapping( value = PAGEABLE_DATA_PATH, produces = EXTERNAL_MEDIA_TYPE)
	public BaseResponse getPageableListData(@Valid @RequestBody CommonPageableData pageableData, HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.getPageableListData(pageableData, authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}
}
