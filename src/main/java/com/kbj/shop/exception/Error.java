package com.kbj.shop.exception;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Error {
    private int code;
    private String message;
}
