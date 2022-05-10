package com.codesoom.demo.application;

import com.codesoom.demo.domain.Role;
import com.codesoom.demo.domain.RoleRepository;
import com.codesoom.demo.domain.User;
import com.codesoom.demo.dto.UserLoginDto;
import com.codesoom.demo.exceptions.InvalidAccessTokenException;
import com.codesoom.demo.exceptions.LoginFailException;
import com.codesoom.demo.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.*;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final RoleRepository roleRepository;

    public String login(UserLoginDto userLoginDto) {
        User user = userService.getUser(userLoginDto.getEmail());

        if(user.authenticate(userLoginDto.getPassword())) {
            return jwtUtil.encode(user.getId());
        }

        throw new LoginFailException(user.getEmail());
    }

    public Long parseToken(String token) {
        if(!hasText(token)) {
            throw new InvalidAccessTokenException(token);
        }

        try {
            Claims claims = jwtUtil.decode(token);
            return claims.get("userId", Long.class);
        } catch (JwtException e) {
            throw new InvalidAccessTokenException(token);
        }
    }

    public List<GrantedAuthority> getRoles(Long userId) {

        return roleRepository.findAllByUserId(userId)
                .stream().map((role -> new SimpleGrantedAuthority(role.getName())))
                .collect(Collectors.toList());
    }
}
