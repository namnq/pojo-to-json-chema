package com.ascendmoney.ami.scanner;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
@Retention(RetentionPolicy.RUNTIME)
public @interface ToJsonSchema {
    public String path() default "/tmp/ascend/";
}
