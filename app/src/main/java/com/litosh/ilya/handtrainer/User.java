package com.litosh.ilya.handtrainer;

/**
 * Created by ilya_ on 17.03.2018.
 */

public class User {

    private static long id;
    private static int activity;
    private static long userId;
    private static String userLogin;

    public static long getId() {
        return id;
    }

    public static void setId(long id) {
        User.id = id;
    }

    public static int getActivity() {
        return activity;
    }

    public static void setActivity(int activity) {
        User.activity = activity;
    }

    public static long getUserId() {
        return userId;
    }

    public static void setUserId(long userId) {
        User.userId = userId;
    }

    public static String getUserLogin() {
        return userLogin;
    }

    public static void setUserLogin(String userLogin) {
        User.userLogin = userLogin;
    }
}
