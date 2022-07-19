package com.yaanlabs.healthpredictionapi.controller;

import com.yaanlabs.healthpredictionapi.HealthPredictionAppException;
import com.yaanlabs.healthpredictionapi.model.APIAppResponse;
import com.yaanlabs.healthpredictionapi.model.AuthResponse;
import com.yaanlabs.healthpredictionapi.model.User;
import com.yaanlabs.healthpredictionapi.service.EmailService;
import com.yaanlabs.healthpredictionapi.service.UserService;
import com.yaanlabs.healthpredictionapi.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private static final String UNIQUE_CONSTRAINT_USER_EMAIL = "'user.email_UNIQUE'";

    @Autowired
    UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private EmailService emailService;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user);
            return APIAppResponse.getResponseEntity("user.signup.success", HttpStatus.OK);
        } catch (DataIntegrityViolationException exception) {
            if (CommonUtils.isUniqueKeyIntegrityViolation(exception, UNIQUE_CONSTRAINT_USER_EMAIL)) {
                return APIAppResponse.getResponseEntity("user.signup.already.registered", HttpStatus.BAD_REQUEST);
            }
            throw exception;
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<?> signinUser(@RequestBody User user) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
            AuthResponse authResponse = userService.onSuccessfulSignin(user.getEmail());
            return new ResponseEntity<>(authResponse, HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return APIAppResponse.getResponseEntity("user.signin.invalid.credentials", HttpStatus.UNAUTHORIZED);
        } catch (HealthPredictionAppException appException) {
            return appException.toResponseEntity();
        }
    }

    @PostMapping("/profile/{user_id}")
    public ResponseEntity<?> updateUserProfile(@PathVariable("user_id") Integer userID, @RequestBody User user,
                                               HttpServletRequest request) {
        CommonUtils.validateTokenAndDataRequestedUser(request, userID);
        user.setUserID(userID);
        userService.updateUserInfo(user);
        return APIAppResponse.getResponseEntity("user.update.profile.success", HttpStatus.OK);
    }

    @PostMapping("/password/{user_id}")
    public ResponseEntity<?> updateUserPassword(@PathVariable("user_id") Integer userID, @RequestBody String newPassword,
                                                HttpServletRequest request) {
        CommonUtils.validateTokenAndDataRequestedUser(request, userID);
        try {
            User user = userService.findUserByID(userID);
            user.setPassword(newPassword);
            userService.updateUser(user);
            return APIAppResponse.getResponseEntity("user.update.password.success", HttpStatus.OK);
        } catch (HealthPredictionAppException appException) {
            return appException.toResponseEntity();
        }
    }

    @PostMapping("/profile_image/{user_id}")
    public ResponseEntity<?> updateUserProfileImage(@PathVariable("user_id") Integer userID, @RequestBody String encodedImage,
                                                    HttpServletRequest request) {
        CommonUtils.validateTokenAndDataRequestedUser(request, userID);
        try {
            User user = userService.findUserByID(userID);
            user.setProfileImage(encodedImage);
            userService.updateUser(user);
            return APIAppResponse.getResponseEntity("user.update.profile.image.success", HttpStatus.OK);
        } catch (HealthPredictionAppException appException) {
            return appException.toResponseEntity();
        }
    }

    @GetMapping("/{user_id}")
    public ResponseEntity<?> getUserProfile(@PathVariable("user_id") Integer userID, HttpServletRequest request) {
        CommonUtils.validateTokenAndDataRequestedUser(request, userID);
        try {
            User user = userService.findUserByID(userID);
            user.removePasswordInAPIResponse();
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (HealthPredictionAppException appException) {
            return appException.toResponseEntity();
        }
    }

    @PostMapping("/forgot/email")
    public ResponseEntity<?> getEmailForForgotten(@RequestBody User user) {
        try {
            User userByNameOrPhone = userService.findUserByNameOrPhone(user.getName(), user.getMobileNo());
            return new ResponseEntity<>(userByNameOrPhone.getEmail(), HttpStatus.OK);
        } catch (HealthPredictionAppException appException) {
            return appException.toResponseEntity();
        }
    }

    @PostMapping("/forgot/password")
    public ResponseEntity<?> onUserForgotPassword(@RequestBody String email) throws Exception {
        try {
            User userByEmail = userService.findUserByEmail(email);
            emailService.sendResetPasswordEmail(userByEmail);
            return APIAppResponse.getResponseEntity("user.reset.password.email.sent", HttpStatus.OK);
        } catch (HealthPredictionAppException appException) {
            return appException.toResponseEntity();
        }
    }

    @PostMapping("/reset/password")
    public ResponseEntity<?> resetUserPassword(@RequestParam String token, @RequestBody User user) {
        try {
            User userByEmail = userService.findUserByEmail(user.getEmail());
            if (userByEmail.getPassword().equals(token)) {
                userByEmail.setPassword(user.getPassword());
                userService.updateUser(userByEmail);
                return APIAppResponse.getResponseEntity("user.reset.password.success", HttpStatus.OK);
            } else {
                return APIAppResponse.getResponseEntity("user.reset.password.invalid.token", HttpStatus.BAD_REQUEST);
            }
        } catch (HealthPredictionAppException appException) {
            return appException.toResponseEntity();
        }
    }

}
