package com.codesoom.demo.infra;

import com.codesoom.demo.domain.Role;
import com.codesoom.demo.domain.RoleRepository;
import org.springframework.data.repository.CrudRepository;

public interface JpaRoleRepository extends RoleRepository, CrudRepository<Role, Long> {
}
