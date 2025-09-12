package com.ianclark226.event_venue.service;

import com.ianclark226.event_venue.model.User;

import java.util.List;

public interface IUserService {

    User registerUser(User user);

    List<User> getUsers();

    void deleteUser(String email);

    User getUser(String email);
}
