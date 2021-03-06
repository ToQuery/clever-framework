package io.github.toquery.framework.core.log;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author toquery
 * @version 1 modify
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum AppLogType {

    CREATE("创建"),
    MODIFY("修改"),
    DELETE("删除"),
    QUERY("查看");

    private String remark;
}
