/**
 * 
 */
package com.gccws.common.repository;
import java.util.List;

import com.gccws.common.entity.SmsFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @Author    Md. Chabed Alam
 * @Since     September 28, 2022
 * @version   1.0.0
 */

public interface SMSFactoryRepository extends JpaRepository<SmsFactory, Integer>{
	
	String unSendQuery = "select a.*\n"
			+ "from COMMON_SMS_FACTORY a\n"
			+ "where 1=1\n"
			+ "and message_id is null\n"
			+ "order by id desc\n";
	
	@Query(value = unSendQuery, nativeQuery = true)
    List<SmsFactory> findUnSendMessage();
	
}
