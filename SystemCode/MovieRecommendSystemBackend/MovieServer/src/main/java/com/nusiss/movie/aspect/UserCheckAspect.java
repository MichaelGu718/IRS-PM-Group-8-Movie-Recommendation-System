package com.nusiss.movie.aspect;

import com.nusiss.movie.annotation.UserCheck;
import com.nusiss.movie.utils.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Project: MovieRecommendSystem
 * Package: com.nusiss.movie.aspect
 * <p>
 * Created by tangyi on 2022-10-23 01:07
 * @author tangyi
 */
public class UserCheckAspect {
    @Pointcut("@annotation(com.nusiss.movie.annotation.UserCheck)")
    private void pointAll(){}

    @Around("pointAll() && @annotation(userCheck)")
    public Object before(ProceedingJoinPoint joinPoint, UserCheck userCheck) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        assert attributes != null;
        HttpServletRequest request = attributes.getRequest();
        String token = request.getHeader("cookies");
        // TO DO

        return Result.fail("No Access Permission");
    }
}
