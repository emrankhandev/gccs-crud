package com.gccws.base.controller;

/**
 * @Author    Md. Mizanur Rahman
 * @Since     August 1, 2022
 * @version   1.0.0
 */

/**
 * R is Response, B is Body, P is Pageable Body, S is Servlet Request
 * */
public interface BaseController<R, B, P, S>{
	R save(B body, S request);
	R update(B body, S request);
	R delete(B body, S request);
	R getById(int id, S request);
	R getDropdownList(S request);
	R getPageableListData(P pageableBody, S request);
}
