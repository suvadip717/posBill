package com.tnx.posBilling.service.interfaces;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.tnx.posBilling.model.User;

public interface UserService {
    public ResponseEntity<List<User>> getAllUsers();

    public ResponseEntity<User> getUserById(int id);

    public ResponseEntity<User> createUser(User user);

    public ResponseEntity<User> updateUser(int id, User userDetails);

    public ResponseEntity<String> deleteUser(int id);
}
