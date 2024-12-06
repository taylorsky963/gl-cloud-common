package com.gl.myparadise.common.tools.annotation;

import com.gl.myparadise.common.bean.model.ArgsModel;
import com.gl.myparadise.common.tools.bean.BeanMapUtil;
import com.gl.myparadise.common.tools.bean.BeanUtil;
import com.gl.myparadise.common.tools.util.ListUtil;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Vincent
 * @Description: TODO
 * @ClassName: AnnotationUtil
 * @ProjectName: myparadise-common
 * @Date 2023-05-19 - 20:36
 */
public class AnnotationUtil {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationUtil.class);

    private final static String CacheKeyPattern = "\\$\\{(.+?)\\}";

    /**
     * 将参数中的${参数名}替换为对应的值
     * @param argsMultipleName
     * @param methodArgsMap
     * @return
     */
    public static Map<String, Object> getArgsSwitchValueMap(String argsMultipleName, Map<String, Object> methodArgsMap) {

        logger.debug("getArgsSwitchValueMap argsMultiple value : " + argsMultipleName);

        if(methodArgsMap != null){

            Map<String, Object> argsMap = new HashMap<String, Object>();

            Pattern pattern = Pattern.compile(CacheKeyPattern);

            Matcher matcher = pattern.matcher(argsMultipleName);

            while (matcher.find())
            {
                String mapkey = matcher.group(1);

                if(!StringUtils.isEmpty(mapkey)){

                    ArgsModel argsModel = getMethodArgsValue(mapkey, methodArgsMap);

                    if(argsModel != null && argsModel.getArgValue() != null && StringUtils.isNotEmpty(argsModel.getArgName())){

                        logger.debug("getArgsSwitchValueMap methodArgsMap value : " + argsModel.getArgValue());

                        logger.debug("getArgsSwitchValueMap methodArgsMap name : " + argsModel.getArgName());

                        argsMap.put(argsModel.getArgName(), argsModel.getArgValue());

                    }

                }else{

                    logger.error("getArgsSwitchValueMap  argsMultiple format is error");

                }

            }

            logger.debug("getArgsSwitchValueMap return argsMultiple is : " + argsMap.toString());

            return argsMap;

        }else{

            logger.debug("getArgsSwitchValueMap return argsMultiple is : " + argsMultipleName);

            return null;

        }

    }

    public static ArgsModel getMethodArgsValue(String argsName, Map<String, Object> methodArgsMap){

        String[] argSplitArray = argsName.split("\\.");

        ArgsModel argsModel = new ArgsModel();

        if(argSplitArray != null && argSplitArray.length > 1){

            return getArgsBeanValue(argSplitArray, methodArgsMap.get(argSplitArray[0]), 1);

        }else{

            argsModel.setArgName(argsName);

            argsModel.setArgValue(methodArgsMap.get(argsName));

            return argsModel;

        }

    }

    private static ArgsModel getArgsBeanValue(String[] argSplitArray, Object methodArgsBean, int index){

        if(index < argSplitArray.length - 1) {

            return getArgsBeanValue(argSplitArray, getValueByPropertie(methodArgsBean, argSplitArray[index]), index + 1);

        }else {

            ArgsModel argsModel = new ArgsModel();

            argsModel.setArgName(argSplitArray[index]);

            argsModel.setArgValue(getValueByPropertie(methodArgsBean, argSplitArray[index]));

            return argsModel;

        }

    }

    public static Map<String, Object> getMethodNameAndValueMap(JoinPoint joinPoint) {

        Map<String, Object> param = new HashMap<>();

        Object[] paramValues = joinPoint.getArgs();

        String[] paramNames = getMethodSignature(joinPoint).getParameterNames();

        for (int i = 0; i < paramNames.length; i++) {

            if (!ListUtil.isCollection(paramValues[i])) {

                if(paramValues[i].getClass().isPrimitive()){

                    param.put(paramNames[i], paramValues[i]);

                }else{

                    try {

                        param.putAll(BeanMapUtil.beanToMapNotNull(paramValues[i], false));

                    } catch (IllegalAccessException e) {

                        logger.error("getMethodNameAndValueMap is error", e);

                    }

                }

            }

        }

        return param;
    }

    public static MethodSignature getMethodSignature(JoinPoint joinPoint) {

        return (MethodSignature) joinPoint.getSignature();

    }

    /**
     * 根据设置的字段名从返回值中取值
     * @param obj
     * @param propertieName
     * @return
     */
    private static Object getValueByPropertie(Object obj, String propertieName) {

        logger.debug("getValueByPropertie propertieName : " + propertieName);

        Object propertieValue = null;

        try {

            Class userCla = (Class) obj.getClass();

            logger.debug("getValueByPropertie obj name : " + userCla.getName());

            List<Field> fs = BeanUtil.getAllFieldsList(userCla);

            for (int i = 0; i < fs.size(); i++) {

                Field f = fs.get(i);

                f.setAccessible(true);

                if (f.getName().endsWith(propertieName)) {

                    logger.debug("getValueByPropertie propertieName value = " + f.get(obj));

                    propertieValue = f.get(obj);

                    break;

                }

            }

        } catch (Exception e) {

            logger.error("getValueByPropertie is error :", e);

        }

        return propertieValue;
    }
}
