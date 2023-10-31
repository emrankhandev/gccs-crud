package com.gccws.common.repository;


import com.gccws.common.entity.SetupMaster;
import com.gccws.base.repository.BaseRepository;
import com.gccws.common.model.CommonDropdownModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface SetupMasterRepository extends BaseRepository<SetupMaster> {

    String searchQuery = "select a.*\n"
            + "from common_setup_master a\n"
            + "left outer join common_setup_master b on a.parent_id = b.id\n"
            + "where 1=1\n"
            + "and concat(a.name, a.bangla_name, b.name) ilike %:searchValue%";

    @Query(value = searchQuery, nativeQuery = true)
    Page<SetupMaster> searchPageableList(
            @Param("searchValue") String searchValue,
            Pageable pageable
    );




    String searchPageableListByModuleId = "select a.*\n"
            + "from common_setup_master a\n"
            + "left outer join common_setup_master b on a.parent_id = b.id\n"
            + "where 1=1\n"
            + "and a.module_id = :moduleId\n"
            + "and concat(a.name, a.bangla_name, b.name) ilike %:searchValue%";

    @Query(value = searchPageableListByModuleId, nativeQuery = true)
    Page<SetupMaster> searchPageableListByModuleId(
            @Param("searchValue") String searchValue,
            @Param("moduleId") Integer intParam1,
            Pageable pageable
    );

    String dropdownQuery = "select a.id, a.name as name, a.parent_id as extra\r\n"
            + "from common_setup_master a \r\n"
            + "where 1=1\r\n"
            + "and a.active = true\r\n"
            + "order by a.id desc";

    @Query(value = dropdownQuery, nativeQuery = true)
    List<CommonDropdownModel> findDropdownModel();

    String dropdownQueryByModuleId = "select a.id, a.name as name, a.parent_id as extra\r\n"
            + "from common_setup_master a \r\n"
            + "where 1=1\r\n"
            + "and a.module_id = :moduleId\r\n"
            + "and a.active = true\r\n"
            + "order by a.id desc";

    @Query(value = dropdownQueryByModuleId, nativeQuery = true)
    List<CommonDropdownModel> findDropdownModelByModuleId(
            @Param("moduleId") Integer moduleId
    );

}
