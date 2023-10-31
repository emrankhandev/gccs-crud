package com.gccws.common.repository;

import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.entity.ApprovalTeamMaster;
import com.gccws.base.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author    Rima
 * @Since     February 22, 2023
 * @version   1.0.0
 */

@Repository
public interface ApprovalTeamMasterRepository extends BaseRepository<ApprovalTeamMaster> {

	ApprovalTeamMaster findByDevCode(Integer devCode);

	String searchQuery = "select a.*\n"
			+ "from sya_approval_team_master a \n"
//			+ "left outer join sya_app_user c on c.id = a.app_user_id\n"
			+ "where 1=1\n"
			+ "and concat(a.name) ilike  %:searchValue%\n";
	
	@Query(value = searchQuery, nativeQuery = true)
    Page<ApprovalTeamMaster> searchPageableList(
            @Param("searchValue") String searchValue,
            Pageable pageable
    );
	
	String dropdownQuery = "select a.id, a.name as name\r\n"
			+ "from sya_approval_team_master a \r\n"
			+ "where 1=1\r\n"
			+ "and a.active = true\r\n"
			+ "order by a.id desc";

	@Query(value = dropdownQuery, nativeQuery = true)
	List<CommonDropdownModel> findDropdownModel();

	String nextTeamInfo = "select a.id, a.\"name\" as name, b.notify_app_user_id as extra\n" +
			"from sya_approval_team_master a, sya_approval_configuration b\n" +
			"where 1=1\n" +
			"and b.to_team_id = a.id\n" +
			"and b.department_id = :departmentId\n" +
			"and b.transaction_type_id = :transactionTypeId\n" +
			"and b.serial_no >= (select y.serial_no\n" +
			"\tfrom sya_approval_team_master x, sya_approval_configuration y\n" +
			"\twhere 1=1\n" +
			"\tand y.from_team_id = x.id\n" +
			"\tand y.department_id = :departmentId\n" +
			"\tand y.transaction_type_id = :transactionTypeId\n" +
			"\tand x.id = :currentTeamId)";

	@Query(value = nextTeamInfo, nativeQuery = true)
	List<CommonDropdownModel> findNextTeamByDepartmentAndCurrentTeamId(
			@Param("departmentId") Integer departmentId,
			@Param("transactionTypeId") Integer transactionTypeId,
			@Param("currentTeamId") Integer currentTeamId
	);

	String previousTeamInfo =  "select a.id, a.\"name\" as name, b.notify_app_user_id as extra \n" +
			"from sya_approval_team_master a, sya_approval_configuration b\n" +
			"where 1=1\n" +
			"and b.from_team_id  = a.id\n" +
			"and b.department_id = :departmentId\n" +
			"and b.transaction_type_id = :transactionTypeId\n" +
			"and b.serial_no <= (select y.serial_no\n" +
			"\tfrom sya_approval_team_master x, sya_approval_configuration y\n" +
			"\twhere 1=1\n" +
			"\tand y.to_team_id  = x.id\n" +
			"\tand y.department_id = :departmentId\n" +
			"\tand y.transaction_type_id = :transactionTypeId\n" +
			"\tand x.id = :currentTeamId)\n" +
			"order by b.serial_no desc";

	@Query(value = previousTeamInfo, nativeQuery = true)
	List<CommonDropdownModel> findPreviousTeamByDepartmentAndCurrentTeamId(
			@Param("departmentId") Integer departmentId,
			@Param("transactionTypeId") Integer transactionTypeId,
			@Param("currentTeamId") Integer currentTeamId
	);


	String TeamByDepartmentIdQuery = "select a.*\n" +
			"from sya_approval_team_master a, sya_approval_configuration b\n" +
			"where 1=1\n" +
			"and a.id in (b.from_team_id, b.to_team_id)\n" +
			"and b.department_id = :departmentId\n" +
			"group by a.id;";

	@Query(value = TeamByDepartmentIdQuery, nativeQuery = true)
	List<ApprovalTeamMaster> findTeamByDepartmentId(
			@Param("departmentId") Integer departmentId
	);

}
