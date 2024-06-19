/*
package com.cognizant.authapi.base.services;

import org.springframework.util.Assert;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class WhiteListValidator implements ConstraintValidator<WhiteListed, Object> {

    private WhiteListed it;

    @Override
    public void initialize(final WhiteListed constraintAnnotation) {
        this.it = constraintAnnotation;
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        WhiteListService whiteListService = BeanUtil.getBean(WhiteListService.class);
        Assert.notNull(whiteListService, "whiteListService must not be null");
        String type = it.type().isEmpty() ? it.value() : it.type();
        boolean isValid = whiteListService.isValid(type, String.valueOf(value));
        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(String.format(it.message(),type , value)).addConstraintViolation();
        }
        return isValid;
    }

}
*/
