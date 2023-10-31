package com.gccws.common.entity;

import com.gccws.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * @Author    Md. Mizanur Rahman
 * @Since     July 31, 2023
 * @version   1.0.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "SYA_USER_NOTIFICATION_MASTER")
public class NotificationMaster extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Column(name = "NOTIFICATION_TYPE_ID", unique = true, nullable = false)
    private Integer notificationTypeId;

    @Column(name = "NOTIFICATION_TYPE_NAME", unique = true, nullable = false)
    private String notificationTypeName;

}
