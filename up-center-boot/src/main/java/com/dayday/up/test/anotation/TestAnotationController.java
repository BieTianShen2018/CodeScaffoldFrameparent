package com.dayday.up.test.anotation;

import com.dayday.up.costom.anotation.OldBoy;
import com.example.wrapspringbootstarter.service.WrapService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 2020-06-01
 * Time: 22:58
 */
@RestController
public class TestAnotationController {

    @Resource
    WrapService test;

    @GetMapping("/testAnotionOne")
    @OldBoy
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        System.out.println(" controller oldboy is coming");
        return String.format("Hello %s!", name);
    }

    @GetMapping("/testAnotionOne")
    @OldBoy
    public String wrap(@RequestParam(value = "name", defaultValue = "World") String name) {
        System.out.println(" controller oldboy is coming");

      String returnValue=  test.wrap("  nishuona???  ");

        return String.format("Hello %s!", returnValue);
    }

}
