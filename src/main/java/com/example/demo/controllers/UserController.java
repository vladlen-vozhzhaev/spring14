package com.example.demo.controllers;

import com.example.demo.model.User;
import com.example.demo.repo.UserRepo;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class UserController {
    @Autowired
    UserRepo userRepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    @GetMapping("/user")
    public String user(Authentication authentication){
        System.out.println(authentication.getPrincipal());
        return "user";
    }
    @GetMapping("/reg")
    public String reg(@PathParam(value = "error") String error, Model model){
        System.out.println(error);
        boolean err = false;
        if(error != null) err = true;
        model.addAttribute("error", err);
        return "reg";
    }
    @PostMapping("/regUser")
    public String regUser(
            @RequestParam String name,
            @RequestParam String lastname,
            @RequestParam String email,
            @RequestParam String pass){
        Optional<User> oUser = userRepo.findByEmail(email);
        if(oUser.isEmpty()){
            pass = passwordEncoder.encode(pass);
            User user = new User(name, lastname, email, pass);
            userRepo.save(user);
            System.out.println("Пользователь "+name+" зарегистрирован");
            return "redirect:/login";
        }else{
            System.out.println("Такой пользователь уже существует");
        }
        return "redirect:/reg?error=exist";
    }
    @GetMapping("/login")
    public String login(@PathParam(value = "error") String error, Model model){
        boolean err = false;
        if(error != null){
            err = true;
        }
        model.addAttribute("error", err);
        return "login";
    }
    /*@PostMapping("/login")
    public String login(
            @RequestParam String email,
            @RequestParam String pass
    ){
        Optional<User> oUser = userRepo.findByEmailAndPass(email, pass);
        if(oUser.isEmpty()){
            System.out.println("Введён неправильный логин или пароль!");
            return "redirect:/login?error=notFound";
        }else{
            System.out.println("Пользователь "+email+" авторизован");
            return "redirect:/";
        }
    }*/
}
