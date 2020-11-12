package com.night.filter.domain;

import lombok.Data;

@Data
public class RetResult<T> {

    private int code = 200;

    private String message = "success";

    private  T data;
}
