package com.gccws.common.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author    Emran Khan
 * @Since     November 11, 2022
 * @version   1.0.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailFactoryDto {
    private Integer entryUser;
    private Date entryDate;
    private Integer transactionId;
    private String transactionTable;
    private String recipient;
    private String subject;
    private String msgBody;
    private String cc;
    private String bcc;

}
