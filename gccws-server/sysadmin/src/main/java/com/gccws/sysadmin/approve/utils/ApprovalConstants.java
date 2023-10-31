package com.gccws.sysadmin.approve.utils;

public interface ApprovalConstants {
    /*approval status*/
    Integer SUBMIT = 1;
    Integer FORWARD = 2;
    Integer BACK = 3;
    Integer APPROVED = 4;

    Integer REJECT = 5;

    /*server message*/
    String SUBMIT_MESSAGE_EN = "Successfully submitted";
    String SUBMIT_MESSAGE_BN = "সফলভাবে জমা দেওয়া হয়েছে";

    String FORWARD_MESSAGE_EN = "Successfully Forwarded";
    String FORWARD_MESSAGE_BN = "সফলভাবে পাঠানো হয়েছে";

    String APPROVED_MESSAGE_EN = "Successfully Approved";
    String APPROVED_MESSAGE_BN = "সফলভাবে অনুমোদিত";

    String BACK_MESSAGE_EN = "Successfully Backed";
    String BACK_MESSAGE_BN = "সফলভাবে ব্যাক করা হয়েছে";

    String REJECT_MESSAGE_EN = "Successfully Reject";
    String REJECT_MESSAGE_BN = "সফলভাবে প্রত্যাখ্যান করা হয়েছে";



    /*transaction type*/

    Integer JOURNAL_ENTRY = 1;
    Integer SALARY_APPROVAL=2;

    Integer BONUS_APPROVAL=3;


    /*transaction type*/
    Integer PRESENT = 1;
    String PRESENT_VALUE= "Present";

    Integer ON_LEAVE = 6;
    String ON_LEAVE_VALUE= "On Leave";

}
