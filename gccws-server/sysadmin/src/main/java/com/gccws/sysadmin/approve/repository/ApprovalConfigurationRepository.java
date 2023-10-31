package com.gccws.sysadmin.approve.repository;

import com.gccws.base.repository.BaseRepository;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.sysadmin.approve.entity.ApprovalConfiguration;
import com.gccws.sysadmin.approve.model.ApproveUserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * @Author    Rima
 * @Since     February 23, 20223
 * @version   1.0.0
 */
@Repository
public interface ApprovalConfigurationRepository extends BaseRepository<ApprovalConfiguration> {

	String searchQuery = "select a.*\n" +
			"from sya_approval_configuration a\n" +
			"left outer join sya_approval_team_master b on b.id = a.from_team_id\n" +
			"left outer join sya_approval_team_master e on e.id = a.to_team_id  \n" +
			"left outer join sya_app_user  c on c.id = a.notify_app_user_id \n" +
			"left outer join sya_menu_item d on d.id = a.module_id\n" +
			"left outer join common_setup_details f on f.id = a.department_id\n" +
			"where 1=1\n" +
			"and concat(f.\"name\", b.name, e.name, c.display_name, d.name, a.serial_no, a.transaction_type_name) ilike %:searchValue%";
	
	@Query(value = searchQuery, nativeQuery = true)
	Page<ApprovalConfiguration> searchPageableList(
            @Param("searchValue") String searchValue,
            Pageable pageable
    );
	
	String dropdownQuery = "select a.id, '' as name\r\n"
			+ "from sya_approval_configuration a \r\n"
			+ "where 1=1\r\n"
			+ "and a.active = true\r\n"
			+ "order by a.id desc";

	@Query(value = dropdownQuery, nativeQuery = true)
	List<CommonDropdownModel> findDropdownModel();
	
	String findByModuleAndAppUserQuery = "select d.id, a.from_team_id as currentTeamId, a.serial_no as serialNo, a.to_team_id as toTeamId\n" +
			"from sya_approval_configuration a, sya_approval_team_master b,\n" +
			"sya_approval_team_details c, sya_app_user d\n" +
			"where 1=1\n" +
			"and a.department_id = :departmentId\n" +
			"and b.id = a.from_team_id\n" +
			"and c.master_id = b.id\n" +
			"and c.app_user_id = d.id\n" +
			"and d.id = :appUserId\n" +
			"and a.transaction_type_id = :transactionTypeId\n" +
			"limit 1";
	
	@Query(value = findByModuleAndAppUserQuery, nativeQuery = true)
    ApproveUserModel findSubmitUserByDepartmentAndAppUserIdAndTransactionTypeId(
            @Param("departmentId") Integer departmentId,
            @Param("appUserId") Integer appUserId,
            @Param("transactionTypeId") Integer transactionTypeId
    );

	String findApproveUserByModuleAndAppUserQuery = "select d.id, a.to_team_id as currentTeamId, a.serial_no as serialNo, \n" +
			"\t(select x.to_team_id \n" +
			"\tfrom sya_approval_configuration x\n" +
			"\twhere x.department_id = :departmentId\n" +
			"\tand x.transaction_type_id = :transactionTypeId\n" +
			"\tand x.from_team_id = a.to_team_id) as toTeamId\n" +
			"from sya_approval_configuration a, sya_approval_team_master b,\n" +
			"\tsya_approval_team_details c, sya_app_user d\n" +
			"where 1=1\n" +
			"and a.department_id = :departmentId\n" +
			"and b.id = a.to_team_id\n" +
			"and c.master_id = b.id\n" +
			"and c.app_user_id = d.id\n" +
			"and d.id = :appUserId\n" +
			"and a.transaction_type_id = :transactionTypeId\n" +
			"order by a.serial_no desc";

	@Query(value = findApproveUserByModuleAndAppUserQuery, nativeQuery = true)
	ApproveUserModel findApproveAndForwardUserByDepartmentAndAppUserId(
			@Param("departmentId") Integer departmentId,
			@Param("appUserId") Integer appUserId,
			@Param("transactionTypeId") Integer transactionTypeId
	);

	String findByToTeamAndDepartmentIdQuery = "select a.* \n" +
			"from sya_approval_configuration a\n" +
			"where 1=1\n" +
			"and a.to_team_id  = :toTeamId\n" +
			"and a.department_id = :departmentId\n" +
			"and a.transaction_type_id = :transactionTypeId\n" +
			"limit 1";

	@Query(value = findByToTeamAndDepartmentIdQuery, nativeQuery = true)
	ApprovalConfiguration findToTeamAndDepartmentId(
			@Param("toTeamId") Integer toTeamId,
			@Param("departmentId") Integer departmentId,
			@Param("transactionTypeId") Integer transactionTypeId
	);

	String findByDepartmentTypeNotNotifyUserQuery = "select a.*\n" +
			"from sya_approval_configuration a\n" +
			"where 1=1\n" +
			"and a.department_id = :departmentId\n" +
			"and a.transaction_type_id = :transactionTypeId\n" +
			"and a.notify_app_user_id not in (:notifyAppUserId)";

	@Query(value = findByDepartmentTypeNotNotifyUserQuery, nativeQuery = true)
	List<ApprovalConfiguration> findByDepartmentIdTypeIdAndNotNotifyUser(
			@Param("departmentId") Integer departmentId,
			@Param("transactionTypeId") Integer transactionTypeId,
			@Param("notifyAppUserId") List<Integer> notifyAppUserId
	);
	
}
