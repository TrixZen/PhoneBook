package phonebook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import phonebook.dto.LoginRequest;
import phonebook.service.UserService;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;
    @GetMapping("/login")
    public String showLoginForm() {
        return "login";  // login.html хуудас руу чиглэнэ
    }
    @GetMapping("/index")
    public String showIndexPage() {
        return "index";
    }
    @GetMapping("/sign")
    public String showIndexSign() {
        return "sign";
    }
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody LoginRequest request) {
        userService.registerUser(request);
        return ResponseEntity.ok("Бүртгэл амжилттай!");
    }
}

