package com.tnx.posBilling.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tnx.posBilling.model.CustomerGroup;
import com.tnx.posBilling.service.interfaces.CustomerGroupService;

@RestController
@CrossOrigin
@RequestMapping("/customer-groups")
public class CustomerGroupController {
    @Autowired
    private CustomerGroupService customerGroupService;

    @GetMapping
    public ResponseEntity<List<CustomerGroup>> getAllCustomerGroups() {
        return customerGroupService.getAllCustomerGroups();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerGroup> getCustomerGroupById(@PathVariable Integer id) {
        return customerGroupService.getCustomerGroupById(id);
    }

    @PostMapping
    public ResponseEntity<CustomerGroup> createCustomerGroup(@RequestBody CustomerGroup customerGroup) {
        return customerGroupService.createCustomerGroup(customerGroup);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerGroup> updateCustomerGroup(@PathVariable Integer id,
            @RequestBody CustomerGroup updatedGroup) {
        return customerGroupService.updateCustomerGroup(id, updatedGroup);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCustomerGroup(@PathVariable Integer id) {
        return customerGroupService.deleteCustomerGroup(id);
    }
}
