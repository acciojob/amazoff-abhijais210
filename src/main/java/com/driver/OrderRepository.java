package com.driver;

import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository
public class OrderRepository {
    private HashMap<String,Order> orderDb;
    private HashMap<String,DeliveryPartner> partnerDb;
    private HashMap<String, List<String>> orderPartnerPair;
    private HashMap<String,String> assignedOrderDb;

    public OrderRepository() {
        this.orderDb = new HashMap<>();
        this.partnerDb = new HashMap<>();
        this.orderPartnerPair = new HashMap<>();
        this.assignedOrderDb = new HashMap<>();
    }
    public void addOrder(Order order){
        String key = order.getId();
        orderDb.put(key,order);
    }
    public void addPartner(DeliveryPartner partner){
        String key = partner.getId();
        partnerDb.put(key,partner);
    }
    public void addOrderPartnerPair(String orderId,String partnerId){

        if(partnerDb.containsKey(partnerId) && orderDb.containsKey(orderId) && !assignedOrderDb.containsKey(orderId)){
            List<String> orders = orderPartnerPair.get(partnerId);
            if(orders == null)
                orders = new ArrayList<>();

            orders.add(orderId);
            orderPartnerPair.put(partnerId,orders);
            assignedOrderDb.put(orderId,partnerId);

            //we will also update the number of orders for that Delivery partner

            DeliveryPartner deliveryPartner = getPartnerById(partnerId);
            int totalOrder = deliveryPartner.getNumberOfOrders();
            deliveryPartner.setNumberOfOrders(totalOrder+1);
        }
    }
    public Order getOrderById(String orderId){
        return orderDb.get(orderId);
    }
    public DeliveryPartner getPartnerById(String partnerId){
        return partnerDb.get(partnerId);
    }
    public Integer getOrderCountByPartnerId(String partnerId){
        if(orderPartnerPair.containsKey(partnerId))
            return orderPartnerPair.get(partnerId).size();

        return null;
    }
    public List<String> getOrdersByPartnerId(String partnerId){
        if(orderPartnerPair.containsKey(partnerId))
            return orderPartnerPair.get(partnerId);

        return null;
    }
    public List<String> getAllOrders(){
        return new ArrayList<>(orderDb.keySet());
    }
    public Integer getCountOfUnassignedOrders(){

      //now first we count all assigned Orders from OrderPartner Pair
       /* Integer assignOrders = 0;
        for(String partnerId : orderPartnerPair.keySet()){
            assignOrders += orderPartnerPair.get(partnerId).size();
        }
        //now from total Orders we will remove assigned orders now, we will be left with unsigned orders
        return orderDb.size()-assignOrders; */

        //now a little more Optimal way
        return orderDb.size() - assignedOrderDb.size();
    }
    public Integer getOrdersLeftAfterGivenTimeByPartnerId(int intTime,String partnerId){
        Integer count = null;
        for(String orderId : orderPartnerPair.get(partnerId)){
            if(getOrderById(orderId).getDeliveryTime() > intTime)
                count++;
        }
        return count;
    }
    public int getLastDeliveryTimeByPartnerId(String partnerId){
        int time = Integer.MIN_VALUE;
        for(String orderId : orderPartnerPair.get(partnerId)){
            if(getOrderById(orderId).getDeliveryTime() > time)
                time = getOrderById(orderId).getDeliveryTime();
        }
        return time;
    }
    public void deletePartnerById(String partnerId){
        orderPartnerPair.remove(partnerId);
        partnerDb.remove(partnerId);
        //now remove partner from assigned order MAP
        for(String orderID : assignedOrderDb.keySet()){
            String pId = assignedOrderDb.get(orderID);
            if(partnerId.equals(pId))
                assignedOrderDb.remove(orderID);
        }
    }
    public void deleteOrderById(String orderId){
        //before removing order from assignedOrderDb , store partner for that order
        String partnerId = assignedOrderDb.get(orderId);
        for(String oId : orderPartnerPair.get(partnerId)){
            if(oId.equals(orderId)){
                orderPartnerPair.get(partnerId).remove(orderId);
                //now update the order number for that partner also
                int orderCount = getPartnerById(partnerId).getNumberOfOrders();
                getPartnerById(partnerId).setNumberOfOrders(orderCount-1);
            }
        }
        orderDb.remove(orderId);
        assignedOrderDb.remove(orderId);
    }
}