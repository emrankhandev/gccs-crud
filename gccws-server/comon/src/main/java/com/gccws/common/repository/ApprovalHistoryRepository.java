package com.gccws.common.repository;

import com.gccws.common.model.CommonDropdownModel;
import com.gccws.common.entity.ApprovalHistory;
import com.gccws.base.repository.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
/**
 * @Author    Rima
 * @Since     February 23, 2023
 * @version   1.0.0
 */
@Repository
public interface ApprovalHistoryRepository extends BaseRepository<ApprovalHistory> {

    List<ApprovalHistory> findByTransactionIdAndTransactionTypeId(Integer transactionId, Integer transactionTypeId);


    String searchQuery = "select a.*\n"
            + "from sya_approval_history a \n"
            + "where 1=1\n"
            + "and concat(a.a.approval_status_name,a.transaction_table_name, a.transaction_type_name) ilike  %:searchValue%\n";

    @Query(value = searchQuery, nativeQuery = true)
    Page<ApprovalHistory> searchPageableList(
            @Param("searchValue") String searchValue,
            Pageable pageable
    );

    String dropdownQuery = "select a.id, '' as name\r\n"
            + "from sya_approval_history a \r\n"
            + "where 1=1\r\n"
            + "and a.active = true\r\n"
            + "order by a.id desc";

    @Query(value = dropdownQuery, nativeQuery = true)
    List<CommonDropdownModel> findDropdownModel();

    String notificationListQuery = "select a.* \n" +
            "from sya_approval_history a \n" +
            "where 1=1\n" +
            "and a.is_cross is not true\n" +
            "and a.default_app_user_id  = :userId\n" +
            "order by a.id desc";

    @Query(value = notificationListQuery, nativeQuery = true)
    List<ApprovalHistory> findNotificationListByNotifyUserId(
            @Param("userId") Integer userId
    );

    String approvalPendingListQuery = "select a.*\n" +
            "from sya_approval_history a\n" +
            "where 1=1\n" +
            "and a.is_close is not true\n" +
            "and a.to_team_id in  (select y.id\n" +
            "\t\t\t\t\t\tfrom sya_approval_team_details x, sya_approval_team_master y\n" +
            "\t\t\t\t\t\twhere y.id = x.master_id \n" +
            "\t\t\t\t\t\tand x.app_user_id = :userId)\n" +
            "and a.approval_status_id not in (4,5)\n" +
            "and date(a.entry_date) between date(:fromDate) and date(:toDate)\n" +
            "and case when :transactionTypeId = 0 then true else a.transaction_type_id = :transactionTypeId end\n" +
            "order by a.id desc";

    @Query(value = approvalPendingListQuery, nativeQuery = true)
    List<ApprovalHistory> findApprovalPendingList(
            @Param("userId") Integer userId,
            @Param("fromDate") Date fromDate,
            @Param("toDate") Date toDate,
            @Param("transactionTypeId") Integer transactionTypeId
    );

    String findByTypeAndTransactionQuery = "select a.*\n" +
            "from sya_approval_history a\n" +
            "where 1=1\n" +
            "and a.transaction_type_id = (select x.transaction_type_id\n" +
            "\t\t\tfrom sya_approval_history x\n" +
            "\t\t\twhere x.id = :approvalHistoryId)\n" +
            "and a.transaction_id = (select x.transaction_id\n" +
            "\t\t\tfrom sya_approval_history x\n" +
            "\t\t\twhere x.id = :approvalHistoryId)\n" +
            "and a.approval_status_id is not null\n" +
            "order by id";

    @Query(value = findByTypeAndTransactionQuery, nativeQuery = true)
    List<ApprovalHistory> findByApprovalHistoryId(
            @Param("approvalHistoryId") Integer approvalHistoryId
    );


    String filterByDateApprovalPendingListQuery = "select a.*\n" +
            "from sya_approval_history a \n" +
            "where 1=1\n" +
            "and a.approval_status_id in (2,4)\n" +
            "and a.from_app_user_id = :appUserId\n" +
            "and date(a.entry_date) between :formDate and :toDate";

    @Query(value = filterByDateApprovalPendingListQuery, nativeQuery = true)
    List<ApprovalHistory> findApprovalPendingListByDate(
            @Param("appUserId") Integer userId,
            @Param("formDate") @Temporal Date formDate,
            @Param("toDate") @Temporal Date toDate
    );



}
