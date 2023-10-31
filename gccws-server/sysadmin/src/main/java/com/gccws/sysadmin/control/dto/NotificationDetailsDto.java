 package com.gccws.sysadmin.control.dto;

 import com.gccws.base.dto.BaseDto;
 import lombok.AllArgsConstructor;
 import lombok.Data;
 import lombok.EqualsAndHashCode;
 import lombok.NoArgsConstructor;

 /**
  * @Author    Md. Mizanur Rahman
  * @Since     July 31, 2023
  * @version   1.0.0
  */

 @Data
 @EqualsAndHashCode(callSuper = true)
 @AllArgsConstructor
 @NoArgsConstructor
 public class NotificationDetailsDto extends BaseDto {

     private static final long serialVersionUID = 1L;

     private Integer appUserId;
     private String appUserName;

 }
