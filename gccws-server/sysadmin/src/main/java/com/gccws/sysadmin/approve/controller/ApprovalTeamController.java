package com.gccws.sysadmin.approve.controller;

import com.gccws.base.controller.BaseController;
import com.gccws.base.model.BaseResponse;
import com.gccws.base.utils.UserTokenRequestUtils;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.approve.model.ApprovalTeamModel;
import com.gccws.sysadmin.approve.service.ApprovalTeamService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.gccws.base.utils.BaseConstants.*;
import static com.gccws.common.utils.CommonConstants.SYSTEM_ADMIN_END_POINT;

/**
 * @Author    Rima
 * @Since     February 22, 2023
 * @version   1.0.0
 */

@AllArgsConstructor
@RestController
@RequestMapping(SYSTEM_ADMIN_END_POINT +"approval-team")
public class ApprovalTeamController implements BaseController<BaseResponse, ApprovalTeamModel, CommonPageableData, HttpServletRequest> {

	
	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(ApprovalTeamController.class);
	
	private final ApprovalTeamService service;
	
	/* utils */
    private final CommonUtils commonUtils;
    private final UserTokenRequestUtils authTokenUtils;

	@Override
	@PostMapping
	public BaseResponse save(@Valid @RequestBody ApprovalTeamModel body, HttpServletRequest request) {
    	try {
    		return commonUtils.generateSuccessResponse(service.save(body, authTokenUtils.getUserIdFromRequest(request)), SAVE_MESSAGE, SAVE_MESSAGE_BN);
		} catch (Exception e) {
			e.printStackTrace();
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@PutMapping
	public BaseResponse update(@Valid @RequestBody ApprovalTeamModel body, HttpServletRequest request) {
    	try {
    		return commonUtils.generateSuccessResponse(service.update(body, authTokenUtils.getUserIdFromRequest(request)), UPDATE_MESSAGE, UPDATE_MESSAGE_BN);
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@Override
	@DeleteMapping
	public BaseResponse delete(@Valid @RequestBody ApprovalTeamModel body,HttpServletRequest request) {
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

	@GetMapping(value = "get-next-team-by-department-and-current-team-id/{departmentId}/{transactionTypeId}/{currentTeamId}")
	public BaseResponse getNextTeamByDepartmentAndCurrentTeamId(@PathVariable("departmentId") Integer  departmentId, @PathVariable("transactionTypeId") Integer  transactionTypeId, @PathVariable("currentTeamId") Integer  currentTeamId, HttpServletRequest request){
		try {
			return commonUtils.generateSuccessResponse(service.getNextTeamByDepartmentAndCurrentTeamId(departmentId, transactionTypeId, currentTeamId,  authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@GetMapping(value = "get-previous-team-by-department-and-current-team-id/{departmentId}/{transactionTypeId}/{currentTeamId}")
	public BaseResponse getPreviousTeamByDepartmentAndCurrentTeamId(@PathVariable("departmentId") Integer  departmentId, @PathVariable("transactionTypeId") Integer  transactionTypeId, @PathVariable("currentTeamId") Integer  currentTeamId, HttpServletRequest request){
		try {
			return commonUtils.generateSuccessResponse(service.getPreviousTeamByDepartmentAndCurrentTeamId(departmentId, transactionTypeId, currentTeamId,  authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}

	@GetMapping(value = "get-team-by-department-id/{departmentId}")
	public BaseResponse getTeamByDepartmentId(@PathVariable("departmentId") Integer  departmentId,  HttpServletRequest request){
		try {
			return commonUtils.generateSuccessResponse(service.getTeamByDepartmentId(departmentId,   authTokenUtils.getUserIdFromRequest(request)));
		} catch (Exception e) {
			return commonUtils.generateErrorResponse(e);
		}
	}


}
