package com.gccws.base.model;

import lombok.Data;

@Data
public class PageableData {
    private Integer page;
    private Integer size;
    private String searchValue;
}
