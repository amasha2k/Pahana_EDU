package org.example.pahanaedu;

public class Routes {

    //Base API
    public static final String HOME_BASE = "/api";
    public static final String AUTH_BASE = HOME_BASE + "/auth";
    public static final String CUSTOMER_BASE = HOME_BASE + "/customers";
    public static final String ITEM_BASE = HOME_BASE + "/items";
    public static final String ORDER_BASE = HOME_BASE + "/orders";
    public static final String DASHBOARD = HOME_BASE + "/dashboard";


    //Authentication Routes
    public static final String LOGIN = "/login";
    public static final String REGISTER = "/register";
    public static final String FORGOT_PASSWORD = "/forgot-password";
    public static final String RESET_PASSWORD = "/reset-password";
    public static final String DELETE_ACCOUNT = "/delete-account";
    public static final String LOGOUT = "/logout";

    //Customer routes
    public static final String UPDATE_CUSTOMER = "/{id}";
    public static final String DELETE_CUSTOMER = "/{id}";
    public static final String GET_LATEST = "/latest";

    //item routes
    public static final String UPDATE_ITEM = "/{id}";
    public static final String DELETE_ITEM = "/{id}";

    //orders routes
    public static final String UPDATE_ORDER = "/{id}";
    public static final String DELETE_ORDER = "/{id}";
    public static final String GETORDERBYID_ORDER = "/{id}";

    //Dashboard routes
    public static final String STATS = "/stats";
    public static final String ACTIVITY = "/activity";
} 