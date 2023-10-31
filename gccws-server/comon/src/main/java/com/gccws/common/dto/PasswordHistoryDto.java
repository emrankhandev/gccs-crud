package com.gccws.common.dto;

import com.gccws.base.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author  Md. Mizanur Rahman
 * @Email   kmmizanurrahmanjp@gmail.com
 * @since   March 14,2023
 * @version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordHistoryDto extends BaseDto {

    private Integer entryUser;
    private Integer appUserId;
    private String appUserName;

    private String password;

}
