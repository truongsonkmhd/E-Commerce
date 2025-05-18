package vn.feature.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.feature.dtos.OrderDetailDTO;
import vn.feature.exception.payload.DataNotFoundException;
import vn.feature.model.Order;
import vn.feature.model.OrderDetail;
import vn.feature.model.Product;
import vn.feature.repositorys.OrderDetailRepository;
import vn.feature.repositorys.OrderRepository;
import vn.feature.repositorys.ProductRepository;
import vn.feature.service.OrderDetailService;
import vn.feature.util.MessageKeys;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl  implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderDetail createOrderDetail(OrderDetailDTO orderDetailDTO) throws DataNotFoundException {
        // tìm xem orderId có tồn tại hay không
        Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException((MessageKeys.NOT_FOUND+ " "+ orderDetailDTO.getOrderId())));
        Product product = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException((MessageKeys.NOT_FOUND+ " "+ orderDetailDTO.getProductId())));

        OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .numberOfProducts(orderDetailDTO.getNumberOfProducts())
                .totalMoney(orderDetailDTO.getTotalMoney())
                .price(orderDetailDTO.getPrice())
                .color(orderDetailDTO.getColor())
                .build();

        // lưu vào DB
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail getOrderDetail(Long id) throws DataNotFoundException {
        return orderDetailRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException((MessageKeys.NOT_FOUND+ " "+ id)));
    }

    @Override
    public OrderDetail updateOrderDetail(Long id, OrderDetailDTO orderDetailDTO) {
        // tìm xem orderDetail có tồn tại hay không
        OrderDetail existsOrderDetail = getOrderDetail(id);
        Order existsOrder = orderRepository.findById(orderDetailDTO.getOrderId())
                .orElseThrow(() -> new DataNotFoundException((MessageKeys.NOT_FOUND + " "+ orderDetailDTO.getOrderId())));
        Product existsProduct = productRepository.findById(orderDetailDTO.getProductId())
                .orElseThrow(() -> new DataNotFoundException((MessageKeys.NOT_FOUND + " "+ orderDetailDTO.getProductId())));

        existsOrderDetail.setProduct(existsProduct);
        existsOrderDetail.setNumberOfProducts(orderDetailDTO.getNumberOfProducts());
        existsOrderDetail.setTotalMoney(orderDetailDTO.getTotalMoney());
        existsOrderDetail.setPrice(orderDetailDTO.getPrice());
        existsOrderDetail.setColor(orderDetailDTO.getColor());
        existsOrderDetail.setId(id);
        existsOrderDetail.setOrder(existsOrder);
        return orderDetailRepository.save(existsOrderDetail);
    }

    @Override
    public void deleteOrderDetail(Long id) {
        orderDetailRepository.deleteById(id);
    }

    @Override
    public List<OrderDetail> findByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderId(orderId);
    }


}
