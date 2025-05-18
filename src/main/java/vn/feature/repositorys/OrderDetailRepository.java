package vn.feature.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.feature.model.OrderDetail;

import java.util.List;

public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    List<OrderDetail> findByOrderId(Long orderId);
}