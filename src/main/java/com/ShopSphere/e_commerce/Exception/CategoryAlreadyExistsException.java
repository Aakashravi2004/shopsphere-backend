package com.ShopSphere.e_commerce.Exception;

public class CategoryAlreadyExistsException extends RuntimeException{

    private final String resourceName;
    private final String fieldName;

    public CategoryAlreadyExistsException(String resourceName, String fieldName) {
        this.resourceName = resourceName;
        this.fieldName = fieldName;

    }

    @Override
    public String getMessage() {
        return resourceName + " With Name '" + fieldName +  "' Already Exists";
    }
}

