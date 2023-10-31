package com.gccws.common.repository;


import java.util.List;

import com.gccws.common.model.CommonDropdownModel;
import com.gccws.base.repository.BaseRepository;
import com.gccws.common.entity.PasswordPolicy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


/**
 * @Author    Md. Chabed Alam
 * @Since     January 11, 2023
 * @version   1.0.0
 */

@Repository
public interface PasswordPolicyRepository extends BaseRepository<PasswordPolicy> {

	PasswordPolicy findByDevCode(Integer devCode);

	String searchQuery = "select *\n"
			+ "from sya_password_policy \n"
			+ "where  name ilike %:searchValue%";

    @Query(value = searchQuery, nativeQuery = true)
    Page<PasswordPolicy> searchPageableList(
            @Param("searchValue") String searchValue,
            Pageable pageable
    );
    
    String dropdownQuery = "select a.id, a.name as name\r\n"
			+ "from sya_password_policy a \r\n"
			+ "where 1=1\r\n"
			+ "and a.active = true\r\n"
			+ "order by a.id desc";

	@Query(value = dropdownQuery, nativeQuery = true)
	List<CommonDropdownModel> findDropdownModel();
	String agentPolicyQuery = "select a.*\n" +
			"from sya_password_policy a\n" +
			"where id = (select min(id) from sya_password_policy)";
	@Query(value = agentPolicyQuery, nativeQuery = true)
	PasswordPolicy findAgentPolicy(
	);
}
