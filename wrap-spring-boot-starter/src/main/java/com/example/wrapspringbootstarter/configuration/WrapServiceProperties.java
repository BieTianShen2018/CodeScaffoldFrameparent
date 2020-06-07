package com.example.wrapspringbootstarter.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 2020-06-07
 * Time: 23:28
 */
@ConfigurationProperties(
        prefix = "wrap.service"
)
@Data
public class WrapServiceProperties {

    private String prefix="aaa";
    private  String suffix="bbb";
}
