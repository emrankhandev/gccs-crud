package com.gccws.common.repository;


import java.util.List;

import com.gccws.common.model.CommonDropdownModel;
import com.gccws.base.repository.BaseRepository;
import com.gccws.common.entity.UserRoleAssignMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author    Rima
 * @Since     September 1, 2022
 * @version   1.0.0
 */

@Repository
public interface UserRoleAssignMasterRepository extends BaseRepository<UserRoleAssignMaster> {
	
	String searchQuery = "select a.*\n"
			+ "from sya_user_role_assign_master a \n"
			+ "left outer join sya_app_user c on c.id = a.app_user_id\n"
			+ "where 1=1\n"
			+ "and concat(c.username, c.display_name) ilike  %:searchValue%\n";
	
	@Query(value = searchQuery, nativeQuery = true)
    Page<UserRoleAssignMaster> searchPageableList(
            @Param("searchValue") String searchValue,
            Pageable pageable
    );
	
	String dropdownQuery = "select a.id, '' as name\r\n"
			+ "from sya_user_role_assign_master a \r\n"
			+ "where 1=1\r\n"
			+ "and a.active = true\r\n"
			+ "order by a.id desc";

	@Query(value = dropdownQuery, nativeQuery = true)
	List<CommonDropdownModel> findDropdownModel();
	
}
