/**
 *
 */
package com.gccws.common.repository;

import java.util.List;
import java.util.Optional;

import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.entity.AppUser;
import com.gccws.base.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @Author    Md. Chabed Alam
 * @Since     August 1, 2022
 * @version   1.0.0
 */

public interface AppUserRepository extends BaseRepository<AppUser> {
	Optional<AppUser> findByUsernameIgnoreCase(String username);

	AppUser findByEmail(String email);
	AppUser findByUsername(String email);
//	AppUser findByCustomerInfoId(Integer customerId);
//	AppUser findByEmpPersonalInfoId(Integer empId);

	AppUser findByUsernameAndActive(String username, Boolean active);

	List<AppUser> findByActive(boolean active);

	String searchQuery = "select a.*\n"
			+ "from SYA_APP_USER a \n"
			+ "left outer join SYA_PASSWORD_POLICY b on a.PASSWORD_POLICY_ID = b.id\n"
			+ "where 1=1\n"
			+ "and concat(a.username, b.name, a.mobile, a.email) like  %:searchValue%\n";

	@Query(value = searchQuery, nativeQuery = true)
	Page<AppUser> searchPageableList(
			@Param("searchValue") String searchValue,
			Pageable pageable
	);

//	String searchQueryForMarine = "select a.*\n" +
//			"from SYA_APP_USER a \n" +
//			"left outer join bill_customer_info b on a.customer_info_id = b.id\n" +
//			"where 1=1\n" +
//			"and b.agent_type_id = :agentTypeId\n" +
//			"and concat(a.username, b.name, a.mobile, a.email) like  %:searchValue%";
//
//	@Query(value = searchQueryForMarine, nativeQuery = true)
//	Page<AppUser> searchPageableListForAgentProfile(
//			@Param("searchValue") String searchValue,
//			@Param("agentTypeId") Integer agentTypeId,
//			Pageable pageable
//	);

	String searchQueryByType = "select a.* \n" +
			"from SYA_APP_USER a , sya_password_policy b\n" +
			"where 1 =1 \n" +
			"and b.id = a.password_policy_id \n" +
			"and a.user_type_id = :userType\n" +
			"and concat(a.username, b.name) ilike %:searchValue% ";

	@Query(value = searchQueryByType, nativeQuery = true)
	Page<AppUser> searchPageableListByUserType(
			@Param("searchValue") String searchValue,
			@Param("userType") Integer userType,
			Pageable pageable
	);

	String dropdownQuery = "select a.id, concat('(' ,a.display_name, ') ' , a.username) as name\r\n"
			+ "from sya_app_user a \r\n"
			+ "where 1=1\r\n"
			+ "and a.active = true\r\n"
			+ "order by a.id desc";

	@Query(value = dropdownQuery, nativeQuery = true)
	List<CommonDropdownModel> findDropdownModel();

	String userCheckQuery = "select a.id as id\n" +
			"from sya_app_user a\n" +
			"where 1=1\n" +
			"and a.username = :emailId\n" +
			"union \n" +
			"select a.id as id \n" +
			"from bill_customer_info a\n" +
			"where 1=1\n" +
			"and a.email = :emailId";

	@Query(value = userCheckQuery, nativeQuery = true)
	List<Integer> checkByEmail(
			@Param("emailId") String emailId
	);


}
