package com.gccws.common.repository;


import com.gccws.base.repository.BaseRepository;
import com.gccws.common.entity.NotificationMaster;
import com.gccws.common.model.CommonDropdownModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author    Md. Mizanur Rahman
 * @Since     July 31, 2023
 * @version   1.0.0
 */

@Repository
public interface NotificationMasterRepository extends BaseRepository<NotificationMaster> {
	
	String searchQuery = "select a.*\n"
			+ "from SYA_USER_NOTIFICATION_MASTER a \n"
			+ "where 1=1\n"
			+ "and concat(a.NOTIFICATION_TYPE_NAME) ilike  %:searchValue%\n";
	
	@Query(value = searchQuery, nativeQuery = true)
    Page<NotificationMaster> searchPageableList(
            @Param("searchValue") String searchValue,
            Pageable pageable
    );
	
	String dropdownQuery = "select a.id, '' as name\r\n"
			+ "from SYA_USER_NOTIFICATION_MASTER a \r\n"
			+ "where 1=1\r\n"
			+ "and a.active = true\r\n"
			+ "order by a.id desc";

	@Query(value = dropdownQuery, nativeQuery = true)
	List<CommonDropdownModel> findDropdownModel();
	
}
