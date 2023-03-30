package com.driver;

public class Order {

    private String id;
    private int deliveryTime;
    public Order() {
    }
    public Order(String id, String deliveryTime) {

        // The deliveryTime has to converted from string to int and then stored in the attribute
        //deliveryTime  = HH*60 + MM
        if(deliveryTime.substring(0).equals(":")){
            this.deliveryTime = Integer.parseInt(deliveryTime.substring(1));
        }
        else if(deliveryTime.substring(1).equals(":")){
            int HH = Integer.parseInt(deliveryTime.substring(0,1));
            int MM = Integer.parseInt(deliveryTime.substring(2));
            this.deliveryTime = (HH*60) + MM;
        }
        else {
            int HH = Integer.parseInt(deliveryTime.substring(0,2));
            int MM = Integer.parseInt(deliveryTime.substring(3));
            this.deliveryTime = (HH*60) + MM;
        }
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(int deliveryTime) {
        this.deliveryTime = deliveryTime;
    }
}
