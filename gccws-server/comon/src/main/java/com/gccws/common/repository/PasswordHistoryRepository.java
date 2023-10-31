package com.gccws.common.repository;


import java.util.List;

import com.gccws.common.entity.PasswordHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author    Md. Chabed Alam
 * @Since     August 1, 2022
 * @version   1.0.0
 */

@Repository
public interface PasswordHistoryRepository extends JpaRepository<PasswordHistory, Integer> {
	List<PasswordHistory> findByActive(boolean active);
	
	
	String findByMaxDateQuery = "SELECT *\r\n"
            + "FROM SYA_PASSWORD_HISTORY \r\n"
            + "WHERE APP_USER_ID = :id \r\n"
        	+ "AND ENTRY_DATE = (SELECT MAX(ENTRY_DATE) FROM SYA_PASSWORD_HISTORY WHERE APP_USER_ID = :id )";

	@Query(value = findByMaxDateQuery, nativeQuery = true)
    PasswordHistory getMaxEntryDateByUserId(
            @Param("id") Integer id
    );	

}
