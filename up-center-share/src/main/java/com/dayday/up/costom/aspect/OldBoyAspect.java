package com.dayday.up.costom.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.lang.reflect.Modifier;

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

        String name=joinPoint.getSignature().getName();
        System.out.println("Around Begin ###"+name);
        joinPoint.proceed();//执行到这里开始走进来的方法体（必须声明）
        System.out.println("Around End  ###"+name);
    }

    //当想获得注解里面的属性，可以直接注入改注解
    //方法可以带参数，可以同时设置多个方法用&&
    @Before("oldboy()")
    public void before(JoinPoint joinPoint) {
        System.out.println("Before anotation oldboy()");
        System.out.println("目标方法名为:" + joinPoint.getSignature().getName());
        System.out.println("目标方法所属类的简单类名:" +        joinPoint.getSignature().getDeclaringType().getSimpleName());
        System.out.println("目标方法所属类的类名:" + joinPoint.getSignature().getDeclaringTypeName());
        System.out.println("目标方法声明类型:" + Modifier.toString(joinPoint.getSignature().getModifiers()));
        //获取传入目标方法的参数
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            System.out.println("第" + (i+1) + "个参数为:" + args[i]);
        }
        System.out.println("被代理的对象:" + joinPoint.getTarget());
        System.out.println("代理对象自己:" + joinPoint.getThis());
    }

    @After("oldboy()")
    public void after() {
        System.out.println("After anotation  oldboy()");
    }
}
