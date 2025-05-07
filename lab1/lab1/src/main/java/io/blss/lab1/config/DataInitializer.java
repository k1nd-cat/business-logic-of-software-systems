package io.blss.lab1.config;

import io.blss.lab1.entity.Permission;
import io.blss.lab1.entity.Role;
import io.blss.lab1.repository.PermissionRepository;
import io.blss.lab1.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public static final String PERM_MANAGE_CART = "MANAGE_CART";
    public static final String PERM_MANAGE_ORDERS = "MANAGE_ORDERS";
    public static final String PERM_MANAGE_PERSONAL_INFO = "MANAGE_PERSONAL_INFO";
    public static final String PERM_ADD_PRODUCT_TO_CART = "ADD_PRODUCT_TO_CART";

    // --- COURIER Permissions ---
    public static final String PERM_VIEW_AVAILABLE_ORDERS = "VIEW_AVAILABLE_ORDERS";
    public static final String PERM_ACCEPT_ORDER = "ACCEPT_ORDER";
    public static final String PERM_VIEW_ACCEPTED_ORDERS = "VIEW_ACCEPTED_ORDERS";
    public static final String PERM_COMPLETE_ORDER = "COMPLETE_ORDER";


    @Override
    public void run(String... args) throws Exception {
        Permission pManageCart = createPermissionIfNotFound(PERM_MANAGE_CART);
        Permission pManageOrders = createPermissionIfNotFound(PERM_MANAGE_ORDERS);
        Permission pManagePersonalInfo = createPermissionIfNotFound(PERM_MANAGE_PERSONAL_INFO);
        Permission pAddProductToCart = createPermissionIfNotFound(PERM_ADD_PRODUCT_TO_CART);

        Permission pViewAvailableOrders = createPermissionIfNotFound(PERM_VIEW_AVAILABLE_ORDERS);
        Permission pAcceptOrder = createPermissionIfNotFound(PERM_ACCEPT_ORDER);
        Permission pViewAcceptedOrders = createPermissionIfNotFound(PERM_VIEW_ACCEPTED_ORDERS);
        Permission pCompleteOrder = createPermissionIfNotFound(PERM_COMPLETE_ORDER);


        createRoleIfNotFound("USER_ROLE", new HashSet<>(Arrays.asList(
                pManageCart, pManageOrders, pManagePersonalInfo, pAddProductToCart
        )));

        createRoleIfNotFound("COURIER_ROLE", new HashSet<>(Arrays.asList(
                pViewAvailableOrders, pAcceptOrder, pViewAcceptedOrders, pCompleteOrder
        )));
    }

    private Permission createPermissionIfNotFound(String name) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> permissionRepository.save(Permission.builder().name(name).build()));
    }

    private Role createRoleIfNotFound(String name, Set<Permission> permissions) {
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    Role role = Role.builder().name(name).permissions(permissions).build();
                    return roleRepository.save(role);
                });
    }
}
