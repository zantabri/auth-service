package com.zantabri.auth_service.errors;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public final Object id;
    public final Class resourceType;

    public ResourceNotFoundException(Object id, Class<?> clazz) {
        super("resource with id " + id + " and of type " + clazz.getSimpleName() + " not found.");
        this.id = id;
        this.resourceType = clazz;
    }

}
