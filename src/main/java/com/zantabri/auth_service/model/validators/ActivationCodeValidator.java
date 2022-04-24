package com.zantabri.auth_service.model.validators;

import com.zantabri.auth_service.model.ActivationCode;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class ActivationCodeValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return ActivationCode.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

       ActivationCode activationCode = (ActivationCode) target;

       if (activationCode.getCode() == null) {
           errors.reject("codeNull","null value");

       } else if (activationCode.getCode().isEmpty() || activationCode.getCode().isBlank()) {
           errors.reject("codeEmptyOrBlank","code is empty or blank");
       }

       if (activationCode.getExpires() == null) {
           errors.reject("expiresNull", "expires is null");
       }

    }
}
