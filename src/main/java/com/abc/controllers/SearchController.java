package com.abc.controllers;

import com.abc.dao.UserDAO;
import com.abc.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SearchController {

    @Autowired
    private UserDAO userDAO;

    @GetMapping("/search")
    public String searchUser(@RequestParam("query") String query, Model model) {
        List<User> results = userDAO.searchUsersByUsername(query);
        model.addAttribute("results", results);
        model.addAttribute("keyword", query);
        return "searchResult"; // Tên file JSP: searchResult.jsp
    }
}
