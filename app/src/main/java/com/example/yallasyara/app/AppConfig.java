package com.example.yallasyara.app;

public class AppConfig {
    public static String ip="172.20.10.2";
    //192.168.1.111
    //172.20.10.2
    // Server user login url
    public static String URL_LOGIN = "http://"+ip+"/android_login_api/login.php";

    // Server user register url
    public static String URL_REGISTER = "http://"+ip+"/android_login_api/register.php";

    // Server cars url
    public static String URL_CARS = "http://"+ip+"/android_login_api/cars.php";

    // Server user register url
    public static String URL_REVIEWS = "http://"+ip+"/android_login_api/reviews.php";
}
