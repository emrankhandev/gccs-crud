package com.gccws.sysadmin.control.controller.noauth;

import com.gccws.base.model.BaseResponse;
import com.gccws.common.utils.CommonUtils;
import com.gccws.sysadmin.control.dto.AppUserDto;
import com.gccws.sysadmin.control.model.ChangePasswordModel;
import com.gccws.sysadmin.control.service.AppUserService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.gccws.base.utils.BaseConstants.*;
import static com.gccws.common.utils.CommonConstants.SYSTEM_PUBLIC_ADMIN_END_POINT;

/**
 * @Author    Md. Tawhidul Islam
 * @Since     October 11, 2022
 * @version   1.0.0
 */

@AllArgsConstructor
@RestController
@RequestMapping(SYSTEM_PUBLIC_ADMIN_END_POINT + "app-user")
public class AppUserPublicController {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(AppUserPublicController.class);
    private final AppUserService service;
    /* utils */
    private final CommonUtils commonUtils;

    @PostMapping
    public BaseResponse save(@Valid @RequestBody AppUserDto body) {
        try {
            return commonUtils.generateSuccessResponse(service.signUp(body), SAVE_MESSAGE, SAVE_MESSAGE_BN);
        } catch (Exception e) {
            e.printStackTrace();
            return commonUtils.generateErrorResponse(e);
        }
    }


    @GetMapping( value = ("/get-user-by-name/{username}"), produces = EXTERNAL_MEDIA_TYPE)
    public BaseResponse getByUserName(@PathVariable("username") String username) {
        try {
            return commonUtils.generateSuccessResponse(service.getByUserName(username));
        } catch (Exception e) {
            return commonUtils.generateErrorResponse(e);
        }
    }

    @PostMapping( value = ("/change-password"), produces = EXTERNAL_MEDIA_TYPE)
    public BaseResponse changePassword(@Valid @RequestBody ChangePasswordModel body) {
        try {
            return commonUtils.generateSuccessResponse(service.changePassword(body));
        } catch (Exception e) {
            return commonUtils.generateErrorResponse(e);
        }
    }

    @GetMapping( value = ("/get-user-by-email/{email}"), produces = EXTERNAL_MEDIA_TYPE)
    public BaseResponse getByEmail(@PathVariable("email") String email) {
        try {
            return commonUtils.generateSuccessResponse(service.getByEmail(email));
        } catch (Exception e) {
            e.printStackTrace();
            return commonUtils.generateErrorResponse(e);
        }
    }

}
