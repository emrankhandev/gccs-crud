package com.gccws.common.controller;

import com.gccws.common.entity.AuditLogging;
import com.gccws.common.service.AuditLoggingService;
import com.gccws.common.utils.CommonUtils;
import com.gccws.base.model.BaseResponse;
import com.gccws.base.utils.BaseConstants;
import com.gccws.base.utils.UserTokenRequestUtils;
import com.gccws.common.utils.CommonConstants;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping(CommonConstants.SYSTEM_ADMIN_END_POINT +"audit-Logging")
public class AuditLoggingController {


    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(AuditLoggingController.class);

    /*utils*/
    private final CommonUtils commonUtils;

    /*service*/
    private final AuditLoggingService service;

    private final UserTokenRequestUtils tokenUtils;

    @PostMapping("/save-audit-Logging")
    public BaseResponse saveAuditLogging(@Valid @RequestBody AuditLogging body, HttpServletRequest request) {
        try {
            return commonUtils.generateSuccessResponse(service.saveAuditLogging(tokenUtils.getUserIdFromRequest(request), body.getTableName(), body.getCurrentData()), BaseConstants.SAVE_MESSAGE, BaseConstants.SAVE_MESSAGE_BN);
        } catch (Exception e) {
            e.printStackTrace();
            return commonUtils.generateErrorResponse(e);
        }
    }

    @PostMapping("/update-audit-Logging")
    public BaseResponse updateAuditLogging(@Valid @RequestBody AuditLogging body, HttpServletRequest request){
        try {
            return commonUtils.generateSuccessResponse(service.updateAuditLogging(tokenUtils.getUserIdFromRequest(request), body.getTableName(), body.getCurrentData(), body.getOldData()), BaseConstants.UPDATE_MESSAGE, BaseConstants.UPDATE_MESSAGE_BN);
        } catch (Exception e) {
            e.printStackTrace();
            return commonUtils.generateErrorResponse(e);
        }
    }

    @PostMapping("/delete-audit-Logging")
    public BaseResponse deleteAuditLogging(@Valid @RequestBody AuditLogging body, HttpServletRequest request) {
        try {
            return commonUtils.generateSuccessResponse(service.deleteAuditLogging(tokenUtils.getUserIdFromRequest(request), body.getTableName(), body.getCurrentData()), BaseConstants.DELETE_MESSAGE, BaseConstants.DELETE_MESSAGE_BN);
        } catch (Exception e) {
            e.printStackTrace();
            return commonUtils.generateErrorResponse(e);
        }
    }

    @PostMapping("/getMethod-audit-Logging")
    public BaseResponse getMethodAuditLogging(@Valid @RequestBody AuditLogging body, HttpServletRequest request) {
        try {
            return commonUtils.generateSuccessResponse(service.getMethodAuditLogging(tokenUtils.getUserIdFromRequest(request), body.getTableName(), body.getGetMethod()));
        } catch (Exception e) {
            e.printStackTrace();
            return commonUtils.generateErrorResponse(e);
        }
    }

    @PostMapping("/sign-in-audit-Logging")
    public BaseResponse SignInAuditLogging(@Valid @RequestBody AuditLogging body, HttpServletRequest request) {
        try {
            return commonUtils.generateSuccessResponse(service.SignInAuditLogging(tokenUtils.getUserIdFromRequest(request), body.getTableName()));
        } catch (Exception e) {
            e.printStackTrace();
            return commonUtils.generateErrorResponse(e);
        }
    }
}
