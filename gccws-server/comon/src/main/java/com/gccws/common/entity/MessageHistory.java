package com.gccws.common.entity;

import com.gccws.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

/**
 * @author  Md. Mizanur Rahman
 * @Email   kmmizanurrahmanjp@gmail.com
 * @since   March 14,2023
 * @version 1.0
 */

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "COMMON_MESSAGE_HISTORY")
public class MessageHistory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ManyToOne
    @JoinColumn(name = "MODULE_ID")
    private MenuItem module;

    @Column(name = "TRANSACTION_ID", nullable = false)
    private Integer transactionId;

    @Column(name = "TRANSACTION_TABLE", nullable = false)
    private String transactionTable;

    @Column(name = "TRANSACTION_TYPE")
    private String transactionType;

    @Column(name = "MESSAGE", length = 4000)
    private String  message;

    @Column(name = "ATTACHMENT")
    private String  attachment;

    @Column(name = "ATTACHMENT_LOCATION")
    private String  attachmentLocation;

    @Column(name = "IS_READ", columnDefinition = "boolean default false")
    private Boolean isRead = false;

    @Column(name = "READ_DATE")
    private Date readDate;

    @Column(name = "IS_CLOSE", columnDefinition = "boolean default false")
    private Boolean isClose = false;

    @Column(name = "IS_ACTION", columnDefinition = "boolean default false")
    private Boolean isAction = false;

    @Column(name = "LINK")
    private String link;

    @ManyToOne
    @JoinColumn(name = "AUTHORITY_ID")
    private AppUser authority;

    @ManyToOne
    @JoinColumn(name = "RECEIVED_USER_ID")
    private AppUser receivedUser;

    @Column(name = "PUBLISH_DATE")
    private Date publishDate;

    @Column(name = "DESCRIPTION", length = 5000)
    private String description;

}
