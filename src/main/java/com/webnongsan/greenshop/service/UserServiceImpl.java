package com.webnongsan.greenshop.service;

import com.webnongsan.greenshop.model.dto.UserDTO;
import java.util.List;

public interface UserServiceImpl  {
    UserDTO findByEmail(String email);
    List<UserDTO> findAll();
    boolean confirmRegister(UserDTO userDTO, String password, String otp);

    void resetPassword(String email,String newPassword);
    String forgotPassword(String email);
    int sendOtp(UserDTO userDTO);

}
