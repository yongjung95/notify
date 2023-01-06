package com.jung.notify.api.response.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

@Getter
@Setter
public class PagingListResult<T> extends CommonResult{
    private Page<T> data;
}
