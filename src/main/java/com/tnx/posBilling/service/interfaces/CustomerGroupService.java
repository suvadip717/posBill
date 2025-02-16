package com.tnx.posBilling.service.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.tnx.posBilling.model.CustomerGroup;

public interface CustomerGroupService {

    public ResponseEntity<List<CustomerGroup>> getAllCustomerGroups();

    public ResponseEntity<CustomerGroup> getCustomerGroupById(Integer id);

    public ResponseEntity<CustomerGroup> createCustomerGroup(CustomerGroup customerGroup);

    public ResponseEntity<CustomerGroup> updateCustomerGroup(Integer id, CustomerGroup updatedGroup);

    public ResponseEntity<String> deleteCustomerGroup(Integer id);
}
