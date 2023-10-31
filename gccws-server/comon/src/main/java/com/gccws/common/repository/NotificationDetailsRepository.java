package com.gccws.common.repository;


import com.gccws.base.repository.BaseRepository;
import com.gccws.common.entity.NotificationDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author    Md. Mizanur Rahman
 * @Since     July 31, 2023
 * @version   1.0.0
 */

@Repository
public interface NotificationDetailsRepository extends BaseRepository<NotificationDetails> {
	 List<NotificationDetails> findByMasterId(int id);

	List<NotificationDetails> findByMasterNotificationTypeId(int id);
	 List<NotificationDetails> findByAppUserId(Integer appUserId);
}
