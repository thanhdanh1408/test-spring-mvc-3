package com.abc.services;

import java.util.List;

import com.abc.entities.User;

public interface UserService {
    User getUserByUserName(String userName);
    boolean registerUser(User user);
    List<User> searchUsersByFollowStats(int minFollowing, int minFollower);

}