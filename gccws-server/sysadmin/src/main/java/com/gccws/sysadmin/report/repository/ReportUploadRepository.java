package com.gccws.sysadmin.report.repository;

import java.util.List;

import com.gccws.base.repository.BaseRepository;
import com.gccws.common.entity.ReportUpload;
import com.gccws.common.model.CommonDropdownModel;
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
public interface ReportUploadRepository extends BaseRepository<ReportUpload> {

	String searchQuery = "select a.*\n"
			+ "from sya_report_upload a\n"
			+ "where 1=1\n"
			+ "and concat(a.code, a.file_name, a.remarks) ilike %:searchValue%";

	@Query(value = searchQuery, nativeQuery = true)
	Page<ReportUpload> searchPageableList(
			@Param("searchValue") String searchValue,
			Pageable pageable
	);

	String findSubreportQuery = "SELECT report.*\r\n"
			+ "FROM SYA_REPORT_UPLOAD report\r\n"
			+ "WHERE report.IS_SUBREPORT = true \r\n"
			+ "and report.active = true \r\n"
			+ "ORDER BY report.CODE ASC";
	@Query(value = findSubreportQuery, nativeQuery = true)
	List<ReportUpload> findAllActiveSubreport();


	String findMasterReportQuery = "SELECT report.*\r\n"
			+ "FROM SYA_REPORT_UPLOAD report\r\n"
			+ "WHERE report.IS_SUBREPORT = false \r\n"
			+ "and report.active = true \r\n"
			+ "ORDER BY report.CODE ASC";
	@Query(value = findMasterReportQuery, nativeQuery = true)
	List<ReportUpload> findAllActiveMasterReport();

	String dropdownQuery = "select a.id, a.file_name as name\r\n"
			+ "from sya_report_upload a \r\n"
			+ "where 1=1\r\n"
			+ "and a.active = true\r\n"
			+ "order by a.id desc";

	@Query(value = dropdownQuery, nativeQuery = true)
	List<CommonDropdownModel> findDropdownModel();

	String dropdownQueryForSubreport = "select a.id, a.file_name as name\r\n"
			+ "from sya_report_upload a \r\n"
			+ "where 1=1\r\n"
			+ "and a.is_subreport = true\r\n"
			+ "and a.active = true\r\n"
			+ "order by a.id desc";

	@Query(value = dropdownQueryForSubreport, nativeQuery = true)
	List<CommonDropdownModel> findSubReportDropdownModel();

	String dropdownQueryForMasterReport = "select a.id, a.file_name as name\r\n"
			+ "from sya_report_upload a \r\n"
			+ "where 1=1\r\n"
			+ "and a.is_subreport = false\r\n"
			+ "and a.active = true\r\n"
			+ "order by a.id desc";

	@Query(value = dropdownQueryForMasterReport, nativeQuery = true)
	List<CommonDropdownModel> findMasterReportDropdownModel();

}
