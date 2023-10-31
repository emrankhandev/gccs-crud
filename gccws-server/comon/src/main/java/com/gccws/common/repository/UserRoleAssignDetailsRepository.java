package com.gccws.common.repository;


import java.util.List;

import com.gccws.base.repository.BaseRepository;
import com.gccws.common.entity.UserRoleAssignDetails;
import org.springframework.stereotype.Repository;

/**
 * @Author    Rima
 * @Since     September 1, 2022
 * @version   1.0.0
 */

@Repository
public interface UserRoleAssignDetailsRepository extends BaseRepository<UserRoleAssignDetails> {
	 List<UserRoleAssignDetails> findByMasterId(int id);
	 List<UserRoleAssignDetails> findByMasterAppUserId(Integer appUserId);
}
