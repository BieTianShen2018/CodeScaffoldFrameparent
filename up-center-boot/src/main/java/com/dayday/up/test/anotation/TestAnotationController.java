package com.dayday.up.test.anotation;

import com.dayday.up.costom.anotation.OldBoy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 2020-06-01
 * Time: 22:58
 * @author admin
 */
@RestController
public class TestAnotationController {

//    @Resource
//    WrapService wrapTest;


    @GetMapping("/testAnotionOne")
    @OldBoy
    public String hello(@RequestParam(value = "name", defaultValue = "World") String name) {
        System.out.println(" controller oldboy is coming");
        return String.format("Hello %s!", name);
    }

//    @GetMapping("/testMyStarter")
//    @OldBoy
//    public String wrap(@RequestParam(value = "name", defaultValue = "World") String name) {
//        System.out.println("wrap controller  is coming");
//        String returnValue = wrapTest.wrap("  ni shuo na :"+name);
//        return String.format("Hello %s!", returnValue);
//    }

}
