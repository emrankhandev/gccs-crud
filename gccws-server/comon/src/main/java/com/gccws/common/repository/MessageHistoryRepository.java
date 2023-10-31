package com.gccws.common.repository;

import com.gccws.base.repository.BaseRepository;
import com.gccws.common.entity.MessageHistory;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author  Md. Mizanur Rahman
 * @Email   kmmizanurrahmanjp@gmail.com
 * @since   March 14,2023
 * @version 1.0
 */

@Repository
public interface MessageHistoryRepository extends BaseRepository<MessageHistory> {

    List<MessageHistory> findByTransactionIdAndTransactionTable(Integer transactionId, String transactionTable);

    String findByUserIdQuery = "select a.* \n" +
            "from COMMON_MESSAGE_HISTORY a \n" +
            "where 1=1\n" +
            "and a.RECEIVED_USER_ID  = :userId\n" +
            "and a.IS_CLOSE  = false\n" +
            "order by a.id desc";

    @Query(value = findByUserIdQuery, nativeQuery = true)
    List<MessageHistory> findByUserId(
            @Param("userId") Integer userId
    );

}
