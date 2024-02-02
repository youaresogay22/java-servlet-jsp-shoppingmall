package com.nhnacademy.shoppingmall.user.service.impl;

import com.nhnacademy.shoppingmall.common.page.Page;
import com.nhnacademy.shoppingmall.thread.request.impl.PointChannelRequest;
import com.nhnacademy.shoppingmall.user.exception.UserAlreadyExistsException;
import com.nhnacademy.shoppingmall.user.exception.UserNotFoundException;
import com.nhnacademy.shoppingmall.user.repository.impl.UserRepositoryImpl;
import com.nhnacademy.shoppingmall.user.service.UserService;
import com.nhnacademy.shoppingmall.user.domain.User;
import com.nhnacademy.shoppingmall.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUser(String userId){
        //todo#4-1 회원조회
        Optional<User> user = userRepository.findById(userId);
        return user.orElse(null);
    }

    @Override
    public void saveUser(User user) {
        //todo#4-2 회원등록
        if (userRepository.countByUserId(user.getUserId()) >= 1) {
            throw new UserAlreadyExistsException(user.getUserId());
        } else {
            int result = userRepository.save(user);
            if (result == -1) {
                throw new RuntimeException("save failed, check your log");
            }
        }
    }

    @Override
    public void updateUser(User user) {
        //todo#4-3 회원수정
        if (userRepository.countByUserId(user.getUserId()) >= 1) {
            userRepository.update(user);
        } else if (userRepository.countByUserId(user.getUserId()) == 0) {
            throw new UserNotFoundException(user.getUserId());
        }
    }

    @Override
    public void deleteUser(String userId) {
        //todo#4-4 회원삭제
        if (userRepository.countByUserId(userId) >= 1) {
            userRepository.deleteByUserId(userId);
        } else if (userRepository.countByUserId(userId) == 0) {
            throw new UserNotFoundException(userId);
        }
    }

    @Override
    public User doLogin(String userId, String userPassword) {
        //todo#4-5 로그인 구현, userId, userPassword로 일치하는 회원 조회
        Optional<User> user = userRepository.findByUserIdAndUserPassword(userId, userPassword);
        if (user.isPresent()) {
            userRepository.updateLatestLoginAtByUserId(userId, LocalDateTime.now());
            return user.get();
        } else {
            throw new UserNotFoundException(userId);
        }
    }

    public int getAuthCount(String role) {
        return ((UserRepositoryImpl) userRepository).countAuth(role);
    }

    public Page<User> getUserPage(int page, int pageSize, String role) {
        return ((UserRepositoryImpl) userRepository).findAll(page, pageSize, role);
    }

    public void updateUserPoint(User user, PointChannelRequest request) {
        ((UserRepositoryImpl) userRepository).updatePointAndSaveDetail(user, request);
    }

}
