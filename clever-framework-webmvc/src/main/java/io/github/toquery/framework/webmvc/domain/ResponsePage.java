package io.github.toquery.framework.webmvc.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author toquery
 * @version 1
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponsePage {

    private int pageSize;

    private int pageNumber;

    private int totalElements;

    private int totalPages;
}