package com.abc.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.abc.entities.Post;
import com.abc.entities.User;
import com.abc.services.PostService;
import com.abc.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    private final UserService userService;
    private final PostService postService;

    @Autowired
    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    @GetMapping("/profile")
    public String profileUser(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            List<Post> posts = postService.getPostById(user.getId());
            model.addAttribute("user", user);
            model.addAttribute("posts", posts);
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tải hồ sơ người dùng");
        }

        return "profile";
    }

    @GetMapping("/search-users")
    public String searchUsers(
            @RequestParam(name = "minFollowing", defaultValue = "0") int minFollowing,
            @RequestParam(name = "minFollower", defaultValue = "0") int minFollower,
            Model model) {
        try {
            List<User> users = userService.findUsersByFollowCriteria(minFollowing, minFollower);
            model.addAttribute("users", users);
            model.addAttribute("minFollowing", minFollowing);
            model.addAttribute("minFollower", minFollower);

            if (users == null || users.isEmpty()) {
                model.addAttribute("notFound", true);
            }
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("notFound", true);
        } catch (Exception e) {
            model.addAttribute("error", "Không thể tìm kiếm người dùng");
            model.addAttribute("notFound", true);
        }

        return "user_search_result";
    }
}