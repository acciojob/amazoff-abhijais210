package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    OrderRepository or;
    //add Order
    public void addOrder(Order order){
        or.addOrder(order);
    }
    //add partner
    public void addPartner(String id){
        //now we will create a partner with this id
        DeliveryPartner partner = new DeliveryPartner(id);
        or.addPartner(partner);
    }
    //order partner pair
    public void addOrderPartnerPair(String orderId,String partnerId){
        or.addOrderPartnerPair(orderId,partnerId);
    }
    //get ORder by order ID
    public Order getOrderById(String orderId){
        return or.getOrderById(orderId);
    }
    //get partner by ID
    public DeliveryPartner getPartnerById(String partnerId){
        return or.getPartnerById(partnerId);
    }
    //get total number of orders for a partner ID
    public Integer getOrderCountByPartnerId(String partnerId){
        return or.getOrderCountByPartnerId(partnerId);
    }
    //get List of Orders for Delivery Partner by Partner ID
    public List<String> getOrdersByPartnerId(String partnerId){
        return or.getOrdersByPartnerId(partnerId);
    }
    //get all the list of orders
    public List<String> getAllOrders(){
        return or.getAllOrders();
    }
    //get total numbers of Orders that have not been assigned yet to any of the delivery partners
    public Integer getCountOfUnassignedOrders(){
        return or.getCountOfUnassignedOrders();
    }
    //count of orders that are left not delivered after a particular time
    public Integer getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId){
        //now first we convert our time from String to Integer
        int HH = Integer.parseInt(time.substring(0,2));
        int MM = Integer.parseInt(time.substring(2));
        int intTime = (HH*60) + MM;

        return or.getOrdersLeftAfterGivenTimeByPartnerId(intTime,partnerId);
    }
    public String getLastDeliveryTimeByPartnerId(String partnerId){
        int time = or.getLastDeliveryTimeByPartnerId(partnerId);
        if(time == Integer.MIN_VALUE) return null;
        //now convert time from int to String
        int hh = time/60;
        int mm = time%60;
        String HH = Integer.toString(hh);
        String MM = Integer.toString(mm);
        if(hh >= 1 && hh <= 9){
            HH = "0" + HH;
        }
        if(mm >= 1 && mm <= 9){
            MM = "0" + MM;
        }
        return HH+":"+MM;
    }
    public void deletePartnerById(String partnerId){
        or.deletePartnerById(partnerId);
    }
    public void deleteOrderById(String orderId){
        or.deleteOrderById(orderId);
    }
}
