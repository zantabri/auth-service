package com.zantabri.auth_service.errors;

public class ResourceNotFoundException extends RuntimeException {

    public final Object id;
    public final Class resourceType;

    public ResourceNotFoundException(Object id, Class<?> clazz) {
        super("resource with id " + id + " and of type " + clazz.getSimpleName() + " not found.");
        this.id = id;
        this.resourceType = clazz;
    }

}
