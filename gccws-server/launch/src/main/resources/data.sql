INSERT INTO public.sya_password_policy (active,entry_app_user_code,entry_date,entry_user,update_app_user_code,update_date,update_user,alphanumeric,match_username,min_length,"name",password_age,password_remember,sequential,special_char,upper_lower) VALUES
    (true,NULL,'2023-01-01 00:00:00',1,NULL,'2023-01-29 11:54:33.758',1,false,false,5,'Simple Policy',60,NULL,false,false,false);

INSERT INTO public.sya_app_user (active,entry_app_user_code,entry_date,entry_user,update_app_user_code,update_date,update_user,is_account_expired,is_account_locked,is_credentials_expired,display_name,email,mobile,"password",username,password_policy_id,user_type_id,user_type_name) VALUES
    (true,NULL,'2023-01-01 00:00:00',1,NULL,NULL,NULL,false,false,false,'Developer Team',NULL,NULL,'$2a$10$BO137irnGuzS.Bte4vctjuB.p4/fxiFwKOyomjDNiXNImAVQDb/Ym','developer',1,NULL,NULL);

INSERT INTO public.sya_password_history (active,entry_app_user_code,entry_date,entry_user,update_app_user_code,update_date,update_user,"password",app_user_id) VALUES
    (true,'404','2023-01-18 09:49:17.374',1,NULL,NULL,NULL,'7654321',1);

INSERT INTO public.sya_menu_item (active,entry_app_user_code,entry_date,entry_user,update_app_user_code,update_date,update_user,bangla_name,is_delete,is_edit,icon,is_insert,menu_type,menu_type_name,name,serial_no,url,is_view,parent_id,report_upload_id) VALUES
     (true,NULL,'2023-01-17 12:13:47.174',1,NULL,'2023-01-29 13:03:49.65',1,'সুপার অ্যাডমিন',NULL,NULL,'cil-settings',NULL,1,'Module','Super Admin',1,'/super-admin',true,NULL,NULL),
     (true,NULL,'2023-01-19 16:34:20.773',1,NULL,'2023-01-26 17:15:18.627',1,'প্যারামিটার অ্যাসাইন',true,true,'',true,3,'Menu','Parameter Assign',9,'/parameter-assign',true,1,NULL),
     (true,NULL,'2023-01-18 15:03:33.539',1,NULL,'2023-01-26 17:15:27.819',1,'রিপোর্ট',true,true,'',true,3,'Menu','Report',10,'/report',true,1,NULL),
     (true,NULL,'2023-01-19 16:28:49.728',1,NULL,'2023-01-26 17:10:30.749',1,'মেনু আইটেম',true,true,'',true,3,'Menu','Menu Item',1,'/menu-item',true,1,NULL),
     (true,NULL,'2023-01-19 16:31:17.088',1,NULL,'2023-01-26 17:12:15.392',1,'পাসওয়ার্ড নীতি',true,true,'',true,3,'Menu','Password Policy',2,'/password-policy',true,1,NULL),
     (true,NULL,'2023-01-17 12:18:13.532',1,NULL,'2023-01-26 17:12:35.248',1,'ব্যবহারকারীর ভূমিকা',true,true,'',true,3,'Menu','User Role',4,'/user-role',true,1,NULL),
     (true,NULL,'2023-01-17 12:17:00.889',1,NULL,'2023-01-26 17:12:46.832',1,'অ্যাপ্লিকেশন ব্যবহারকারী',true,true,'',true,3,'Menu','Application User',3,'/application-user',true,1,NULL),
     (true,NULL,'2023-01-19 16:30:26.691',1,NULL,'2023-01-26 17:13:08.801',1,'ইউজার রোল অ্যাসাইন',true,true,'',true,3,'Menu','User Role Assign',5,'/user-role-assign',true,1,NULL),
     (true,NULL,'2023-01-19 16:32:06.738',1,NULL,'2023-01-26 17:14:20.05',1,'রিপোর্ট আপলোড',true,true,'',true,3,'Menu','Report Upload',6,'/report-upload',true,1,NULL),
     (true,NULL,'2023-01-19 16:32:54.034',1,NULL,'2023-01-26 17:14:30.463',1,'সাব রিপোর্ট মাস্টার',true,true,'',true,3,'Menu','Sub Report Master',7,'/sub-report-master',true,1,NULL),
     (true,NULL,'2023-01-19 16:33:38.639',1,NULL,'2023-01-26 17:14:56.893',1,'প্যারামিটার মাস্টার',true,true,'',true,3,'Menu','Parameter Master',8,'/parameter-master',true,1,NULL);

INSERT INTO public.sya_user_role_master (active,entry_app_user_code,entry_date,entry_user,update_app_user_code,update_date,update_user,bangla_name,name) VALUES
    (true,NULL,'2023-01-18 15:04:26.82',1,NULL,'2023-01-26 11:28:48.476',1,'Super Admin','Super Admin');

INSERT INTO public.sya_user_role_details (active,entry_app_user_code,entry_date,entry_user,update_app_user_code,update_date,update_user,is_delete,is_edit,is_insert,is_view,master_id,menu_item_id) VALUES
    (NULL,NULL,'2023-01-18 15:04:26.85',1,NULL,NULL,NULL,true,true,true,true,1,2),
    (NULL,NULL,'2023-01-19 16:36:51.25',1,NULL,NULL,NULL,true,true,true,true,1,3),
    (NULL,NULL,'2023-01-19 16:36:51.25',1,NULL,NULL,NULL,true,true,true,true,1,4),
    (NULL,NULL,'2023-01-19 16:36:51.251',1,NULL,NULL,NULL,true,true,true,true,1,5),
    (NULL,NULL,'2023-01-19 16:36:51.251',1,NULL,NULL,NULL,true,true,true,true,1,6),
    (NULL,NULL,'2023-01-19 16:36:51.252',1,NULL,NULL,NULL,true,true,true,true,1,7),
    (NULL,NULL,'2023-01-19 16:36:51.252',1,NULL,NULL,NULL,true,true,true,true,1,8),
    (NULL,NULL,'2023-01-19 16:36:51.252',1,NULL,NULL,NULL,true,true,true,true,1,9),
    (NULL,NULL,'2023-01-19 16:36:51.252',1,NULL,NULL,NULL,true,true,true,true,1,10),
    (NULL,NULL,'2023-01-19 16:36:51.252',1,NULL,NULL,NULL,true,true,true,true,1,11);

INSERT INTO public.sya_user_role_assign_master (active,entry_app_user_code,entry_date,entry_user,update_app_user_code,update_date,update_user,app_user_id) VALUES
    (NULL,NULL,'2023-01-25 15:38:46.663',1,NULL,NULL,NULL,1);

INSERT INTO public.sya_user_role_assign_details (active,entry_app_user_code,entry_date,entry_user,update_app_user_code,update_date,update_user,master_id,user_role_id) VALUES
    (NULL,NULL,'2023-01-18 15:05:00.09',1,NULL,NULL,NULL,1,1);
