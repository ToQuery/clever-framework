package io.github.toquery.framework.system.service;

import io.github.toquery.framework.core.exception.AppException;
import io.github.toquery.framework.curd.service.AppBaseService;
import io.github.toquery.framework.system.domain.SysRole;

import java.util.List;
import java.util.Set;

/**
 * @author toquery
 * @version 1
 */
public interface ISysRoleService extends AppBaseService<SysRole, Long> {
    List<SysRole> findByCode(String code);

    SysRole saveSysRoleCheck(SysRole sysRole) throws AppException;

    void deleteSysRoleCheck(Set<Long> ids) throws AppException;
}
