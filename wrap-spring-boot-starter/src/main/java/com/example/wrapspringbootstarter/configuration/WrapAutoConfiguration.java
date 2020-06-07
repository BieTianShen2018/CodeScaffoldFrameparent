package com.example.wrapspringbootstarter.configuration;

import com.example.wrapspringbootstarter.service.WrapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 2020-06-07
 * Time: 23:24
 * @author admin
 */
@Configuration()
@ConditionalOnClass({WrapService.class})
@EnableConfigurationProperties({WrapServiceProperties.class})
public class WrapAutoConfiguration {

    @Autowired
    private WrapServiceProperties wsp;

    @Bean(name="wrapTest")
    public WrapService wrapTemplate() throws Exception {
        WrapService template = new WrapService(wsp.getPrefix(),wsp.getSuffix());
        return template;
    }

}
