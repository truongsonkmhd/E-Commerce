package vn.feature.response.order_detail;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import vn.feature.model.OrderDetail;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailResponse {
    private Long id;

    @JsonProperty("order_id")
    private Long orderId;

    @JsonProperty("product_id")
    private Long productId;

    private Float price;

    @JsonProperty("number_of_products")
    private Integer numberOfProducts;

    @JsonProperty("total_money")
    private Float totalMoney;

    private String color;

    public static OrderDetailResponse fromOrderDetail(OrderDetail orderDetail) {
        return OrderDetailResponse.builder()
                .id(orderDetail.getId())
                .orderId(orderDetail.getOrder().getId())
                .productId(orderDetail.getProduct().getId())
                .price(orderDetail.getPrice())
                .numberOfProducts(orderDetail.getNumberOfProducts())
                .totalMoney(orderDetail.getTotalMoney())
                .color(orderDetail.getColor())
                .build();
    }

    public static List<OrderDetailResponse> fromOrderDetailList(List<OrderDetail> orderDetailList) {
        return orderDetailList.stream()
                .map(orderDetail -> OrderDetailResponse.builder()
                        .id(orderDetail.getId())
                        .orderId(orderDetail.getOrder().getId())
                        .productId(orderDetail.getProduct().getId())
                        .price(orderDetail.getPrice())
                        .numberOfProducts(orderDetail.getNumberOfProducts())
                        .totalMoney(orderDetail.getTotalMoney())
                        .color(orderDetail.getColor())
                        .build()
                ).toList();
    }

}