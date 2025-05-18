package vn.feature.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.feature.dtos.OrderDTO;
import vn.feature.model.Order;
import vn.feature.response.order.OrderResponse;

import java.util.List;

public interface OrderService {
    Order createOrder(OrderDTO orderDTO) ;
    Order getOrderById(Long id);
    Order updateOrder(Long id, OrderDTO orderDTO);
    void deleteOrder(Long id);
    List<Order> findByUserId(Long userId);
    Page<OrderResponse> findByKeyword(String keyword, Pageable pageable);

}
