package com.gccws.common.repository;


import java.util.List;

import com.gccws.common.model.CommonDropdownModel;
import com.gccws.base.repository.BaseRepository;
import com.gccws.common.entity.UserRoleMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author    Md. Chabed Alam
 * @Since     August 29, 2022
 * @version   1.0.0
 */

@Repository
public interface UserRoleMasterRepository extends BaseRepository<UserRoleMaster> {

	UserRoleMaster findByDevCode(Integer devCode);
	
	String searchQuery = "select a.*\n"
			+ "from sya_user_role_master a \n"
			+ "where 1=1\n"
			+ "and concat(a.name, a.bangla_name) ilike  %:searchValue%\n";
	
	@Query(value = searchQuery, nativeQuery = true)
    Page<UserRoleMaster> searchPageableList(
            @Param("searchValue") String searchValue,
            Pageable pageable
    );
	
	String dropdownQuery = "select a.id, a.name as name\r\n"
			+ "from sya_user_role_master a \r\n"
			+ "where 1=1\r\n"
			+ "and a.active = true\r\n"
			+ "order by a.id desc";

	@Query(value = dropdownQuery, nativeQuery = true)
	List<CommonDropdownModel> findDropdownModel();


	String roleByUserQuery = "select a.*\n" +
			"from sya_user_role_master a, \n" +
			"\tsya_user_role_assign_details b, sya_user_role_assign_master c\n" +
			"where 1=1\n" +
			"and b.user_role_id = a.id\n" +
			"and c.id  = b.master_id \n" +
			"and c.app_user_id = :appUserId\n" +
			"group by a.id";

	@Query(value = roleByUserQuery, nativeQuery = true)
	List<UserRoleMaster> findUserRoleQuery(
			@Param("appUserId") Integer appUserId
	);

}
