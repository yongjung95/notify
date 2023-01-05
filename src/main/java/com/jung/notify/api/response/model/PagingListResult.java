package com.jung.notify.api.response.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PagingListResult<T> extends CommonResult{
    private List<?> data;
    private int pageNumber;
    private int totalPageNumber;
    private boolean isFirst;
    private boolean hasNext;
}
