package com.litosh.ilya.handtrainer;

/**
 * Created by ilya_ on 17.03.2018.
 */

public class User {

    private static long id;
    private static int activity;
    private static long userId;
    private static String userLogin;
    private static int wholeCountRotations;
    private static int sessionCountRotations;
    private static int finishedCountRotations;
    private static int currentDuration;
    private static String currentDate;

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

    public static int getWholeCountRotations() {
        return wholeCountRotations;
    }

    public static void setWholeCountRotations(int wholeCountRotations) {
        User.wholeCountRotations = wholeCountRotations;
    }

    public static int getSessionCountRotations() {
        return sessionCountRotations;
    }

    public static void setSessionCountRotations(int sessionCountRotations) {
        User.sessionCountRotations = sessionCountRotations;
    }

    public static int getFinishedCountRotations() {
        return finishedCountRotations;
    }

    public static void setFinishedCountRotations(int finishedCountRotations) {
        User.finishedCountRotations = finishedCountRotations;
    }

    public static int getCurrentDuration() {
        return currentDuration;
    }

    public static void setCurrentDuration(int currentDuration) {
        User.currentDuration = currentDuration;
    }

    public static String getCurrentDate() {
        return currentDate;
    }

    public static void setCurrentDate(String currentDate) {
        User.currentDate = currentDate;
    }
}
