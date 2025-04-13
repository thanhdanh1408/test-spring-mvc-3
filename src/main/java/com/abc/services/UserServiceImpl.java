package com.abc.services;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abc.dao.UserDAO;
import com.abc.entities.User;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDAO userDAO;

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
        );

    
    @Override
    public User getUserByUserName(String userName) {
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên người dùng không được rỗng");
        }
        return userDAO.getUserByUserName(userName);
    }

    @Override
    public boolean registerUser(User user) {
        if (user == null || user.getUsername() == null || user.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Tên người dùng không được rỗng");
        }
        if (user.getPassWord() == null || user.getPassWord().trim().isEmpty()) {
            throw new IllegalArgumentException("Mật khẩu không được rỗng");
        }
        return userDAO.registerUser(user);
    }
    
    public boolean isEmailValid(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            return false;
        }
        return userDAO.getUserByEmail(email) == null;
    }
    
    public boolean isAgeValid(LocalDate dateOfBirth) {
        return Period.between(dateOfBirth, LocalDate.now()).getYears() >= 15;
    }
    
    public void saveUser(User user) {
        userDAO.saveUser(user);
    }

    public void updateUser(User user) {
        userDAO.updateUser(user);
    }

    public User getUserById(Long id) {
        return userDAO.getUserById(id);
    }

    @Override
    public List<User> findUsersByFollowCriteria(int minFollowing, int minFollower) {
        if (minFollowing < 0 || minFollower < 0) {
            throw new IllegalArgumentException("Số lượng người theo dõi hoặc đang theo dõi không được âm");
        }
        return userDAO.findUsersByFollowCriteria(minFollowing, minFollower);
    }

    @Override
    public List<User> searchUsersByUsername(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("Từ khóa tìm kiếm không được rỗng");
        }
        return userDAO.searchUsersByUsername(keyword);
    }
}