import { INavData } from '@coreui/angular';

export const navItems: INavData[] = [
  {
    name: 'Dashboard',
    url: '/dashboard-home',
    iconComponent: { name: 'cil-home' },
  },
  {
    name: 'Home',
    url: '/user',
    iconComponent: { name: 'cil-home' },
  },

  // super admin
  {
    name: 'Super Admin',
    url: '/super-admin',
    iconComponent: { name: 'cil-user' },
    children: [
      {
        name: 'Menu Item',
        url: '/super-admin/menu-item',
      },
      {
        name: 'User Role',
        url: '/super-admin/user-role',
      },
      {
        name: 'Password Policy',
        url: '/super-admin/password-policy',
      },
      {
        name: 'Application User',
        url: '/super-admin/application-user',
      },
      {
        name: 'App User Assign',
        url: '/super-admin/app-user-assign',
      },
      {
        name: 'User Role Assign',
        url: '/super-admin/user-role-assign',
      },
      {
        name: 'File Validator',
        url: '/super-admin/file-validator',
      },
      {
        name: 'Report Upload',
        url: '/super-admin/report-upload',
      },
      {
        name: 'Sub Report Master',
        url: '/super-admin/sub-report-master',
      },
      {
        name: 'Parameter Master',
        url: '/super-admin/parameter-master',
      },
      {
        name: 'Parameter Assign',
        url: '/super-admin/parameter-assign',
      },
      {
        name: 'Report',
        url: '/super-admin/report',
      },

    ]
  },

//admin
  {
    name: 'Admin',
    url: '/admin',
    iconComponent: { name: 'cil-user' },
    children: [
      {
        name: 'Database',
        url: '/admin/database',
      },
      {
        name: 'Client',
        url: '/admin/client',
      },
      {
        name: 'Merchant',
        url: '/admin/merchant',
      },


    ]
  },

  //admin
  {
    name: 'Billing',
    url: '/billing',
    iconComponent: { name: 'cil-user' },
    children: [
      {
        name: 'document',
        url: '/billing/document',
      },
    ]
  },

];
