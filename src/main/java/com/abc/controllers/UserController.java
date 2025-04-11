package com.abc.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.abc.entities.Post;
import com.abc.entities.User;
import com.abc.services.PostService;
import com.abc.services.UserService;
import com.abc.services.UserServiceImpl;

import jakarta.servlet.http.HttpSession;

@Controller
public class UserController {

    private final UserService userService = new UserServiceImpl();
    private final PostService postService;

    public UserController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/profile")
    public String profileUser(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null)
            return "redirect:/login";

        List<Post> posts = postService.getPostById(user.getId());

        model.addAttribute("user", user);
        model.addAttribute("posts", posts);

        return "profile";
    }

    // ✅ Search user theo số lượng following và follower
    @GetMapping("/search-users")
    public String searchUsers(
            @RequestParam(name = "minFollowing", defaultValue = "0") int minFollowing,
            @RequestParam(name = "minFollower", defaultValue = "0") int minFollower,
            Model model) {

        List<User> users = userService.searchUsersByFollowStats(minFollowing, minFollower);
        model.addAttribute("users", users);
        model.addAttribute("minFollowing", minFollowing);
        model.addAttribute("minFollower", minFollower);

        // Nếu không có user phù hợp thì gán cờ để hiển thị ảnh not-found
        if (users == null || users.isEmpty()) {
            model.addAttribute("notFound", true);
        }

        return "user_search_result"; // tên trang kết quả
    }
}
