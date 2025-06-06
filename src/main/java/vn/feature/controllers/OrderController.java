package vn.feature.controllers;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import vn.feature.dtos.OrderDTO;
import vn.feature.model.Order;
import vn.feature.response.ApiResponse;
import vn.feature.response.order.OrderPageResponse;
import vn.feature.response.order.OrderResponse;
import vn.feature.service.OrderService;
import vn.feature.util.MessageKeys;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("")
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderDTO orderDTO, BindingResult  bindingResult){
        try{
            if (bindingResult.hasErrors()) {
                List<String> errorMessages = bindingResult.getFieldErrors()
                        .stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(
                        ApiResponse.builder()
                                .errors(errorMessages.stream()
                                        .toList()).build()
                );
            }
            Order order = orderService.createOrder(orderDTO);
            return ResponseEntity.ok().body(
                    ApiResponse.<Order>builder()
                            .success(true)
                           // .message(translate(MessageKeys.CREATE_ORDER_SUCCESS, order.getId()))
                            .payload(order)
                            .build()
            );
        } catch (Exception e){
            return ResponseEntity.badRequest().body(
                    ApiResponse.builder()
                            .error(e.getMessage())
                            //.message(translate(MessageKeys.CREATE_ORDER_FAILED))
                           .build()
            );
        }
    }

    /**
     * Lấy ra danh sách đơn hàng theo user_id
     **/

    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER')")
    @GetMapping("/user/{user_id}")
    public ResponseEntity<?> getOrders(@Valid @PathVariable("user_id") Long userId){
        try{
            List<Order> orders = orderService.findByUserId(userId);
            List<OrderResponse> orderResponses = OrderResponse.fromOrdersList(orders);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .payload(orderResponses)
                    .build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .error(e.getMessage())
                    .message((MessageKeys.GET_INFORMATION_FAILED)).build());
        }
    }

    /**
     * Lấy ra danh sách đơn hàng theo order_id
     **/
    @PreAuthorize("hasRole('ROLE_ADMIN') OR hasRole('ROLE_USER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrder(@Valid @PathVariable("id") Long orderId) {
        try {
            Order existsOrder = orderService.getOrderById(orderId);
            OrderResponse orderResponse = OrderResponse.fromOrder(existsOrder);
            return ResponseEntity.ok(ApiResponse.builder()
                    .success(true)
                    .payload(orderResponse).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .error(e.getMessage())
                    .message((MessageKeys.GET_INFORMATION_FAILED)).build()
            );
        }
    }

    /**
     *  Lấy ra tất cả các đơn hàng với quyền ADMIN
     **/
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/get-order-by-keyword")
    public ResponseEntity<OrderPageResponse> getOrderByKeyword(
            @RequestParam(defaultValue = "", required = false) String keyword,
            @RequestParam(defaultValue = "0", name = "page") int page,
            @RequestParam(defaultValue = "10", name = "limit") int limit
    ) {
        // tạo Pageable từ thông tin trang và giới hạn
        PageRequest pageRequest = PageRequest.of(
                page,
                limit,
                Sort.by("id").ascending()
        );
        Page<OrderResponse> orderPage = orderService.findByKeyword(keyword, pageRequest);
        List<OrderResponse> orders = orderPage.getContent();
        return ResponseEntity.ok(OrderPageResponse.builder()
                .orders(orders)
                .pageNumber(page)
                .totalElements(orderPage.getTotalElements())
                .pageSize(orderPage.getSize())
                .isLast(orderPage.isLast())
                .totalPages(orderPage.getTotalPages())
                .build());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateOrder(
            @Valid @PathVariable long id,
            @Valid @RequestBody OrderDTO orderDTO
    ) {
        try {
            Order order = orderService.updateOrder(id, orderDTO);
            return ResponseEntity.ok().body(ApiResponse.builder()
                    .success(true)
                    .message(MessageKeys.MESSAGE_UPDATE_GET + " " + order.getId())
                    .payload(order).build()
            );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.builder()
                    .error(e.getMessage())
                    .message((MessageKeys.GET_INFORMATION_FAILED)).build()
            );
        }
    }

}
