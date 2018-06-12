package com.shiro.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.shiro.exception.ParamException;
import com.shiro.exception.PermissionException;
import org.apache.commons.collections.MapUtils;

import javax.validation.*;
;
import java.util.*;

/**
 * @Author: Mo
 * @Date: Created in  2018/6/4 9:23
 */
public class BeanVaildator {

    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    // 校验类
    public static <T>Map<String, String> validate(T t, Class... groups) {
        Validator validator = validatorFactory.getValidator();
        Set validatorResult = validator.validate(t, groups);

        if (validatorResult.isEmpty()) {
            return Collections.emptyMap();
        } else {
            LinkedHashMap error = new LinkedHashMap();
            Iterator iterator = validatorResult.iterator();

            while (iterator.hasNext()) {
                ConstraintViolation violation = (ConstraintViolation)iterator.next();
                error.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return error;
        }

    }

    // 校验列表
    public static Map<String, String> validateList(Collection<?> collection) {
        Preconditions.checkNotNull(collection);
        Iterator iterator = collection.iterator();
        Map errors;

        do {
            if (!iterator.hasNext()) {
                return Collections.EMPTY_MAP;
            }
            Object object = iterator.next();
            errors = validate(object, new Class[0]);

        } while (errors.isEmpty());

       return errors;
    }


    public static Map<String, String> validateObject(Object first, Object... objects) {
        if (objects != null && objects.length > 0) {
            return validateList(Lists.asList(first, objects));
        } else {
            return validate(first, new Class[0]);
        }
    }


    public static void check(Object param) throws ParamException {
        Map<String, String> map = BeanVaildator.validateObject(param);
        if (MapUtils.isNotEmpty(map)) {
            throw new PermissionException(map.toString());
        }
    }

}
