/**
 *
 */
package com.gccws.sysadmin.control.controller.auth;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.gccws.base.controller.BaseController;
import com.gccws.base.model.BaseResponse;
import com.gccws.base.utils.UserTokenRequestUtils;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.control.dto.AppUserDto;
import com.gccws.sysadmin.control.service.AppUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

import static com.gccws.base.utils.BaseConstants.*;
import static com.gccws.common.utils.CommonConstants.SYSTEM_ADMIN_END_POINT;

/**
 * @Author    Rima
 * @Since     August 30, 2022
 * @version   1.0.0
 */
@AllArgsConstructor
@RestController
@RequestMapping(SYSTEM_ADMIN_END_POINT + "app-user")
public class AppUserController implements BaseController<BaseResponse, AppUserDto, CommonPageableData, HttpServletRequest> {

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(AppUserController.class);
	private final AppUserService service;

	/* utils */
	private final CommonUtils commonUtils;
	private final UserTokenRequestUtils authTokenUtils;

	@Override
	@PostMapping
	public BaseResponse save(@Valid @RequestBody AppUserDto body, HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.save(body, authTokenUtils.getUserIdFromRequest(request)), SAVE_MESSAGE, SAVE_MESSAGE_BN);
		} catch (Exception e) {
			e.printStackTrace();
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@PutMapping
	public BaseResponse update(@Valid @RequestBody AppUserDto body, HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.update(body, authTokenUtils.getUserIdFromRequest(request)), UPDATE_MESSAGE, UPDATE_MESSAGE_BN);
		} catch (Exception e) {
			e.printStackTrace();
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@DeleteMapping
	public BaseResponse delete(@Valid @RequestBody AppUserDto body, HttpServletRequest request) {
		try {
			if(service.delete(body, authTokenUtils.getUserIdFromRequest(request))) {
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
			return commonUtils.generateSuccessResponse(service.getById(id, authTokenUtils.getUserIdFromRequest(request)));
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

	@PutMapping( value = "get-pageable-list-data-for-agent-profile", produces = EXTERNAL_MEDIA_TYPE)
	public BaseResponse getPageableListDataForAgentProfile(@Valid @RequestBody CommonPageableData pageableData, HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.getPageableListDataForAgentProfile(pageableData, authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@GetMapping(value = "get-by-emp-id/{empId}", produces = EXTERNAL_MEDIA_TYPE)
	public BaseResponse getByEmpPersonalInfoId(@PathVariable("empId") Integer empId, HttpServletRequest request) {
		try {
			return commonUtils.generateSuccessResponse(service.getByEmpPersonalInfoId(empId, authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}


}
