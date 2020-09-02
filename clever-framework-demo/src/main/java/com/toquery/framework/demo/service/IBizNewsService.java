package com.toquery.framework.demo.service;


import com.toquery.framework.demo.entity.BizNews;
import io.github.toquery.framework.crud.service.AppBaseService;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */

public interface IBizNewsService extends AppBaseService<BizNews, Long> {

    BizNews update(Long id, String name);

    BizNews getById(Long id);

    Page<BizNews> findByName(String name, Integer page, Integer size);

    org.springframework.data.domain.Page<BizNews> queryJpaByPage(int requestPageNumber, int requestPageSize);

    com.github.pagehelper.Page<BizNews> queryMyBatisByPage(int requestPageNumber, int requestPageSize);

    List<BizNews> listJpa();

    List<BizNews> listMyBatis();

    BizNews saveJpa(BizNews bizNews);

    BizNews saveMyBatis(BizNews bizNews);

    BizNews updateJpa(BizNews bizNews);

    BizNews updateMyBatis(BizNews bizNews);

    void deleteJpa(Set<Long> ids);

    void deleteMyBatis(Set<Long> ids);

    BizNews detailJpa(Long id);

    BizNews detailMyBatis(Long id);
}
