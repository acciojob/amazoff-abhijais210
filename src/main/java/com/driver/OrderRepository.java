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
    public HashMap<String,String> assignedOrderDb = new HashMap<>();


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
            return orderPartnerPair.get(partnerId).size();
    }
    public List<String> getOrdersByPartnerId(String partnerId){
            return orderPartnerPair.get(partnerId);
    }
    public List<String> getAllOrders(){
        return new ArrayList<>(orderDb.keySet());
    }
    public int getCountOfUnassignedOrders(){

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
        //now remove partner from assigned order MAP
        List<String> orderId = new ArrayList<>();
        for(String oID : assignedOrderDb.keySet()){
            if(assignedOrderDb.get(oID).equals(partnerId))
                orderId.add(oID);
        }
        //now traverse the OrderId List and compare with assignOrderDB key set
        for(String oID : orderId){
            assignedOrderDb.remove(oID);
        }
        orderPartnerPair.remove(partnerId);
        partnerDb.remove(partnerId);
    }
    public void deleteOrderById(String orderId){
        //before removing order from assignedOrderDb , store partner for that order
        String partnerId = assignedOrderDb.get(orderId);
        for(String oId : orderPartnerPair.get(partnerId)){
            if(oId.equals(orderId)){
                orderPartnerPair.get(partnerId).remove("orderId");
                //now update the order number for that partner also
                int orderCount = getPartnerById(partnerId).getNumberOfOrders();
                getPartnerById(partnerId).setNumberOfOrders(orderCount-1);
            }
        }
        orderDb.remove(orderId);
        assignedOrderDb.remove(orderId);
    }
}