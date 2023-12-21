package com.webnongsan.greenshop.controller;

import com.webnongsan.greenshop.model.dto.ChangePassword;
import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.repository.Entities.UserEntity;
import com.webnongsan.greenshop.service.SendEmailServiceImpl;
import com.webnongsan.greenshop.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final SendEmailServiceImpl sendEmailService;
    private final UserServiceImpl userService;

    @Autowired
    HttpSession session;

    @GetMapping("/forgotPassword")
    public String forgotPassword(){
        return "web/forgotPassword";
    }


    @PostMapping("/forgotPassword")
    public String forgotPassword(Model model, @RequestParam("email") String email)
    {

                String userEmail = userService.forgotPassword(email);
                if(userEmail != null){
                    model.addAttribute("email",email);
                    model.addAttribute("message","Mã xác thực OTP đã được gửi tới Email : "+userEmail);
                    return "web/confirmOtpForgotPassword";
                } else {
                    model.addAttribute("error", "Email này chưa đăng ký!");
                    return "web/forgotPassword";
                }
    }


    @PostMapping("/changePassword")
    public String changePassword(Model model, @Valid @ModelAttribute("changePassword") ChangePassword changePassword,
                                       BindingResult result,
                                       @RequestParam("email") String email){
        if (result.hasErrors() || !changePassword.getNewPassword().equals(changePassword.getConfirmPassword())) {
            model.addAttribute("newPassword", changePassword.getNewPassword());
            model.addAttribute("confirmPassword", changePassword.getConfirmPassword());
            model.addAttribute("email", email);
            if (!changePassword.getNewPassword().equals(changePassword.getConfirmPassword())) {
                model.addAttribute("error", "error");
            }
            return "/web/changePassword";
        }
        userService.resetPassword(email, changePassword.getNewPassword());
        model.addAttribute("message", "Đặt lại mật khẩu thành công!");
        model.addAttribute("email", "");
        session.removeAttribute("otp");
        return "/web/changePassword";
    }


    @PostMapping("/confirmOtpForgotPassword")
    public String confirm(Model model, @RequestParam("otp") String otp,
                                                @RequestParam("email") String email){
        if (otp.equals(String.valueOf(session.getAttribute("otp")))) {
            model.addAttribute("email", email);
            model.addAttribute("newPassword", "");
            model.addAttribute("confirmPassword", "");
            model.addAttribute("changePassword", new ChangePassword());
            return "web/changePassword";
        }
        model.addAttribute("email", email);
        model.addAttribute("error", "Mã xác thực OTP không đúng, thử lại!");
        return "web/confirmOtpForgotPassword";
    }
}
