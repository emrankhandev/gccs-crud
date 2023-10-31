package com.gccws.common.repository;


import java.util.List;

import com.gccws.base.repository.BaseRepository;
import com.gccws.common.entity.UserRoleDetails;
import org.springframework.stereotype.Repository;

/**
 * @Author    Md. Chabed Alam
 * @Since     August 29, 2022
 * @version   1.0.0
 */

@Repository
public interface UserRoleDetailsRepository extends BaseRepository<UserRoleDetails> {
	 List<UserRoleDetails> findByMasterId(int id);
	 List<UserRoleDetails> findByMasterIdAndMenuItemMenuTypeAndMenuItemParentIdOrderByMenuItemSerialNoAsc(int roleId, int menuType, int moduleId);

}
