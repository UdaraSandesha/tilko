package com.yaanlabs.healthpredictionapi.service;

import com.yaanlabs.healthpredictionapi.HealthPredictionAppException;
import com.yaanlabs.healthpredictionapi.model.AuthResponse;
import com.yaanlabs.healthpredictionapi.model.User;
import com.yaanlabs.healthpredictionapi.repository.UserRepository;
import com.yaanlabs.healthpredictionapi.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public void registerUser(User user) throws DataIntegrityViolationException {
        userRepository.save(user);
    }

    public void updateUserInfo(User user) {
        Optional<User> userResult = userRepository.findById(user.getUserID());
        if (userResult.isPresent()) {
            User updateUser = userResult.get();
            updateUser.setName(user.getName());
            updateUser.setMobileNo(user.getMobileNo());
            updateUser.setAddress(user.getAddress());
            updateUser.setGender(user.getGender());
            updateUser.setBirthday(user.getBirthday());
            updateUser.setHeight(user.getHeight());
            updateUser.setWeight(user.getWeight());

            userRepository.save(updateUser);
        }
    }

    public User findUserByEmail(String email) throws HealthPredictionAppException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new HealthPredictionAppException("user.signin.email.not.found", email);
        }
        return user;
    }

    public User updateUser(User updatedUser) {
        return userRepository.save(updatedUser);
    }

    public User findUserByID(Integer userID) throws HealthPredictionAppException {
        Optional<User> userResult = userRepository.findById(userID);
        if (userResult.isPresent()) {
            return userResult.get();
        } else {
            throw new HealthPredictionAppException("user.find.failed");
        }
    }

    public User findUserByNameOrPhone(String name, String mobileNumber) throws HealthPredictionAppException {
        boolean findByName = false;
        boolean findByMobile = false;
        if (name != null && !name.isEmpty())
            findByName = true;

        if (mobileNumber != null && !mobileNumber.isEmpty())
            findByMobile = true;

        List<User> userList;
        if (findByName && findByMobile) {
            userList = this.userRepository.findUserByNameAndMobileNo(name, mobileNumber);
        } else if (findByName) {
            userList = this.userRepository.findUserByName(name);
        } else if (findByMobile) {
            userList = this.userRepository.findUserByMobileNo(mobileNumber);
        } else {
            throw new HealthPredictionAppException("user.find.failed.no.valid.info");
        }

        if (userList.isEmpty()) {
            throw new HealthPredictionAppException("user.find.failed");
        }

        if (userList.size() != 1) {
            throw new HealthPredictionAppException("user.find.failed.more.than.one.user");
        }

        return userList.get(0);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return getUserDetailsFromUser(findUserByEmail(username));
        } catch (HealthPredictionAppException appException) {
            throw new UsernameNotFoundException(appException.getMessage());
        }
    }

    public UserDetails getUserDetailsFromUser(User user) {
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), new ArrayList<>());
    }

    public AuthResponse onSuccessfulSignin(String email) throws HealthPredictionAppException {
        return onSuccessfulSignin(findUserByEmail(email));
    }

    public AuthResponse onSuccessfulSignin(User user) {
        user = updateUser(user);
        String token = jwtTokenUtil.generateToken(user);
        return new AuthResponse("user.signin.success", user.getUserID(), token);
    }

}
