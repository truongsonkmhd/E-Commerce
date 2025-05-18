package vn.feature.service.impl;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.feature.dtos.CartItemDTO;
import vn.feature.dtos.OrderDTO;
import vn.feature.exception.payload.DataNotFoundException;
import vn.feature.model.*;
import vn.feature.repositorys.OrderDetailRepository;
import vn.feature.repositorys.OrderRepository;
import vn.feature.repositorys.ProductRepository;
import vn.feature.repositorys.UserRepository;
import vn.feature.response.order.OrderResponse;
import vn.feature.service.OrderService;
import vn.feature.util.MessageKeys;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl
        implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Order createOrder(OrderDTO orderDTO) throws DataNotFoundException {
        // Kiểm tra xem userId có tồn tại hay không
        User user = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException((MessageKeys.NOT_FOUND+ " "+orderDTO.getUserId())));

        // Convert orderDTO -> order
        // Sử dụng modelMapper
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));

        // Cập nhật các trường của đơn hàng từ orderDTO
        Order order = new Order();
        modelMapper.map(orderDTO, order);
        order.setUser(user);
        order.setOrderDate(new Date()); // Lấy thời điểm hiện tại
        order.setStatus(OrderStatus.PENDING);

        // Kiểm tra shipping date >= ngày hôm nay
        LocalDate shippingDate = orderDTO.getShippingDate() == null
                ? LocalDate.now()
                : orderDTO.getShippingDate();
        if (shippingDate.isBefore(LocalDate.now())) {
            throw new DataNotFoundException((MessageKeys.TOKEN_EXPIRATION_TIME));
        }
        order.setShippingDate(shippingDate); // Set thời điểm giao hàng
        order.setActive(true); // Trạng thái đơn hàng đã được active
        order.setTotalMoney(orderDTO.getTotalMoney());
        orderRepository.save(order); // Lưu vào database

        // Tạo danh sách các đối tượng orderDetails
        List<OrderDetail> orderDetails = new ArrayList<>();
        for (CartItemDTO cartItemDTO : orderDTO.getCartItems()) {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrder(order);

            // Lấy thông tin sản phẩm từ cartItemDTO
            Long productId = cartItemDTO.getProductId();
            int quantity = cartItemDTO.getQuantity();

            // Tìm thông tin sản phẩm từ cơ sở dữ liệu
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new DataNotFoundException(
                            (MessageKeys.PRODUCT_NOT_FOUND + " "+ productId))
                    );

            // Đặt thông tin cho orderDetails
            orderDetail.setProduct(product);
            orderDetail.setNumberOfProducts(quantity);
            orderDetail.setPrice(product.getPrice());
            // Tính total_money và kiểm tra giới hạn
            float totalMoney = product.getPrice() * quantity;
            if (totalMoney > 99999999.99) {
                throw new DataNotFoundException("Total money for product " + productId + " exceeds the limit of 99999999.99");
            }
            orderDetail.setTotalMoney(totalMoney);

            // Thêm orderDetails vào danh sách
            orderDetails.add(orderDetail);
        }

        // Lưu danh sách OrderDetails vào cơ sở dữ liệu
        orderDetailRepository.saveAll(orderDetails);
        return order;
    }

    @Override
    public Order getOrderById(Long id) {
        return orderRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Order updateOrder(Long id, OrderDTO orderDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException((MessageKeys.NOT_FOUND+ " "+ id)));
        User existsUser = userRepository.findById(orderDTO.getUserId())
                .orElseThrow(() -> new DataNotFoundException((MessageKeys.NOT_FOUND+ " "+ orderDTO.getUserId())));

        // tạo một luồng ánh xạ
        modelMapper.typeMap(OrderDTO.class, Order.class)
                .addMappings(mapper -> mapper.skip(Order::setId));
        // cập nhật các trường của đơn hàng từ orderDTO
        modelMapper.map(orderDTO, order);
        order.setUser(existsUser);
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(Long id) {
        // xoá mềm, không xoá cứng bản ghi trong DB
        // no hard-delete, please soft-delete
        Order order = orderRepository.findById(id).orElse(null);
        if (order != null) {
            order.setActive(false);
            orderRepository.save(order);
        }
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Page<OrderResponse> findByKeyword(String keyword, Pageable pageable) {
        // lấy danh sách sản phẩm theo trang(page) và giới hạn(limit)
        Page<Order> orderPage;
        orderPage = orderRepository.findByKeyword(keyword, pageable);
        return orderPage.map(OrderResponse::fromOrder);
    }

}
