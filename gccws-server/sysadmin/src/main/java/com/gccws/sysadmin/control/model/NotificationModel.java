package com.gccws.sysadmin.control.model;

import com.gccws.sysadmin.control.dto.NotificationDetailsDto;
import com.gccws.sysadmin.control.dto.NotificationMasterDto;
import lombok.Data;

import java.util.List;

/**
 * @Author    Md. Mizanur Rahman
 * @Since     July 31, 2023
 * @version   1.0.0
 */

@Data
public class NotificationModel {
	private NotificationMasterDto master;
    private List<NotificationDetailsDto> detailsList;
}
