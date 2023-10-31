package com.gccws.sysadmin.control.model;

import lombok.Data;

/**
 * @Author    Md. Tawhidul Islam
 * @Since     March 13, 2022
 * @version   1.0.0
 */
@Data
public class ChangePasswordModel {
    private String username;
    private String password;
}
