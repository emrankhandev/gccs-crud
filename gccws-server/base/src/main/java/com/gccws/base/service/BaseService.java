package com.gccws.base.service;

import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @Author    Md. Mizanur Rahman
 * @Since     August 1, 2022
 * @version   1.0.0
 */

/**
 * M is Request Response Model, D is Dropdown Response, P is Pageable Body
 * */

public interface BaseService<M, D, P> {
    M save(M obj, int userId);
    M update(M obj, int userId);
    Boolean delete(M obj, int userId);
    M getById(int id, int userId);
    List<D> getDropdownList(int userId);
    Page<M> getPageableListData(P pageableBody, int userId);
}
