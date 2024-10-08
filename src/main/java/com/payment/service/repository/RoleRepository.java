package com.payment.service.repository;



import com.payment.service.enumerations.RoleName;
import com.payment.service.models.Role;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;


public interface RoleRepository extends CrudRepository<Role, Long> {


    Optional<Role> findByRoleName(RoleName roleName);

    @Query(nativeQuery = true, value = "select * from billpayment.role join billpayment.user_role on role.id = user_role.role_id where user_id =:userId")
    Set<Role> findAllUserRolesById(Long userId);

}
