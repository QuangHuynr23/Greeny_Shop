package com.webnongsan.greenshop.controller;

import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.repository.Entities.UserEntity;
import com.webnongsan.greenshop.repository.UserRepository;
import com.webnongsan.greenshop.service.SendEmailServiceImpl;
import com.webnongsan.greenshop.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RegisterController {
    private final UserServiceImpl userService;
    private final SendEmailServiceImpl sendEmailService;

    @Autowired
    HttpSession session;
    @GetMapping("/register")
    public String registerForm(ModelMap model){
        model.addAttribute("user", new UserEntity());
        return "web/register";
    }

    @PostMapping("/register")
    public String register(Model model, @Validated @ModelAttribute("user") UserDTO userDTO
                                      , BindingResult result){
        if (result.hasErrors()) {
            return "web/register";
        }
        if(!checkEmail(userDTO.getEmail())){
            model.addAttribute("error","Email này đã được sử dụng");
            return "web/register";
        }
        int otp = userService.sendOtp(userDTO);
        model.addAttribute("user",userDTO);
        model.addAttribute("message","Mã xác thực OTP đã gửi tới Email : "
                            + userDTO.getEmail() +", Hãy kiểm tra Email của bạn");
        return "web/confirmOtpRegister";
    }

    @PostMapping("/confirmOtpRegister")
    public String confirmRegister(Model model, @ModelAttribute("user") UserDTO userDTO,
                                        @RequestParam("password") String password, @RequestParam("otp") String otp){
        if(userService.confirmRegister(userDTO,password,otp)){
            model.addAttribute("message", "Đăng ký thành công");
            return "web/login";
        }
        model.addAttribute("user", userDTO);
        model.addAttribute("error", "Mã xác thực OTP không chính xác, hãy thử lại!");
        return "web/confirmOtpRegister";
    }

    public boolean checkEmail(String email){
        List<UserDTO> userDTOS = userService.findAll();
        for(UserDTO userDTO: userDTOS){
            if(userDTO.getEmail().equalsIgnoreCase(email)){
                return false;
            }
        }
        return true;
    }
}
