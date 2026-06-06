package com.ShopSphere.e_commerce.Exception;

public class UserAlreadyExistsException extends RuntimeException {

    private final String resorceName;
    private final String fieldName;


    public UserAlreadyExistsException(String resorceName, String fieldName) {
        this.resorceName = resorceName;
        this.fieldName = fieldName;

    }

    @Override
    public String getMessage() {
        return resorceName + " With email " + fieldName + " " +  "Already Exists";
    }

}
