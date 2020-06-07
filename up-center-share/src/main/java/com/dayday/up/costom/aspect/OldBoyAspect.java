package com.dayday.up.costom.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: admin
 * Date: 2020-06-01
 * Time: 22:54
 */
@Component
@Aspect
public class OldBoyAspect {

    @Pointcut("@annotation(com.dayday.up.costom.anotation.OldBoy)")
    private void oldboy() { }

    /**
     * 定制一个环绕通知
     * @param joinPoint
     */
    @Around("oldboy()")
    public void advice(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("Around Begin");
        joinPoint.proceed();//执行到这里开始走进来的方法体（必须声明）
        System.out.println("Around End");
    }

    //当想获得注解里面的属性，可以直接注入改注解
    //方法可以带参数，可以同时设置多个方法用&&
    @Before("oldboy()")
    public void record(JoinPoint joinPoint) {
        System.out.println("Before oldboy()");
    }

    @After("oldboy()")
    public void after() {
        System.out.println("After  oldboy()");
    }
}
