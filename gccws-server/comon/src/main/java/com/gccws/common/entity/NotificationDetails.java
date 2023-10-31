package com.gccws.common.entity;


import com.gccws.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;


/**
 * @Author    Md. Mizanur Rahman
 * @Since     July 31, 2023
 * @version   1.0.0
 */



@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "SYA_USER_NOTIFICATION_DETAILS")
public class NotificationDetails extends BaseEntity {

	
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(NotificationDetails.class);

	@OnDelete(action = OnDeleteAction.CASCADE)
	@OneToOne
	@JoinColumn(name = "MASTER_ID")
	private NotificationMaster master;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@OneToOne
	@JoinColumn(name = "APP_USER_ID", nullable = false)
	private AppUser appUser;

}
