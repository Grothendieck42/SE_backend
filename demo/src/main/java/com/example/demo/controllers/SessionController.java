package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.demo.configs.Config;
import com.example.demo.entities.SessionEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.repositories.UserRepository;
import com.example.demo.repositories.SqlSessionRepository;
import com.example.demo.requests.session.LoginRequest;
import com.example.demo.responses.session.*;
import com.example.demo.utils.SecurityUtils;
import com.example.demo.utils.SessionUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.example.demo.utils.SecurityUtils.getHashedPasswordByPasswordAndSalt;
import static com.example.demo.utils.SecurityUtils.getSalt;

@Controller
@RequestMapping(path = "/session")
public class SessionController {
    private final UserRepository userRepository;
    private final SqlSessionRepository sqlSessionRepository;

    @Autowired
    public SessionController(UserRepository userRepository, SqlSessionRepository sqlSessionRepository) {
        this.userRepository = userRepository;
        this.sqlSessionRepository = sqlSessionRepository;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest login) {
        if (!userRepository.existsById(login.getUid())) {
            return new ResponseEntity<>(new LoginResponse("", "", null, "User not exists"), HttpStatus.BAD_REQUEST);
        }

        UserEntity user = userRepository.findById(login.getUid()).get();
        if (!user.getHashedPassword().equals(SecurityUtils.getHashedPasswordByPasswordAndSalt(login.getPassword(), user.getSalt()))) {
            return new ResponseEntity<>(new LoginResponse("", "", null, "Password incorrect"), HttpStatus.BAD_REQUEST);
        }

        Optional<SessionEntity> ret = sqlSessionRepository.findByUid(login.getUid());
        SessionEntity session;
        if (ret.isPresent()) {
            session = ret.get();
        } else {
            session = new SessionEntity();
            session.setUid(login.getUid());
        }
        session.setToken(SessionUtils.getToken());
        session.setTimestamp(Timestamp.valueOf(LocalDateTime.now()));
        session.setOpt(Timestamp.valueOf(LocalDateTime.now()));    // 
        sqlSessionRepository.save(session);

        return new ResponseEntity<>(new LoginResponse(login.getUid(), session.getToken(), user.readTypeName(), "OK"), HttpStatus.OK);
    }

    @PostMapping(path = "/logout")
    public ResponseEntity<LogoutResponse> logout(@Autowired HttpServletRequest request) {
        String token = request.getHeader(Config.AUTH_HEADER);
        Optional<SessionEntity> session = sqlSessionRepository.findByToken(token);
        if (!session.isPresent()) {
            return new ResponseEntity<>(new LogoutResponse("Not logged in", null), HttpStatus.UNAUTHORIZED);
        } else {
            String uid = session.get().getUid();
            sqlSessionRepository.delete(session.get());
            return new ResponseEntity<>(new LogoutResponse("OK", uid), HttpStatus.OK);
        }
    }
}
