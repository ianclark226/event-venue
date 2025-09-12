package com.ianclark226.event_venue.service;

import com.ianclark226.event_venue.model.Role;
import com.ianclark226.event_venue.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IRoleService {

    List<Role> getRoles();

    Role createRole(Role theRole);

    void deleteRole(Long id);

    Role findByName(String name);

    User removeUserFromRole(Long userId, Long roleId);

    User assignRoleToUser(Long userId, Long roleId);

    Role removeAllUsersFromRole(Long roleId);
}
