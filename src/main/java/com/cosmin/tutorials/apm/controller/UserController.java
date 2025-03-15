package com.cosmin.tutorials.apm.controller;

import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Transaction;
import com.cosmin.tutorials.apm.database.User;
import com.cosmin.tutorials.apm.exception.UserNotFoundException;
import com.cosmin.tutorials.apm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public User get(@PathVariable("id") Integer id) {
        Transaction transaction = ElasticApm.currentTransaction();
        transaction.setName("GetUserRequest");
        transaction.setUser("testuser", "email", "uid");
        transaction.addLabel("getUser", id);
        transaction.addCustomContext("getRequestUser", id);

        User user = userService.get(id).orElseThrow(UserNotFoundException::new);

        transaction.end();
        return user;
    }

    @PostMapping("")
    public User create(@RequestBody User user) {
        return userService.save(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        User user = userService.get(id).orElseThrow(UserNotFoundException::new);
        userService.delete(user.getId());

        return new ResponseEntity(HttpStatus.OK);
    }
}
