/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.suraj.services.OrderMicroService.query;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import com.suraj.services.OrderMicroService.core.data.OrderEntity;
import com.suraj.services.OrderMicroService.core.data.OrdersRepository;
import com.suraj.services.OrderMicroService.core.events.OrderApprovedEvent;
import com.suraj.services.OrderMicroService.core.events.OrderCreatedEvent;
import com.suraj.services.OrderMicroService.core.events.OrderRejectedEvent;

@Component
@ProcessingGroup("order-group")
public class OrderEventsHandler {
    
    private final OrdersRepository ordersRepository;
    
    public OrderEventsHandler(OrdersRepository ordersRepository) {
        this.ordersRepository = ordersRepository;
    }

    @EventHandler
    public void on(OrderCreatedEvent event) throws Exception {
        OrderEntity orderEntity = new OrderEntity();
        BeanUtils.copyProperties(event, orderEntity);
 
        this.ordersRepository.save(orderEntity);
    }
    
    @EventHandler
    public void on(OrderApprovedEvent event) throws Exception {
    	OrderEntity orderEntity = ordersRepository.findByOrderId(event.getOrderId());
    	orderEntity.setOrderId(event.getOrderId());
    	
       ordersRepository.save(orderEntity);
    }
    
    @EventHandler
    public void on(OrderRejectedEvent event) throws Exception {
    	OrderEntity orderEntity = ordersRepository.findByOrderId(event.getOrderId());
    	orderEntity.setOrderStatus(event.getOrderStatus());
    	
       ordersRepository.save(orderEntity);
    }
     
}
