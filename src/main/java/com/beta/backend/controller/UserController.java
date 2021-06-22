package com.beta.backend.controller;

import com.beta.backend.domain.dto.PasswordResetDTO;
import com.beta.backend.domain.dto.RoleDTO;
import com.beta.backend.domain.dto.UserDTO;
import com.beta.backend.service.mapper.UserMapper;
import com.beta.backend.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("info/{id:\\d}")
    public ResponseEntity<UserDTO> getUserInfo(@PathVariable(name = "id") Long id) {
        UserDTO byId = userMapper.userToUserDTO(userService.findById(id).orElse(null));
        return ResponseEntity.ok().body(byId);
    }

    @GetMapping("info/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllUserInfo() {
        return ResponseEntity.ok().body(userMapper.usersToUserDTOs(userService.findAll()));
    }
    @GetMapping("info/public/all")
    public ResponseEntity<?> getAllPublicUserInfo() {
        return ResponseEntity.ok().body(userMapper.usersToUserDTOs(userService.findPublicUsers()));
    }

    @PutMapping("info/{id:\\d+}")

    public ResponseEntity<?> updateUserInfo(@PathVariable(name = "id") Long id, @RequestBody UserDTO userDTO) {
        UserDTO byId = userMapper.userToUserDTO(userService.updateUserInfo(id, userMapper.userDTOToUser(userDTO)));
        return ResponseEntity.ok().body(byId);
    }

    @PutMapping("chairman/{chairmanId:\\d+},{followerId:\\d+}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> setChairmanForFollower(@PathVariable Long chairmanId, @PathVariable Long followerId) {
        UserDTO byId = userMapper.userToUserDTO(userService.updateChairman(chairmanId, followerId));
        return ResponseEntity.ok().body(byId);
    }

    @GetMapping("followers/{id:\\d+}")
    public ResponseEntity<?> findFollowersByChairmanId(@PathVariable(name = "id") Long id) {

        List<UserDTO> followers = userMapper.usersToUserDTOs(userService.findFollowersByChairmanId(id));
        return ResponseEntity.ok().body(followers);

    }

    @PutMapping("password/{id:\\d+}")
    public ResponseEntity<?> resetUserPassword(@PathVariable(name = "id") Long id, @Valid @RequestBody PasswordResetDTO passwordDTO) {
        UserDTO byId = userMapper.userToUserDTO(userService.updatePassword(id, passwordDTO.getPassword()));
        return ResponseEntity.ok().body(byId);

    }

    @PutMapping("roles/{id:\\d+}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> setRoles(@PathVariable(name = "id") Long id, @RequestBody Set< @Valid RoleDTO> roles) {
        UserDTO byId = userMapper.userToUserDTO(userService.updateRoles(id, roles.stream()
            .map(RoleDTO::toRole)
            .filter(Objects::nonNull)
            .collect(Collectors.toSet())));
        return ResponseEntity.ok().body(byId);
    }

    @DeleteMapping("{id:\\d+}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("chairman/all")
    public ResponseEntity<?> findChairmans() {
        return ResponseEntity.ok().body(userMapper.usersToUserDTOs(userService.findChairmans()));
    }

    @GetMapping("chairman/followers/{chairmanId:\\d+}")
    public ResponseEntity<?> findChairmanByFollower(@PathVariable long chairmanId) {
        return ResponseEntity.ok().body(userMapper.usersToUserDTOs(userService.findFollowersByChairmanId(chairmanId)));
    }
}
