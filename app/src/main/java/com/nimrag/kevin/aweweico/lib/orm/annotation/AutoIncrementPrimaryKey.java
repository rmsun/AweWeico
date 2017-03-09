package com.nimrag.kevin.aweweico.lib.orm.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by sunkevin on 2017/3/9.
 *
 * 自增长主键
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoIncrementPrimaryKey {
    public String column() default "id";
}
