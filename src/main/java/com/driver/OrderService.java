package com.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrderRepository orderRepository;
    //add Order
    public void addOrder(Order order){
        orderRepository.addOrder(order);
    }
    //add partner
    public void addPartner(String id){
        //now we will create a partner with this id
        DeliveryPartner partner = new DeliveryPartner(id);
        orderRepository.addPartner(partner);
    }
    //order partner pair
    public void addOrderPartnerPair(String orderId,String partnerId){
        orderRepository.addOrderPartnerPair(orderId,partnerId);
    }
    //get ORder by order ID
    public Order getOrderById(String orderId){
        return orderRepository.getOrderById(orderId);
    }
    //get partner by ID
    public DeliveryPartner getPartnerById(String partnerId){
        return orderRepository.getPartnerById(partnerId);
    }
    //get total number of orders for a partner ID
    public int getOrderCountByPartnerId(String partnerId){
        return orderRepository.getOrderCountByPartnerId(partnerId);
    }
    //get List of Orders for Delivery Partner by Partner ID
    public List<String> getOrdersByPartnerId(String partnerId){
        return orderRepository.getOrdersByPartnerId(partnerId);
    }
    //get all the list of orders
    public List<String> getAllOrders(){
        return orderRepository.getAllOrders();
    }
    //get total numbers of Orders that have not been assigned yet to any of the delivery partners
    public int getCountOfUnassignedOrders(){
        return orderRepository.getCountOfUnassignedOrders();
    }
    //count of orders that are left not delivered after a particular time
    public int getOrdersLeftAfterGivenTimeByPartnerId(String time,String partnerId){
        //now first we convert our time from String to Integer
        String[] arr = time.split(":");
        int intTime = Integer.parseInt(arr[0])*60 + Integer.parseInt(arr[1]);

        return orderRepository.getOrdersLeftAfterGivenTimeByPartnerId(intTime,partnerId);
    }
    public String getLastDeliveryTimeByPartnerId(String partnerId){
        return orderRepository.getLastDeliveryTimeByPartnerId(partnerId);
    }
    public void deletePartnerById(String partnerId){
        orderRepository.deletePartnerById(partnerId);
    }
    public void deleteOrderById(String orderId){
        orderRepository.deleteOrderById(orderId);
    }
}