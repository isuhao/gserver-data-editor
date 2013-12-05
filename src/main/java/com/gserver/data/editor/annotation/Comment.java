package com.gserver.data.editor.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = {ElementType.FIELD, ElementType.PACKAGE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Comment {
	String desc() default "";
	String search() default "";
}
