package com.example.onlineshopping.validation;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ResponseErrorValidation {

    public HttpEntity<?> mapValidationResult(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();

            List<ObjectError> allErrors = bindingResult.getAllErrors();
            if (!CollectionUtils.isEmpty(allErrors)) {
                for (ObjectError error : allErrors) {
                    errors.put(error.getCode(), error.getDefaultMessage());
                }
            }
            return ResponseEntity.badRequest().body(errors);

//            List<FieldError> errors = bindingResult.getFieldErrors();
//            for (FieldError error : errors) {
//                map.put(error.getField(), error.getDefaultMessage());
//            }
//            return ResponseEntity.badRequest().body(map);
        }
        return null;
    }
}
