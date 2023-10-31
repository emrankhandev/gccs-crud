package com.gccws.sysadmin.report.repository;

import com.gccws.base.repository.BaseRepository;
import com.gccws.common.model.CommonDropdownModel;
import com.gccws.sysadmin.report.entity.ParameterAssignMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author		Rima
 * @Since		January 10, 2023
 * @version		1.0.0
 */

@Repository
public interface ParameterAssignMasterRepository extends BaseRepository<ParameterAssignMaster> {

    List<ParameterAssignMaster> findByMenuItemId(int id);

    List<ParameterAssignMaster> findByMenuItemDevCode(int devCode);

    String searchQuery = "select a.*\n"
            + "from sya_parameter_assign_master a \n"
            + "left outer join sya_menu_item b on a.menu_item_id = b.id\n"
            + "where 1=1\n"
            + "and concat(b.name) ilike  %:searchValue%\n";

    @Query(value = searchQuery, nativeQuery = true)
    Page<ParameterAssignMaster> searchPageableList(
            @Param("searchValue") String searchValue,
            Pageable pageable
    );

    String dropdownQuery = "select a.id, a.name as name\r\n"
            + "from sya_menu_item a \r\n"
            + "where 1=1\r\n"
            + "and a.active = true\r\n"
            + "order by a.id desc";
    @Query(value = dropdownQuery, nativeQuery = true)

    List<CommonDropdownModel> findDropdownModel();

}
