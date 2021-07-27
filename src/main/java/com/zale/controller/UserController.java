package com.zale.controller;

import com.zale.entity.User;
import com.zale.service.UserService;
import com.zale.utils.VerifyCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("user")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("register")
    public Map<String, Object> register(@RequestBody User user, String code, HttpServletRequest request) {
        log.info("User info: [{}]", user.toString());
        log.info("User verification code: [{}]", code);
        Map<String, Object> map = new HashMap<>();
        try {
            String key = (String) request.getServletContext().getAttribute("code");
            if(key.equalsIgnoreCase(code)) {
                userService.register(user);
                map.put("state", true);
                map.put("msg", "Notice: register success");
            }
            else {
                throw new RuntimeException("verification code error!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state", false);
            map.put("msg", "Notice: " + e.getMessage());
        }
        return map;

    }

    @PostMapping("login")
    public Map<String, Object> login(@RequestBody User user) {
        log.info("current user info: [{}]", user.toString());
        Map<String, Object> map = new HashMap<>();
        try {
            User userDB = userService.login(user);
            map.put("state", true);
            map.put("msg", "Login success!");
            map.put("user", userDB);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("state", false);
            map.put("msg", e.getMessage());
        }
        return map;
    }


    @GetMapping("getImage")
    public String getImageCode(HttpServletRequest request) throws IOException {
        String code = VerifyCodeUtils.generateVerifyCode(4);
        request.getServletContext().setAttribute("code", code);

        //transfer Image to base64
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        VerifyCodeUtils.outputImage(120, 30, byteArrayOutputStream, code);
        return "data:image/png;base64,"+ Base64Utils.encodeToString(byteArrayOutputStream.toByteArray());
    }
}
