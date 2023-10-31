package com.gccws.sysadmin.report.repository;

import java.util.List;

import com.gccws.base.repository.BaseRepository;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.sysadmin.report.entity.ParameterMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @Author		Md. Mizanur Rahman
 * @Since		January 10, 2023
 * @version		1.0.0
 */
@Repository
public interface ParameterMasterRepository extends BaseRepository<ParameterMaster> {
	String searchQuery = "select a.*\n"
			+ "from sya_parameter_master a \n"
			+ "where 1=1\n"
			+ "and concat(a.title, a.name, a.bangla_name, a.sql, a.data_type) ilike  %:searchValue%\n";

	@Query(value = searchQuery, nativeQuery = true)
	Page<ParameterMaster> searchPageableList(
			@Param("searchValue") String searchValue,
			Pageable pageable
	);

	@Query(value = "SELECT max(id) FROM ParameterMaster")
	int getMaxId();

	String dropdownQuery = "select a.id, a.title || ' (' || a.name || ')' as name\r\n"
			+ "from sya_parameter_master a \r\n"
			+ "where 1=1\r\n"
			+ "and a.active = true\r\n"
			+ "order by a.id desc";

	@Query(value = dropdownQuery, nativeQuery = true)
	List<CommonDropdownModel> findDropdownModel();

}
