/*
package com.cognizant.authapi.base.services;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.CONSTRUCTOR, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = WhiteListValidator.class)
public @interface WhiteListed {

    String value() default "url";

    String type() default "";

    String message() default "whitelist error - invalid %s - '%s'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}*/
