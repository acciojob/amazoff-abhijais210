package com.driver;

import io.swagger.models.auth.In;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderRepository {
    public HashMap<String,Order> orderDb = new HashMap<>();
    public HashMap<String,DeliveryPartner> partnerDb = new HashMap<>();
    public HashMap<String, List<String>> orderPartnerPair = new HashMap<>();


    public void addOrder(Order order){
        String key = order.getId();
        orderDb.put(key,order);
    }
    public void addPartner(DeliveryPartner partner){
        String key = partner.getId();
        partnerDb.put(key,partner);
    }
    public void addOrderPartnerPair(String orderId,String partnerId){

        if(partnerDb.containsKey(partnerId) && orderDb.containsKey(orderId)){
            List<String> orders = orderPartnerPair.get(partnerId);
            if(orders == null)
                orders = new ArrayList<>();

            orders.add(orderId);
            orderPartnerPair.put(partnerId,orders);

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
            return orderPartnerPair.get(partnerId).size();
    }
    public List<String> getOrdersByPartnerId(String partnerId){
            return orderPartnerPair.get(partnerId);
    }
    public List<Order> getAllOrders(){
        return new ArrayList<>(orderDb.values());
    }
    public int getCountOfUnassignedOrders(){

      //now first we count all assigned Orders from OrderPartner Pair
        int assignOrders = 0;
        for(String partnerId : orderPartnerPair.keySet()){
            assignOrders += orderPartnerPair.get(partnerId).size();
        }
        //now from total Orders we will remove assigned orders now, we will be left with unsigned orders
        return orderDb.size()-assignOrders;
    }
    public int getOrdersLeftAfterGivenTimeByPartnerId(int intTime,String partnerId){
        int count = 0;
        for(String orderId : orderPartnerPair.get(partnerId)){
            if(getOrderById(orderId).getDeliveryTime() > intTime)
                count++;
        }
        return count;
    }
    public String getLastDeliveryTimeByPartnerId(String partnerId){
        int maxtime = 0;
        List<String> orders = orderPartnerPair.get(partnerId);
        for(String orderId : orders){
            if(getOrderById(orderId).getDeliveryTime() > maxtime)
                maxtime = getOrderById(orderId).getDeliveryTime();
        }
        String time = "";
        int hh = maxtime/60;
        int mm = maxtime%60;

        if(hh/10 == 0){
            time = time + "0"+String.valueOf(hh)+":";
        }
        else{
            time = time + String.valueOf(hh)+":";
        }

        if(mm/10 == 0){
            time = time + "0"+String.valueOf(mm);
        }
        else{
            time = time + String.valueOf(mm);
        }
        return time;
    }
    public void deletePartnerById(String partnerId){
        orderPartnerPair.remove(partnerId);
        partnerDb.remove(partnerId);
    }
    public void deleteOrderById(String orderId){

        for(Map.Entry<String,List<String>> map : orderPartnerPair.entrySet()){
            List<String> orders = map.getValue();
            String key = map.getKey();
            for(String oID : orders){
                if(oID.equals(orderId)){
                    orderPartnerPair.get(key).remove("orderId");
                    break;
                }
            }
        }
        orderDb.remove(orderId);
    }
}