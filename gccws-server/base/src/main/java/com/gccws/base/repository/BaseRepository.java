package com.gccws.base.repository;

import java.util.List;

import com.gccws.base.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @Author    Md. Mizanur Rahman
 * @Since     August 1, 2022
 * @version   1.0.0
 */

/**
 * E is Entity
 * */

@NoRepositoryBean
public interface BaseRepository<E extends BaseEntity>  extends JpaRepository<E, Integer> {
    List<E> findByActive(boolean active);
}
