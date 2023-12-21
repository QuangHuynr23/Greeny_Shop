package com.webnongsan.greenshop.controller;

import com.webnongsan.greenshop.repository.Entities.UserEntity;
import com.webnongsan.greenshop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


@Controller
@RequiredArgsConstructor
public class LoginController {
    @GetMapping(value = "/login")
    public String login() {
        return "web/login";
    }


}

