package com.gccws.common.repository;

import com.gccws.common.model.CommonDropdownModel;
import com.gccws.base.repository.BaseRepository;
import com.gccws.common.entity.FileValidator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @Author    Md. Chabed Alam
 * @Since     September 26, 2022
 * @version   1.0.0
 */

@Repository
public interface FileValidatorRepository extends BaseRepository<FileValidator> {

	FileValidator findByDevCode(Integer devCode);
	
	String searchQuery = "select a.*\n"
			+ "from sya_file_validator a\n"
			+ "where 1=1\n"
			+ "and concat(a.name) ilike %:searchValue%";

    @Query(value = searchQuery, nativeQuery = true)
	Page<FileValidator> searchPageableList(
            @Param("searchValue") String searchValue,
            Pageable pageable
    );

    String dropdownQuery = "select a.id, a.name as name\r\n"
			+ "from sya_file_validator a \r\n"
			+ "where 1=1\r\n"
			+ "and a.active = true\r\n"
			+ "order by a.id desc";

	@Query(value = dropdownQuery, nativeQuery = true)
	List<CommonDropdownModel> findDropdownModel();
	
	
	
}
