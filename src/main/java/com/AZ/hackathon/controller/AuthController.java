package com.AZ.hackathon.controller;

import com.AZ.hackathon.config.GoogleTokenVerifier;
import com.AZ.hackathon.entity.User;
import com.AZ.hackathon.repository.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    private final UserRepository userRepository;
//    private final JwtService jwtService;
//
//    public AuthController(UserRepository userRepository, JwtService jwtService) {
//        this.userRepository = userRepository;
//        this.jwtService = jwtService;
//    }
//
//    @PostMapping("/google")
//    public String googleLogin(@RequestBody Map<String, String> body) throws Exception {
//
//        String idToken = body.get("token");
//
//        var payload = GoogleTokenVerifier.verify(idToken);
//
//        String email = payload.getEmail();
//        String name = (String) payload.get("name");
//
//        User user = userRepository.findByEmail(email)
//                .orElseGet(() -> {
//                    User newUser = new User();
//                    newUser.setEmail(email);
//                    newUser.setUsername(name);
//                    return userRepository.save(newUser);
//                });
//
//        // 🔥 generate your own JWT
//        return jwtService.generateToken(user.getId());
//    }
//}
