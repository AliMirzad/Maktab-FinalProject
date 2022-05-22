package com.Maktab.Final.model.service;

import com.Maktab.Final.model.entity.Customer;
import com.Maktab.Final.model.entity.Order;
import com.Maktab.Final.model.entity.SubService;
import com.Maktab.Final.model.entity.enums.OrderStatus;
import com.Maktab.Final.model.repository.OrderRepository;
import com.Maktab.Final.model.service.serviceInterface.OrderServiceInterface;
import com.Maktab.Final.model.exception.LogicErrorException;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderService implements OrderServiceInterface {
    //---------------------------------------------------------needed
    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final SubServiceService subServiceService;

    public OrderService(OrderRepository orderRepository, @Lazy CustomerService customerService, SubServiceService subServiceService) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.subServiceService = subServiceService;
    }

    //---------------------------------------------------------methods
    @Override
    public Order findOrderById(Integer id) {
        Order order = orderRepository.findOrderById(id);
        if (order == null) throw new LogicErrorException("order not found");
        return order;
    }

    @Override
    public List<Order> findOrderByCustomer(String nationalCode) {
        Customer customer = customerService.findCustomerByNationalCode(nationalCode);
        if (customer == null)
            throw new LogicErrorException("customer order not found");
        List<Order> orders = orderRepository.findOrderByCustomer(customer);
        if (orders == null) throw new LogicErrorException("order list is empty");
        return orders;
    }

    @Override
    public List<Order> findOrderBySubServices(SubService subService) {
        if (subServiceService.findSubServiceById(subService.getId()) == null)
            throw new LogicErrorException("sub service order not found");
        List<Order> orders = orderRepository.findOrderBySubService(subService);
        if (orders == null) throw new LogicErrorException("order list is empty");
        return orders;
    }

    public void create(Order order, String nationalCode, String subServiceName) {
        Customer customer = customerService.findCustomerByNationalCode(nationalCode);
        if (customer == null) throw new LogicErrorException("customer order not found");
        SubService subService = subServiceService.findSubServiceByName(subServiceName);
        if (subService == null) throw new LogicErrorException("sub service order not found");
        order.setCustomer(customer);
        order.setSubService(subService);
        order.setId(null);
        if (order.getAddress() == null || order.getAddress().isEmpty())
            throw new LogicErrorException("address order can't be null/empty");
        if (order.getDescription() == null || order.getDescription().isEmpty())
            throw new LogicErrorException("description order can't be null/empty");
        if (order.getSuggestPrice() == null || order.getSuggestPrice() < 100) throw new LogicErrorException("suggest price order must be over 100 coin");
        if (order.getSubService() == null) throw new LogicErrorException("sub service order can't be null/empty");
        order.setOrderRegisterTime(LocalDateTime.now());
        order.setStatus(OrderStatus.new_req);
        orderRepository.save(order);
    }

    public void save(Order order) {
        orderRepository.save(order);
    }
}
