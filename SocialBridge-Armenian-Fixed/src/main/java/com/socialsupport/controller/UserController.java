package com.socialsupport.controller;

import com.socialsupport.entity.AppUser;
import com.socialsupport.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserRepository users;
    private final PasswordEncoder passwords = new BCryptPasswordEncoder();

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password,
                        HttpSession session, Model model) {
        AppUser user = users.findByUsernameOrEmail(username, username).orElse(null);

        if (user == null || !passwords.matches(password, user.getPassword())) {
            model.addAttribute("error", "Username or password is incorrect.");
            return "login";
        }

        session.setAttribute("userId", user.getId());
        return "redirect:/account";
    }

    @GetMapping("/register")
    public String register() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String username,
                           @RequestParam String email, @RequestParam String password,
                           @RequestParam String confirmPassword, Model model) {
        if (name.isBlank() || username.isBlank() || !email.contains("@")
                || password.length() < 8 || !password.equals(confirmPassword)) {
            model.addAttribute("error", "Please check the details and try again.");
            return "register";
        }

        if (users.existsByUsername(username) || users.existsByEmail(email)) {
            model.addAttribute("error", "This username or email is already registered.");
            return "register";
        }

        AppUser user = new AppUser();
        user.setName(name);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwords.encode(password));
        users.save(user);

        return "redirect:/login?registered";
    }

    @GetMapping("/account")
    public String account(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/login";
        }

        AppUser user = users.findById(userId).orElse(null);
        if (user == null) {
            session.invalidate();
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "account";
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
