package com.gccws.sysadmin.report.controller;

import com.gccws.base.controller.BaseController;
import com.gccws.base.model.BaseResponse;
import com.gccws.base.utils.UserTokenRequestUtils;
import com.gccws.common.model.CommonPageableData;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.control.controller.auth.UserRoleAssignController;
import com.gccws.sysadmin.report.model.ParameterAssignModel;
import com.gccws.sysadmin.report.service.ParameterAssignService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.gccws.base.utils.BaseConstants.*;
import static com.gccws.common.utils.CommonConstants.*;


/**
 * @Author		Rima
 * @Since		January 10, 2023
 * @version		1.0.0
 */

@AllArgsConstructor
@RestController
@RequestMapping(SYSTEM_ADMIN_END_POINT + "parameter-assign")
public class ParameterAssignController implements BaseController<BaseResponse, ParameterAssignModel, CommonPageableData, HttpServletRequest> {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(UserRoleAssignController.class);
    private final ParameterAssignService service;

    /* utils */
    private final CommonUtils commonUtils;
    private final UserTokenRequestUtils tokenUtils;

    @Override
    @PostMapping
    public BaseResponse save(@Valid @RequestBody ParameterAssignModel body, HttpServletRequest request) {
        try {
            return commonUtils.generateSuccessResponse(service.save(body, tokenUtils.getUserIdFromRequest(request)), SAVE_MESSAGE, SAVE_MESSAGE_BN);
        } catch (Exception e) {
            e.printStackTrace();
            return commonUtils.generateErrorResponse(e);
        }
    }

    @Override
    @PutMapping
    public BaseResponse update(@Valid @RequestBody ParameterAssignModel body, HttpServletRequest request) {
        try {
            return commonUtils.generateSuccessResponse(service.update(body, tokenUtils.getUserIdFromRequest(request)), UPDATE_MESSAGE, UPDATE_MESSAGE_BN);
        } catch (Exception e) {
            return commonUtils.generateErrorResponse(e);
        }
    }

    @Override
    @DeleteMapping
    public BaseResponse delete(@Valid @RequestBody ParameterAssignModel body, HttpServletRequest request) {
        try {
            if(service.delete(body, tokenUtils.getUserIdFromRequest(request))) {
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
            return commonUtils.generateSuccessResponse(service.getById(id, tokenUtils.getUserIdFromRequest(request)));
        } catch (Exception e) {
            return commonUtils.generateErrorResponse(e);
        }
    }

    @Override
    @GetMapping(value = DROPDOWN_LIST_PATH, produces = EXTERNAL_MEDIA_TYPE)
    public BaseResponse getDropdownList(HttpServletRequest request) {
        try {
            return commonUtils.generateSuccessResponse(service.getDropdownList(tokenUtils.getUserIdFromRequest(request)));
        } catch (Exception e) {
            return commonUtils.generateErrorResponse(e);
        }
    }

    @Override
    @PutMapping( value = PAGEABLE_DATA_PATH, produces = EXTERNAL_MEDIA_TYPE)
    public BaseResponse getPageableListData(@Valid @RequestBody CommonPageableData pageableData, HttpServletRequest request) {
        try {
            return commonUtils.generateSuccessResponse(service.getPageableListData(pageableData, tokenUtils.getUserIdFromRequest(request)));
        } catch (Exception e) {
            return commonUtils.generateErrorResponse(e);
        }
    }
}
