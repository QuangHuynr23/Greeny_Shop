package com.webnongsan.greenshop.service.impl;

import com.webnongsan.greenshop.converter.UserConverter;
import com.webnongsan.greenshop.model.dto.UserDTO;
import com.webnongsan.greenshop.repository.Entities.RoleEntity;
import com.webnongsan.greenshop.repository.Entities.UserEntity;
import com.webnongsan.greenshop.repository.UserRepository;
import com.webnongsan.greenshop.service.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceImpl {
    private final UserRepository userRepository;
    private final UserConverter userConverter;
    private final SendEmailService sendEmailService;
    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Autowired
    HttpSession session;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public UserDTO findByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        return userConverter.convertToDTO(userEntity);
    }

    @Override
    public List<UserDTO> findAll() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities.stream().map(userConverter::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public boolean confirmRegister(UserDTO userDTO, String password, String otp) {
        if (otp.equals(String.valueOf(session.getAttribute("otp")))) {
            userDTO.setPassword(passwordEncoder.encode(password));
            userDTO.setRegisterDate(new Date());
            userDTO.setStatus(true);
            userDTO.setAvatar("user.png");
            userDTO.setRoleEntities(List.of(new RoleEntity("ROLE_USER")));
            userRepository.save(userConverter.convertToEntity(userDTO));
            session.removeAttribute("otp");
            return true;
        }
        return false;
    }

    @Override
    public void resetPassword(String email, String newPassword) {
        UserDTO userDTO = findByEmail(email);
        if (userDTO != null) {
            userDTO.setStatus(true);
            userDTO.setPassword(bCryptPasswordEncoder.encode(newPassword));
            userDTO.setRoleEntities(List.of(new RoleEntity("ROLE_USER")));
            UserEntity userEntity = userConverter.convertToEntity(userDTO);
            userRepository.save(userEntity);
        }
    }

    @Override
    public String forgotPassword(String email) {
        List<UserDTO> userDTOList = findAll();
        for (UserDTO userDTO : userDTOList) {
            if (email.trim().equals(userDTO.getEmail())) {
                session.removeAttribute("otp");
                int random_otp = (int) Math.floor(Math.random() * (999999 - 100000 + 1) + 100000);
                session.setAttribute("otp", random_otp);
                String body = "<div>\r\n" + "<h3>Mã xác thực OTP của bạn là: <span style=\"color:#119744; font-weight: bold;\">"
                        + random_otp + "</span></h3>\r\n" + "</div>";
                sendEmailService.queue(email, "Quên mật khẩu", body);
                return userDTO.getEmail();
            }
        }
        return null;
    }

    @Override
    public int sendOtp(UserDTO userDTO) {
        session.removeAttribute("otp");
        int random_otp = (int) Math.floor(Math.random() * (999999 - 100000 + 1) + 100000);
        session.setAttribute("otp",random_otp);
        String body = "<div>\r\n" + "<h3>Mã xác thực OTP của bạn là: <span style=\"color:#119744; font-weight: bold;\">"
                + random_otp + "</span></h3>\r\n" + "</div>";
        sendEmailService.queue(userDTO.getEmail(),"Đăng ký tài khoản",body);
        return random_otp;
    }
}