package com.tnx.posBilling.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import com.tnx.posBilling.exceptions.ResourceNotFoundException;
import com.tnx.posBilling.model.CustomerGroup;
import com.tnx.posBilling.repository.CustomerGroupRepository;
import com.tnx.posBilling.service.interfaces.CustomerGroupService;

@Repository
public class CustomerGroupServiceImpl implements CustomerGroupService {
    @Autowired
    private CustomerGroupRepository customerGroupRepository;

    @Override
    public ResponseEntity<List<CustomerGroup>> getAllCustomerGroups() {
        return new ResponseEntity<>(customerGroupRepository.findAll(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CustomerGroup> getCustomerGroupById(Integer id) {
        CustomerGroup customerGroup = customerGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer group not found with id: "));
        return new ResponseEntity<>(customerGroup, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<CustomerGroup> createCustomerGroup(CustomerGroup customerGroup) {
        return new ResponseEntity<>(customerGroupRepository.save(customerGroup), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<CustomerGroup> updateCustomerGroup(Integer id, CustomerGroup updatedGroup) {
        CustomerGroup existingGroup = customerGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group is not found"));
        existingGroup.setGroupName(updatedGroup.getGroupName());
        existingGroup.setUsers(updatedGroup.getUsers());
        return new ResponseEntity<>(customerGroupRepository.save(existingGroup), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> deleteCustomerGroup(Integer id) {
        CustomerGroup existingGroup = customerGroupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group is not found"));
        customerGroupRepository.delete(existingGroup);
        return new ResponseEntity<>("Group deleted", HttpStatus.OK);
    }
}
