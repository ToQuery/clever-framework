package io.github.toquery.framework.security.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toquery.framework.common.entity.AppEntityTree;
import io.github.toquery.framework.core.constant.AppPropertiesDefault;
import io.github.toquery.framework.dao.entity.AppBaseEntityJpaSoftDelEntity;
import io.github.toquery.framework.dao.entity.AppBaseEntityPrimaryKeyLong;
import io.github.toquery.framework.dao.entity.AppBaseEntitySort;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.HashSet;

/**
 * @author toquery
 * @version 1
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sys_menu")
public class SysMenu extends AppBaseEntityPrimaryKeyLong implements AppBaseEntitySort, AppBaseEntityJpaSoftDelEntity {

    public SysMenu(@NotNull @Size(max = 50) String name, @NotNull @Size(max = 50) String code) {
        this.name = name;
        this.code = code;
    }

    @NotNull
    @Size(max = 50)
    @Column(length = 50)
    private String name;

    @NotNull
    @Size(max = 50)
    @Column(length = 50)
    private String code;


    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "sys_role_menu",
            joinColumns = {@JoinColumn(name = "menu_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    @BatchSize(size = 20)
    private Collection<SysRole> roles = new HashSet<>();

    // 树状结构
    @Column(name = "level")
    private int level = 0;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "parent_ids")
    private String parentIds;

    @Column(name = "has_children")
    private boolean hasChildren = false;

    @Column(name = "sort_num")
    private int sortNum = 0;

    /**
     * 是否删除：1已删除；0未删除
     */
    @ColumnDefault("false")
    @Column(name = AppPropertiesDefault.JPA_COLUMN_SOFT_DEL)
    private boolean del = false;


    /**
     * 子集
     */
    @Transient
    private Collection<SysMenu> children = new HashSet<>();

    /**
     * 父级信息
     */
    @Transient
    private SysMenu parent;


    public boolean isHasChildren() {
        return hasChildren;
    }

    @Override
    public boolean getDel() {
        return del;
    }

    public boolean getHasChildren() {
        return hasChildren;
    }

}
