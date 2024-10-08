package com.payment.service.services;

import com.payment.service.models.Permission;
import com.payment.service.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Service
public class PermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    public Set<Permission> getTransactionPermissions() {
        List<String> descriptions = Arrays.asList("CREATE_TRANSACTION", "VIEW_TRANSACTION");
        return permissionRepository.findPermissionsByDescriptions(descriptions);
    }
}
