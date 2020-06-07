package com.example.wrapspringbootstarter.service;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 2020-06-07
 * Time: 23:07
 */
//@ALLArgsConstructor
@Data
@AllArgsConstructor()
public class WrapService {

    private String before;
    private String after;

    public String wrap(String word){
        return before+word+after;
    }
}
