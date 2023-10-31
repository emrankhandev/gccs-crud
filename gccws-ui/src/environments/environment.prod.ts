// export NODE_OPTIONS=--max_old_space_size=4096;
export const environment = {
  production: true,
  domain   : '192.168.20.17',
  app: {
    baseApiEndPoint: 'https://ppa.addiesoft.com/payra-port-accounting-server/api/',
    apiEndPointPublic: 'public/',
    apiEndPointPrivate: 'private/',
  },
};
