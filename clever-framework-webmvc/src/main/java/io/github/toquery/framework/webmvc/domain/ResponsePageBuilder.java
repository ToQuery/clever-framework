package io.github.toquery.framework.webmvc.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.hateoas.PagedModel;

import java.util.HashMap;

/**
 * 将不同类型的分页转为相同的page对象
 * 查询出当前页号+1，第一页为1 !
 * <p>
 * TODO 可优化 InitializingBean
 *
 * @author toquery
 * @version 1
 */
@Getter
@Setter
@AllArgsConstructor
public final class ResponsePageBuilder {
    private int pageSize;
    private int pageNum;
    private Long totalElements;
    private int totalPages;

    public ResponsePageBuilder() {
    }

    public ResponsePageBuilder(ResponsePage responsePage) {
        this.pageSize = responsePage.getPageSize();
        this.pageNum = responsePage.getPageSize();
        this.totalElements = responsePage.getTotalElements();
        this.totalPages = responsePage.getTotalPages();
    }

    public ResponsePageBuilder(PagedModel<?> pagedResources) {
        this(pagedResources.getMetadata());
    }

    public ResponsePageBuilder(PagedModel.PageMetadata pageMetadata) {
        if (pageMetadata != null) {
            this.pageNum = (int) pageMetadata.getNumber() + 1;
            this.pageSize = (int) pageMetadata.getSize();
            this.totalElements = pageMetadata.getTotalElements();
            this.totalPages = (int) pageMetadata.getTotalPages();
        }
    }

    /**
     * 将 Page helper 对象转化为Page对象
     *
     * @param page Page helper 对象
     */
    public ResponsePageBuilder(com.github.pagehelper.Page<?> page) {
        if (page != null) {
            this.pageNum = page.getPageNum() + 1;
            this.pageSize = page.getPageSize();
            this.totalElements = page.getTotal();
            this.totalPages = page.getPages();
        }
    }

    public ResponsePageBuilder(org.springframework.data.domain.Page<?> page) {
        if (page != null) {
            this.pageNum = page.getNumber() + 1;
            this.pageSize = page.getSize();
            this.totalElements = page.getTotalElements();
            this.totalPages = page.getTotalPages();
        }
    }


    public ResponsePageBuilder page(PagedModel<?> pagedResources) {
        return this.page(pagedResources.getMetadata());
    }


    public ResponsePageBuilder page(PagedModel.PageMetadata pageMetadata) {
        if (pageMetadata != null) {
            this.pageNum = (int) pageMetadata.getNumber() + 1;
            this.pageSize = (int) pageMetadata.getSize();
            this.totalElements = pageMetadata.getTotalElements();
            this.totalPages = (int) pageMetadata.getTotalPages();
        }
        return this;
    }

    /**
     * 将 Page helper 对象转化为Page对象
     *
     * @param page Page helper 对象
     */
    public ResponsePageBuilder page(com.github.pagehelper.Page<?> page) {
        if (page != null) {
            this.pageNum = page.getPageNum() + 1;
            this.pageSize = page.getPageSize();
            this.totalElements = page.getTotal();
            this.totalPages = page.getPages();
        }
        return this;
    }

    public ResponsePageBuilder page(org.springframework.data.domain.Page<?> page) {
        if (page != null) {
            this.pageNum = page.getNumber() + 1;
            this.pageSize = page.getSize();
            this.totalElements = page.getTotalElements();
            this.totalPages = page.getTotalPages();
        }
        return this;
    }

    public ResponsePageBuilder pageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public ResponsePageBuilder pageNum(int pageNum) {
        this.pageNum = pageNum;
        return this;
    }

    public ResponsePageBuilder totalElements(Long totalElements) {
        this.totalElements = totalElements;
        return this;
    }

    public ResponsePageBuilder totalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }

    public ResponsePage build() {
        return new ResponsePage(this);
    }
}
