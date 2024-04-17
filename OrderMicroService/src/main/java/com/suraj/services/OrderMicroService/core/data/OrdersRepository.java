/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.suraj.services.OrderMicroService.core.data;

import org.springframework.data.jpa.repository.JpaRepository;

import com.suraj.services.OrderMicroService.core.events.OrderApprovedEvent;

public interface OrdersRepository extends JpaRepository <OrderEntity, String>{
    
	OrderEntity findByOrderId(String orderId);
}
