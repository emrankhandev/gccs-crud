package com.gccws.sysadmin.control.controller.noauth;

import com.gccws.base.model.BaseResponse;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.control.service.PasswordPolicyService;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.gccws.base.utils.BaseConstants.EXTERNAL_MEDIA_TYPE;
import static com.gccws.common.utils.CommonConstants.SYSTEM_PUBLIC_ADMIN_END_POINT;

/**
 * @Author    Md. Tawhidul Islam
 * @Since     October 11, 2022
 * @version   1.0.0
 */

@AllArgsConstructor
@RestController
@RequestMapping(SYSTEM_PUBLIC_ADMIN_END_POINT + "password-policy")
public class PasswordPolicyPublicController {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(PasswordPolicyPublicController.class);

    private final PasswordPolicyService service;

    /* utils */
    private final CommonUtils commonUtils;


    @GetMapping( value = "get-agent-policy", produces = EXTERNAL_MEDIA_TYPE)
    public BaseResponse getAgentPolicy() {
        try {
            return commonUtils.generateSuccessResponse(service.getAgentPolicy());
        } catch (Exception e) {
            return commonUtils.generateErrorResponse(e);
        }
    }

}
