package com.alphaverse.inventorymanagementsystem.util;

import android.app.Application;

public class CurrentUserAPI extends Application {
    private String userId;
    private String userName;
    private String userStoreName;
    private String userStoreAddress;
    private String userPhoneNumber;
    private String userEmailAddress;
    private static CurrentUserAPI instance;

    public static CurrentUserAPI getInstance() {
        if (instance == null) {
            instance = new CurrentUserAPI();
        }
        return instance;
    }

    public CurrentUserAPI() {
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserEmailAddress() {
        return userEmailAddress;
    }

    public void setUserEmailAddress(String userEmailAddress) {
        this.userEmailAddress = userEmailAddress;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserStoreName() {
        return userStoreName;
    }

    public void setUserStoreName(String userStoreName) {
        this.userStoreName = userStoreName;
    }

    public String getUserStoreAddress() {
        return userStoreAddress;
    }

    public void setUserStoreAddress(String userStoreAddress) {
        this.userStoreAddress = userStoreAddress;
    }
}
