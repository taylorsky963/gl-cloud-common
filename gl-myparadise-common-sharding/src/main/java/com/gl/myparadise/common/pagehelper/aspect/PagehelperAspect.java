package com.gl.myparadise.common.pagehelper.aspect;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.gl.myparadise.common.bean.model.PageListModel;
import com.gl.myparadise.common.pagehelper.annotation.PageQuery;
import com.gl.myparadise.common.tools.annotation.AnnotationUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: PagehelperAspect
 * @ProjectName: myparadise-common
 * @Date 2023-05-19 - 20:05
 */
@Aspect
@Component
public class PagehelperAspect {

    private static final Logger logger = LoggerFactory.getLogger(PagehelperAspect.class);


    @Around("@annotation(pageQuery)")
    public Object pagingQuery(ProceedingJoinPoint joinPoint, PageQuery pageQuery) throws Throwable {

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Class<?> returnType = signature.getMethod().getReturnType();

        if (returnType == PageListModel.class) {

            Object pageNum = AnnotationUtil.getMethodArgsValue(pageQuery.pageNumParaName(), AnnotationUtil.getMethodNameAndValueMap(joinPoint)).getArgValue();

            Object pageSize = AnnotationUtil.getMethodArgsValue(pageQuery.pageSizeParaName(), AnnotationUtil.getMethodNameAndValueMap(joinPoint)).getArgValue();

            Object isSqlCount = AnnotationUtil.getMethodArgsValue(pageQuery.isSqlCount(), AnnotationUtil.getMethodNameAndValueMap(joinPoint)).getArgValue();

            if(isSqlCount == null){

                isSqlCount = new Boolean(true);

            }

            if (pageNum != null && pageSize != null) {

                Page<Object> page = null;

                PageListModel<?> pageListModel = null;

                try {

                    page = PageHelper.startPage((Integer) pageNum, (Integer) pageSize, (Boolean) isSqlCount);

                    pageListModel = (PageListModel<?>) joinPoint.proceed();

                    pageListModel.setPageNum(page.getPageNum());

                    pageListModel.setPages(page.getPages());

                    pageListModel.setPageSize(page.getPageSize());

                    pageListModel.setPageTotal(page.getTotal());

                    pageListModel.setSqlCount((Boolean) isSqlCount);

                } finally {//保证线程变量被清除

                    if (PageHelper.getLocalPage() != null || page != null) {

                        PageHelper.clearPage();

                    }

                }

                return pageListModel;

            }
        }

        return joinPoint.proceed();
    }
}
