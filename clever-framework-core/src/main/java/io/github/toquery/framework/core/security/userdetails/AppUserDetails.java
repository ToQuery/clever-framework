package io.github.toquery.framework.core.security.userdetails;

import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

/**
 * 用户基础字段定义与 Spring UserDetails 相互辅助
 */
public interface AppUserDetails extends UserDetails {

    Long getId();

    Date getLastPasswordResetDate();

    void setLastPasswordResetDate(Date lastPasswordResetDate);
}
