package vn.feature.repositorys;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.feature.model.Order;
import vn.feature.util.ConfigSql;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // tìm đơn hàng của một user nào đó
    List<Order> findByUserId(Long userId);

    // lẩy ra tất cả các order
    @Query(ConfigSql.Order.GET_ALL_ORDER)
    Page<Order> findByKeyword(String keyword, Pageable pageable);
}
