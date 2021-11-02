package com.sally.order;

public class OrderServiceEndpoints {
    public static final String SERVICE_PREFIX = "/order-service";
    public static final String V1 = SERVICE_PREFIX + "/api/v1";
    public static final String ORDER = "/order";
    public static final String ORDER_BY_ID = ORDER + "/{id}";
    public static final String ORDER_CANCEL = ORDER_BY_ID + "/cancel";


    public static final String SAGA = ORDER + "/saga";
    public static final String AGGREGATE_BY_ID = ORDER + "/aggregate/{id}";
    public static final String AGGREGATE_EVENTS_BY_ID = AGGREGATE_BY_ID + "/events";

}
