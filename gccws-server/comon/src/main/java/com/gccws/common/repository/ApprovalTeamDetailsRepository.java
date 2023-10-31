package com.gccws.common.repository;

import com.gccws.common.entity.ApprovalTeamDetails;
import com.gccws.base.repository.BaseRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author    Rima
 * @Since     February 22, 2023
 * @version   1.0.0
 */

@Repository
public interface ApprovalTeamDetailsRepository extends BaseRepository<ApprovalTeamDetails> {
	 List<ApprovalTeamDetails> findByMasterId(int id);
	 //List<ApprovalTeamDetails> findByMasterAppUserId(Integer appUserId);
}
