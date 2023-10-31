package com.gccws.sysadmin.control.controller.noauth;

import com.gccws.base.model.BaseResponse;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.control.service.AppUserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import static com.gccws.base.utils.BaseConstants.*;
import static com.gccws.common.utils.CommonConstants.SYSTEM_PUBLIC_ADMIN_END_POINT;


/**
 * @Author    Rima
 * @Since    July 16, 2023
 * @version   1.0.0
 */
@AllArgsConstructor
@RestController
@RequestMapping(SYSTEM_PUBLIC_ADMIN_END_POINT + "mail-sender")
public class MailSenderPublicController {
    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(MailSenderPublicController.class);
    private final AppUserService service;

    private final CommonUtils commonUtils;

    @GetMapping( value = ("/send-mail-for-otp/{email}/{userName}"), produces = EXTERNAL_MEDIA_TYPE)
    public BaseResponse sendEmailForOTP(@PathVariable("email") String emailAddress, @PathVariable("userName") String userName){
        try {
            return commonUtils.generateSuccessResponse(service.sendEmailForOTP(emailAddress,userName), SAVE_MESSAGE, SAVE_MESSAGE_BN);
        } catch (Exception e) {
            return commonUtils.generateErrorResponse(e);
        }
    }
}
