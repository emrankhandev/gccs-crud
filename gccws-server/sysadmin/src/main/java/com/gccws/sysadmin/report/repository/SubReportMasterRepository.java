package com.gccws.sysadmin.report.repository;

import java.util.List;

import com.gccws.base.repository.BaseRepository;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.sysadmin.report.entity.SubReportMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author		Rima
 * @Since		January 10, 2023
 * @version		1.0.0
 */
@Repository
public interface SubReportMasterRepository extends BaseRepository<SubReportMaster> {

	String searchQuery = "select a.*\n"
			+ "from sya_sub_report_master a \n"
			+ "left outer join sya_menu_item b on a.menu_item_id = b.id\n"
			+ "left outer join sya_report_upload c on a.report_upload_id = c.id\n"
			+ "where 1=1\n"
			+ "and concat(b.name, c.file_name) ilike  %:searchValue%\n";

	@Query(value = searchQuery, nativeQuery = true)
	Page<SubReportMaster> searchPageableList(
			@Param("searchValue") String searchValue,
			Pageable pageable
	);

	List<SubReportMaster> findByMenuItemIdAndActive(int menuItemId, boolean active);

	List<SubReportMaster> findByMenuItemDevCodeAndActive(int devCode, boolean active);

	String dropdownQuery = "select a.id, '' as name\r\n"
			+ "from sya_sub_report_master a \r\n"
			+ "where 1=1\r\n"
			+ "and a.active = true\r\n"
			+ "order by a.id desc";

	@Query(value = dropdownQuery, nativeQuery = true)
	List<CommonDropdownModel> findDropdownModel();


}
