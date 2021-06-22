package com.beta.backend.service.user;

import com.beta.backend.domain.model.Role;
import com.beta.backend.domain.model.User;
import com.beta.backend.domain.exception.impl.ForbiddenExceptionImpl;
import com.beta.backend.domain.exception.impl.UserNotFoundExceptionImpl;
import com.beta.backend.domain.exception.impl.ValidationExceptionImpl;
import lombok.NonNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface UserService {
    @Transactional(readOnly = true)
    boolean existsById(long id);

    @Transactional(readOnly = true)
    boolean existsByUsername(@NonNull String username);

    @Transactional(readOnly = true)
    Optional<User> findUserByUsername(@NonNull String username);

    @Transactional(readOnly = true)
    Optional<User> findById(long id);

    @Transactional(readOnly = true)
    List<User> findAll();

    @Transactional(readOnly = true)
    List<User> findPublicUsers();

    List<User> findFollowersByChairmanId(long chairmanId);

    @Transactional(readOnly = true)
    User findChairmanByFollowerId(long followerID) throws UserNotFoundExceptionImpl;

    @Transactional(readOnly = true)
    List<User> findChairmans();

    @Transactional
    User createUser(@NonNull User user);

    @Transactional
    void deleteUser(long id);

    @Transactional
    User updateUserInfo(long sourceUserId, @NonNull User changedUser) throws UserNotFoundExceptionImpl, ValidationExceptionImpl, ForbiddenExceptionImpl;

    @Transactional
    User updatePassword(long userId, String password);

    @Transactional
    User updateRoles(long id, @NonNull Set<Role> roles);

    @Transactional
    User updateChairman(long chairmanId, long followerId);

    @Transactional(readOnly = true)
    boolean isUserInChairmanGroup(long userId, long chairmanId);
}
