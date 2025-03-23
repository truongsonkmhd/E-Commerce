package vn.feature.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.feature.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {

}
