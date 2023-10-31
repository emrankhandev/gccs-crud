package com.gccws.sysadmin.report.repository;

import com.gccws.base.repository.BaseRepository;
import com.gccws.sysadmin.report.entity.ParameterAssignDetail;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author		Rima
 * @Since		January 10, 2023
 * @version		1.0.0
 */

@Repository
public interface ParameterAssignDetailRepository extends BaseRepository<ParameterAssignDetail> {

    List<ParameterAssignDetail> findByParameterAssignMasterIdOrderBySerialNoAsc(int id);

}
