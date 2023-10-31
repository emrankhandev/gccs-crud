package com.gccws.common.dto;

import com.gccws.base.dto.BaseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

/**
 * @author  Md. Mizanur Rahman
 * @Email   kmmizanurrahmanjp@gmail.com
 * @since   March 14,2023
 * @version 1.0
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageHistoryDto extends BaseDto {
    private Integer moduleId;
    private String moduleName;
    private Integer transactionId;
    private String transactionTable;
    private String transactionType;
    private String  message;
    private String  attachment;
    private String  attachmentLocation;
    private Boolean isRead;
    private Date readDate;
    private Boolean isClose;
    private Boolean isAction;
    private String link;
    private Integer authorityId;
    private String authorityName;
    private Integer receivedUserId;
    private String receivedUserName;
    private Date publishDate;
    private String description;
}
