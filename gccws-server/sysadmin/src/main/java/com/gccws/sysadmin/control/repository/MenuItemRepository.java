package com.gccws.sysadmin.control.repository;
import java.util.List;

import com.gccws.base.repository.BaseRepository;
import com.gccws.common.entity.MenuItem;
import com.gccws.common.model.CommonDropdownModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author    Rima
 * @Since     January 09, 2023
 * @version   1.0.0
 */

@Repository
public interface MenuItemRepository extends BaseRepository<MenuItem> {

	List<MenuItem>  findByMenuType(Integer menuType);

	MenuItem  findByDevCode(Integer devCode);

	String searchQuery = "select a.*\n"
			+ "from sya_menu_item a \n"
			+ "left outer join sya_menu_item b on a.parent_id = b.id\n"
			+ "where 1=1\n"
			+ "and concat(a.name, a.bangla_name, a.serial_no, a.url, a.menu_type_name, b.name) ilike  %:searchValue%\n";
	@Query(value = searchQuery, nativeQuery = true)
	Page<MenuItem> searchPageableList(
			@Param("searchValue") String searchValue,
			Pageable pageable
	);

	String dropdownQuery = "select a.id, a.name as name,\r\n"
			+"a.menu_type as extra\n"
			+ "from sya_menu_item a \r\n"
			+ "where 1=1\r\n"
			+ "and a.active = true\r\n"
			+ "order by a.id desc";

	@Query(value = dropdownQuery, nativeQuery = true)
	List<CommonDropdownModel> findDropdownModel();

	String dropdownByMenuTypeQuery = "select a.id, concat(a.name, ' (', a.menu_type_name, ')', ' (', b.name , ')' )  as name\n" +
			"from sya_menu_item a \n" +
			"left outer join sya_menu_item b on a.parent_id = b.id\n" +
			"where 1=1\n" +
			"and a.active = true \n" +
			"and a.menu_type in (:menuType)\n" +
			"order by a.menu_type_name";

	@Query(value = dropdownByMenuTypeQuery, nativeQuery = true)
	List<CommonDropdownModel> findDropdownModelByMenuType(
			@Param("menuType") List<Integer> menuType
	);

	String findPageByAppUserIdQuery = "select  a.id, a.active, a.entry_app_user_code, a.entry_date, a.entry_user, a.update_app_user_code, a.update_date, a.update_user,\n" +
			"a.name, a.bangla_name, a.icon, a.menu_type,\ta.menu_type_name, a.serial_no, a.dev_code, a.url, a.parent_id, a.report_upload_id,\n" +
			"b.is_insert, b.is_edit, b.is_delete, b.is_view, b.is_approve\n" +
			"from sya_menu_item a, sya_user_role_details b, sya_user_role_master c,\n" +
			"\t sya_user_role_assign_details d, sya_user_role_assign_master e\n" +
			"where 1=1\n" +
			"and a.id = b.menu_item_id\n" +
			"and b.master_id = c.id\n" +
			"and d.user_role_id = c.id\n" +
			"and d.master_id = e.id\n" +
			"and a.menu_type = 3\n" +
			"and e.app_user_id = :appUserId\n" +
			"order by parent_id, serial_no";
	@Query(value = findPageByAppUserIdQuery, nativeQuery = true)
	List<MenuItem> findPageByAppUserId(
			@Param("appUserId") Integer appUserId
	);

	String moduleDropdownQuery = "select a.id, a.name as name\n" +
			"from sya_menu_item a \n" +
			"where 1=1\n" +
			"and a.active = true\n" +
			"and a.parent_id is  null \n" +
			"order by a.id desc";

	@Query(value = moduleDropdownQuery, nativeQuery = true)
	List<CommonDropdownModel> findModuleDropdownModel();


	String reportModuleDropdownQuery = "select f.*\n" +
			"from sya_menu_item a, \n" +
			"\tsya_user_role_details b, sya_user_role_master c,\n" +
			"\tsya_user_role_assign_details d, sya_user_role_assign_master e,\n" +
			"\tsya_menu_item f\n" +
			"where 1=1\n" +
			"and b.menu_item_id = a.id\n" +
			"and c.id = b.master_id \n" +
			"and d.user_role_id = c.id\n" +
			"and e.id = d.master_id \n" +
			"and f.id = a.parent_id \n" +
			"and a.menu_type = 4\n" +
			"and e.app_user_id = :appUserId\n" +
			"group by f.id";
	@Query(value = reportModuleDropdownQuery, nativeQuery = true)
	List<MenuItem> findReportModuleByAppUser(
			@Param("appUserId") Integer appUserId
	);

	String reportByModuleIdDropdownQuery = "select c.*\n" +
			"from  sya_user_role_details a, sya_user_role_master b,\n" +
			"\tsya_menu_item c,\n" +
			"\tsya_user_role_assign_details d, sya_user_role_assign_master e\n" +
			"where 1=1\n" +
			"and b.id = a.master_id \n" +
			"and c.id = a.menu_item_id\n" +
			"and d.user_role_id = b.id\n" +
			"and e.id = d.master_id \n" +
			"and c.menu_type = 4\n" +
			"and e.app_user_id = :appUserId\n" +
			"and c.parent_id = :moduleId";
	@Query(value = reportByModuleIdDropdownQuery, nativeQuery = true)
	List<MenuItem> findReportByModuleIdAndAppUser(
			@Param("moduleId") Integer moduleId,
			@Param("appUserId") Integer appUserId
	);

}
