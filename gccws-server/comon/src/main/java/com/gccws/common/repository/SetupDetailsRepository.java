package com.gccws.common.repository;


import com.gccws.common.entity.SetupDetails;
import com.gccws.base.repository.BaseRepository;
import com.gccws.common.model.CommonDropdownModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @Author    Emran Khan
 * @Since     October 12, 2022
 * @version   1.0.0
 */


public interface SetupDetailsRepository extends BaseRepository<SetupDetails> {

    String searchQuery = "select a.*\n" +
            "from common_setup_details a  \n" +
            "left outer join common_setup_details b on a.parent_id = b.id\n" +
            "left outer join common_setup_master c on c.id = a.master_id\n" +
            "where 1=1\n" +
            "and concat(a.name, a.bangla_name, b.name, c.name) ilike  %:searchValue%";

    @Query(value = searchQuery, nativeQuery = true)
    Page<SetupDetails> searchPageableList(
            @Param("searchValue") String searchValue,
            Pageable pageable
    );

    String searchPageableListByModuleId = "select a.*\n"
            + "from common_setup_details a  \n"
            + "left outer join common_setup_details b on a.parent_id = b.id\n"
            + "left outer join common_setup_master c on c.id = a.master_id\n"
            + "where 1=1\n"
            + "and a.module_id = :moduleId\n"
            + "and concat(a.name, a.bangla_name, b.name, c.name) ilike  %:searchValue%\n";

    @Query(value = searchPageableListByModuleId, nativeQuery = true)
    Page<SetupDetails> searchPageableListByModuleId(
            @Param("searchValue") String searchValue,
            @Param("moduleId") Integer intParam1,
            Pageable pageable
    );


    String dropdownQuery = "select a.id, a.name as name, a.master_id as extra\r\n"
            + "from common_setup_details a \r\n"
            + "where 1=1\r\n"
            + "and a.active = true\r\n"
            + "order by a.id desc";

    @Query(value = dropdownQuery, nativeQuery = true)
    List<CommonDropdownModel> findDropdownModel();

    String dropdownQueryByModuleId = "select a.id, a.name as name, a.master_id as extra\r\n"
            + "from common_setup_details a \r\n"
            + "where 1=1\r\n"
            + "and a.module_id = :moduleId\r\n"
            + "and a.active = true\r\n"
            + "order by a.id desc";

    @Query(value = dropdownQueryByModuleId, nativeQuery = true)
    List<CommonDropdownModel> findDropdownModelByModuleId(
            @Param("moduleId") Integer moduleId
    );

    String findByDevCodeAndMasterIdQuery = "select a.*\n" +
            "from common_setup_details a, common_setup_master b\n" +
            "where 1=1\n" +
            "and a.master_id = b.id \n" +
            "and b.dev_code = :devCode\n" +
            "and a.parent_id  = :parentId\n" +
            "and a.active is true\r\n" +
            "order by a.name";
    @Query(value = findByDevCodeAndMasterIdQuery, nativeQuery = true)
    List<SetupDetails> findByDevCodeAndParentIdAndActive(
            @Param("devCode") Integer devCode, @Param("parentId") Integer parentId
    );

    String findByDevCodeQuery = "select a.*\n" +
            "from common_setup_details a, common_setup_master b\n" +
            "where 1=1\n" +
            "and a.master_id = b.id \n" +
            "and b.dev_code = :devCode\n" +
            "and a.active is true\r\n" +
            "order by a.name";

    @Query(value = findByDevCodeQuery, nativeQuery = true)
    List<SetupDetails> findByDevCodeAndActive(
            @Param("devCode") Integer devCode
    );

}
