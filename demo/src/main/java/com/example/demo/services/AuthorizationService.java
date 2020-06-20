package com.example.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.configs.Config;
import com.example.demo.entities.AuthorityEntity;
import com.example.demo.entities.RoleEntity;
import com.example.demo.entities.TypeGroupEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.repositories.AuthorityRepository;
import com.example.demo.repositories.UserRepository;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class AuthorizationService {
    @Autowired
    AuthorityRepository authorityRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    HttpServletRequest request;
    
    private static Pattern pattern = Pattern.compile("\\{.*}");

    @Transactional(rollbackFor = {})
    public boolean checkMethodAccessAuthority() {
        String uid = (String) request.getAttribute(Config.CURRENT_UID_ATTRIBUTE);
        String uri = request.getRequestURI();
        Optional<UserEntity> user = userRepository.findById(uid);
        Optional<AuthorityEntity> authority = authorityRepository.findByUri(pattern.matcher(uri).replaceAll("*"));
        if (!authority.isPresent()) {
            return true;
        } 
        else if (user.isPresent()) {
            TypeGroupEntity type = user.get().getType();
            if (type != null) {
                for (RoleEntity role : type.getRoles()) {
                    if (role.getAuthorities().contains(authority.get())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}

