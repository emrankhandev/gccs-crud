package com.gccws.common.utils;


import com.gccws.base.utils.BaseConstants;

/**
 * @Author    Md. Mizanur Rahman
 * @Since     August 1, 2022
 * @version   1.0.0
 */

public interface CommonConstants extends BaseConstants {

    //================================== *** ==================================
    //									API MODULE endpoint
    //================================== *** ==================================
    /*module URL*/
    String COMMON_END_POINT = PRIVATE_APP_ENDPOINT + "common/";
    String PUBLIC_COMMON_END_POINT = PUBLIC_APP_ENDPOINT  + "common/";
    String AUTH_END_POINT = APP_ENDPOINT + "auth/";
    String SYSTEM_ADMIN_END_POINT = PRIVATE_APP_ENDPOINT + "sya/";
    String SYSTEM_PUBLIC_ADMIN_END_POINT = PUBLIC_APP_ENDPOINT  + "sya/";
    String HR_END_POINT = PRIVATE_APP_ENDPOINT + "hr/";
    String ACCOUNTS_END_POINT = PRIVATE_APP_ENDPOINT + "accounts/";
    String PAYROLL_END_POINT = PRIVATE_APP_ENDPOINT + "payroll/";
    String BUDGET_END_POINT = PRIVATE_APP_ENDPOINT + "budget/";
    String BILLING_END_POINT = PRIVATE_APP_ENDPOINT + "billing/";
    String PUBLIC_BILLING_END_POINT = PUBLIC_APP_ENDPOINT  + "billing/";
    String BIMAN_END_POINT = PRIVATE_APP_ENDPOINT  + "biman/";

    String INVENTORY_END_POINT = PRIVATE_APP_ENDPOINT + "inventory/";
    String INVENTORY_BILLING_END_POINT = PUBLIC_APP_ENDPOINT  + "inventory/";



    //================================== *** ==================================
    //									VALIDATION PATTERN
    //================================== *** ==================================

    String INT_V_P = "[0-9]*";
    String INT_V_P_MAX_L_20 = "^[0-9]{0,20}$";

    String STRING_V_P = "^[A-Za-z0-9]$";
    String STRING_V_P_MAX_L_50 = "^[A-Za-z0-9]{0,50}$";

    String STRING_V_P_U_D = "^[A-Za-z0-9_-]$";
    String STRING_V_P_U_D_MAX_L_50 = "^[A-Za-z0-9_-]{0,50}$";

    String DAY_MONTH_YEAR_V_P = "^[0-9]{8}$"; //10092023
    String MONTH_YEAR_V_P = "^[0-9]{6}$"; //092023

    //================================== *** ==================================
    //									FILE VALIDATOR DEV CODE
    //================================== *** ==================================

    Integer REPORT_UPLOAD_VALIDATOR_CODE = 1;


    //================================== *** ==================================
    //									Static Field
    //================================== *** ==================================

    Integer USER_TYPE_EMP = 2; // 1= SYSTEM ADMIN SAVED USER, 2= EMPLOYEE SAVED USER
    String COMMON_AGENT_PASSWORD = "agent123";

    Integer COMMON_AGENT_PASSWORD_POLICY_ID= 2;
    Integer COMMON_AGENT_USER_ROLE_MASTER_ID = 4;
    Integer CANDF_AGENT_USER_ROLE_MASTER_ID = 5;

    Integer AGENT_APPROVE_TYPE = 2;

    Integer COLLECTION_TYPE_SALARY_ID = 1;

    /*Integration With Payroll*/
    Integer INTEGRATION_WITH_PAYROLL_DEV_CODE = 1;
    Integer NOC_DR_DEV_CODE = 2;
    Integer NOC_CR_DEV_CODE = 3;
    Integer BILL_COVER_PAGE_RECEIVABLE_DR_DEV_CODE = 4;
    Integer BILL_COVER_PAGE_VAT_AMOUNT_DR_DEV_CODE = 5;
    Integer BILL_COVER_PAGE_CR_DEV_CODE = 6;

    Integer ITEM_RECEIVE_DR_DEV_CODE = 7;
    Integer ITEM_RECEIVE_CR_DEV_CODE = 8;

    Integer BILL_ADVANCE_FOR_NOC_DEBIT_DR_DEV_CODE = 9;

    Integer BILL_ADJUSTMENT_RECEIVABLE_DR_DEV_CODE = 10;

    Integer BILL_ADJUSTMENT_RECEIVABLE_VAT_AMOUNT_CR_DEV_CODE = 11;

    Integer AGENT_TYPE = 1;
    
}
