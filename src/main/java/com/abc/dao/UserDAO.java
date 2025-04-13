package com.abc.dao;

import com.abc.entities.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class UserDAO {

    @Autowired
    private SessionFactory sessionFactory;

    public User getUserByUserName(String userName) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE username = :username", User.class);
            query.setParameter("username", userName);
            return query.uniqueResult();
        }
    }
    
    public User getUserByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE email = :email", User.class);
            query.setParameter("email", email);
            return query.uniqueResult();
        }
    }
    
    public void saveUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
    }

    public void updateUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }
    
    public User getUserById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(User.class, id);
        }
    }

    public boolean registerUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            user.setCreatedAt(LocalDateTime.now());
            session.save(user);
            session.getTransaction().commit();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> findUsersByFollowCriteria(int minFollowing, int minFollower) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "SELECT u FROM User u " +
                        "WHERE (SELECT COUNT(f) FROM Follow f WHERE f.followingUserId = u.id) >= :minFollowing " +
                        "AND (SELECT COUNT(f) FROM Follow f WHERE f.followedUserId = u.id) >= :minFollower";
            Query<User> query = session.createQuery(hql, User.class);
            query.setParameter("minFollowing", minFollowing);
            query.setParameter("minFollower", minFollower);
            return query.list();
        }
    }

    public List<User> searchUsersByUsername(String keyword) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("FROM User WHERE username LIKE :keyword", User.class);
            query.setParameter("keyword", "%" + keyword + "%");
            return query.list();
        }
    }
}