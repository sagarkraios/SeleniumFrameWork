package com.hm.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.hm.enums.CategoryType;
import com.hm.enums.DataType;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
@Documented
public @interface FrameworkAnnotation {

	public CategoryType[] category() default {};

	public DataType dataType() default DataType.EXCEL;

	public String dataSheetName() default "";

}
