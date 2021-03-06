package com.example.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import com.example.demo.configs.Config;
import com.example.demo.entities.DepartmentEntity;
import com.example.demo.entities.MajorClassEntity;
import com.example.demo.entities.TypeGroupEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.repositories.DepartmentRepository;
import com.example.demo.repositories.MajorClassRepository;
import com.example.demo.repositories.TypeGroupRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.requests.information.*;
import com.example.demo.responses.information.*;
import com.example.demo.annotations.session.*;
import com.example.demo.services.QueryService;
import com.example.demo.utils.SecurityUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static com.example.demo.utils.SecurityUtils.getHashedPasswordByPasswordAndSalt;
import static com.example.demo.utils.SecurityUtils.getSalt;

@Controller
@RequestMapping(path = "/user")
public class UserController {
    private final UserRepository userRepository;
    private final ResourceLoader resourceLoader;
    private final DepartmentRepository departmentRepository;
    private final MajorClassRepository majorClassRepository;
    private final TypeGroupRepository typeGroupRepository;
    private final QueryService queryService;

    @Autowired
    public UserController(UserRepository userRepository, ResourceLoader resourceLoader,
                          DepartmentRepository departmentRepository, TypeGroupRepository typeGroupRepository,
                          MajorClassRepository majorClassRepository, QueryService queryService) {
        this.userRepository = userRepository;
        this.resourceLoader = resourceLoader;
        this.departmentRepository = departmentRepository;
        this.typeGroupRepository = typeGroupRepository;
        this.majorClassRepository = majorClassRepository;
        this.queryService = queryService;
    }

    @PutMapping(path = "/add")
    //   @Authorization
    public ResponseEntity<AddUserResponse> addUser(@RequestBody AddUserRequest request) {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        String[] uids = request.getUids();
        String[] names = request.getNames();
        String[] genders = request.getGenders();
        String[] passwords = request.getPasswords();
        String type = request.getType();
        if (uids == null || names == null || genders == null || passwords == null || type == null) {
            return new ResponseEntity<>(new AddUserResponse("Invalid request", null, null, null, null), HttpStatus.BAD_REQUEST);
        } else if (uids.length != names.length || names.length != genders.length || genders.length != passwords.length) {
            return new ResponseEntity<>(new AddUserResponse("User information number not matching", null, null, null, null), HttpStatus.BAD_REQUEST);
        }

        UserEntity[] users = new UserEntity[uids.length];

        for (int i = 0; i < uids.length; i++) {
            UserEntity user = users[i] = new UserEntity();
            if (uids[i] == null || uids[i].length() != 10) {
                return new ResponseEntity<>(new AddUserResponse("Uid must have 10 characters", uids[i], names[i], genders[i], type), HttpStatus.BAD_REQUEST);
            }
            user.setUid(uids[i]);
            if (names[i] == null || names[i].length() == 0) {
                return new ResponseEntity<>(new AddUserResponse("Name mustn't be empty", uids[i], names[i], genders[i], type), HttpStatus.BAD_REQUEST);
            }
            user.setName(names[i]);
            String salt = getSalt();
            user.setSalt(salt);
            String password;
            if (passwords[i] != null) {
                password = passwords[i];
            } else {
                password = Config.INIT_PWD;
            }
            if (password.length() < 6) {
                return new ResponseEntity<>(new AddUserResponse("password must have at least 6 characters", uids[i], names[i], genders[i], type), HttpStatus.BAD_REQUEST);
            }
            String hashedPassword = getHashedPasswordByPasswordAndSalt(password, salt);
            user.setHashedPassword(hashedPassword);
            if (request.getType() != null) {
                Optional<TypeGroupEntity> typeGroup = typeGroupRepository.findByName(request.getType());
                if (!typeGroup.isPresent()) {
                    return new ResponseEntity<>(new AddUserResponse("No such user type", uids[i], names[i], genders[i], type), HttpStatus.BAD_REQUEST);
                }
                user.setType(typeGroup.get());
            }
            if (!genders[i].equals("男") && !genders[i].equals("女")) {
                return new ResponseEntity<>(new AddUserResponse("No such gender", uids[i], names[i], genders[i], type), HttpStatus.BAD_REQUEST);
            }
            user.setGender(genders[i]);
            user.setYear(year);
        }

        for (int i = 0; i < uids.length; i++) {
            userRepository.save(users[i]);
        }

        return new ResponseEntity<>(new AddUserResponse("OK", null, null, null, null), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/delete")
    @Authorization
    public ResponseEntity<DeleteUserResponse> deleteUser(@RequestBody DeleteUserRequest request) {
        String[] uids = request.getUids();
        if (uids == null) {
            return new ResponseEntity<>(new DeleteUserResponse("Invalid request", null), HttpStatus.BAD_REQUEST);
        }

        List<String> fails = new ArrayList<>();
        for (String uid : uids) {
            if (uid == null) {
                continue;
            }
            Optional<UserEntity> ret = userRepository.findById(uid);
            if (ret.isPresent()) {
                userRepository.delete(ret.get());
            } else {
                fails.add(uid);
            }

        }
        String[] ret = new String[fails.size()];
        for (int i = 0; i < fails.size(); i++) {
            ret[i] = fails.get(i);
        }
        return new ResponseEntity<>(new DeleteUserResponse("OK", ret), HttpStatus.OK);
    }

    @PostMapping(path = "/modify/pwd")
    @Authorization
    public ResponseEntity<ModifyPwdResponse> modifyPwd(@CurrentUser UserEntity user,
                                                       @RequestBody ModifyPwdRequest request) {

        String name = user.getName();
        if (!user.getHashedPassword().equals(SecurityUtils.getHashedPasswordByPasswordAndSalt(request.getOldPwd(), user.getSalt()))) {
            return new ResponseEntity<>(new ModifyPwdResponse("incorrect password", user.getUid(), name), HttpStatus.UNAUTHORIZED);
        } else if (request.getNewPwd().length() < 6) {
            return new ResponseEntity<>(new ModifyPwdResponse("password must have at least 6 characters", user.getUid(), name), HttpStatus.UNAUTHORIZED);
        }
        user.setHashedPassword(SecurityUtils.getHashedPasswordByPasswordAndSalt(request.getNewPwd(), user.getSalt()));
        userRepository.save(user);

        return new ResponseEntity<>(new ModifyPwdResponse("OK", user.getUid(), name), HttpStatus.OK);
    }

    @PostMapping(path = "/reset/pwd")
    @Authorization
    public ResponseEntity<BasicResponse> resetPwd(@RequestBody BasicUserRequest request) {
        String uid = request.getUid();
        Optional<UserEntity> ret = userRepository.findById(uid);

        if (!ret.isPresent()) {
            return new ResponseEntity<>(new BasicResponse("non-existent uid"), HttpStatus.BAD_REQUEST);
        }
        UserEntity tar = ret.get();
        tar.setHashedPassword(SecurityUtils.getHashedPasswordByPasswordAndSalt(Config.INIT_PWD, tar.getSalt()));

        userRepository.save(tar);

        return new ResponseEntity<>(new BasicResponse("OK"), HttpStatus.OK);
    }

    @PostMapping(path = "/modify/own/info")
    @Authorization
    public ResponseEntity<ModifyUserResponse> modifyOwnInfo(@CurrentUser UserEntity user,
                                                            @RequestBody ModifyUserRequest request) {
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getTelephone() != null) {
            user.setTelephone(request.getTelephone());
        }
        if (request.getIntro() != null) {
            user.setIntro(request.getIntro());
        }
        userRepository.save(user);
        return new ResponseEntity<>(new ModifyUserResponse("OK", user.getUid(), user.getName(), null, null,
                null, null, user.getEmail(), user.getTelephone(), user.getIntro()), HttpStatus.OK);
    }

    @PostMapping(path = "/modify/info")
    @Authorization
    public ResponseEntity<ModifyUserResponse> modifyInfo(@RequestBody ModifyUserRequest request) {
        String uid = request.getUid();
        Optional<UserEntity> ret = userRepository.findById(uid);

        if (!ret.isPresent()) {
            return new ResponseEntity<>(new ModifyUserResponse("Non-existent uid", uid, null, null, null,
                    null, null, null, null, null), HttpStatus.BAD_REQUEST);
        }
        UserEntity user = ret.get();
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getTelephone() != null) {
            user.setTelephone(request.getTelephone());
        }
        if (request.getIntro() != null) {
            user.setIntro(request.getIntro());
        }
        if (request.getYear() != null) {
            user.setYear(request.getYear());
        }

        if (request.getMajorClass() != null) {
            Optional<MajorClassEntity> majorClass = majorClassRepository.findByName(request.getMajorClass());
            if (majorClass.isPresent()) {
                user.setMajorClass(majorClass.get());
            } else {
                return new ResponseEntity<>(new ModifyUserResponse("No such class", user.getUid(), null, null, user.getMajorClass().getName(),
                        null, null, null, null, null), HttpStatus.BAD_REQUEST);
            }

        }

        if (request.getDeptName() != null) {
            Optional<DepartmentEntity> dept = departmentRepository.findByName(request.getDeptName());
            if (dept.isPresent()) {
                user.setDepartment(dept.get());
            } else {
                return new ResponseEntity<>(new ModifyUserResponse("No such department", user.getUid(), null, null, null,
                        null, user.getDepartment().getName(), null, null, null), HttpStatus.BAD_REQUEST);
            }
        }

        if (request.getType() != null) {
            Optional<TypeGroupEntity> typeGroup = typeGroupRepository.findByName(request.getType());
            if (!typeGroup.isPresent()) {
                return new ResponseEntity<>(new ModifyUserResponse("No such user type", user.getUid(), null, null, null,
                        user.getType().getName(), null, null, null, null), HttpStatus.BAD_REQUEST);
            }
            user.setType(typeGroup.get());
        }

        userRepository.save(user);
        return new ResponseEntity<>(new ModifyUserResponse("OK", user.getUid(), user.getName(), user.getGender(), user.readClassName(),
                user.readTypeName(), user.readDepartmentName(), user.getEmail(), user.getTelephone(), user.getIntro()), HttpStatus.OK);
    }

    @PostMapping(path = "/get/own/info")
    @Authorization
    public ResponseEntity<GetUserInfoResponse> getOwnInfo(@CurrentUser UserEntity curUser) {

        return new ResponseEntity<>(new GetUserInfoResponse("OK", curUser.getUid(), curUser.getName(),
                curUser.readTypeName(), curUser.getEmail(), curUser.getTelephone(), curUser.getIntro(),
                curUser.getGender(), curUser.readDepartmentName(), curUser.readClassName(), curUser.getYear()), HttpStatus.OK);
    }

    @PostMapping(path = "/get/info")
    @Authorization
    public ResponseEntity<GetUserInfoResponse> getInfo(@RequestBody GetUserInfoRequest request) {
        String uid = request.getUid();
        Optional<UserEntity> ret = userRepository.findById(uid);

        if (!ret.isPresent()) {
            return new ResponseEntity<>(new GetUserInfoResponse("Non-existent uid", uid, null, null,
                    null, null, null, null, null, null, null), HttpStatus.BAD_REQUEST);

        }
        UserEntity user = ret.get();
        return new ResponseEntity<>(new GetUserInfoResponse("OK", user.getUid(), user.getName(),
                user.readTypeName(), user.getEmail(), user.getTelephone(), user.getIntro(),
                user.getGender(), user.readDepartmentName(), user.readClassName(), user.getYear()), HttpStatus.OK);
    }

    @PostMapping(path = "/query")
    @Authorization
    @Transactional(rollbackFor = {})
    public ResponseEntity<QueryUsersResponse> queryUsers(@RequestBody QueryUsersRequest request) {
        Short departmentId = null;
        if (request.getDepartment() != null) {
            Optional<DepartmentEntity> dept = departmentRepository.findByName(request.getDepartment());
            if (!dept.isPresent()) {
                return new ResponseEntity<>(new QueryUsersResponse("Non-exist department", null, null,
                        null, null, null, null), HttpStatus.BAD_REQUEST);
            }
            departmentId = dept.get().getId();
        }
        Short typeId = null;
        if (request.getType() != null) {
            Optional<TypeGroupEntity> type = typeGroupRepository.findByName(request.getType());
            if (!type.isPresent()) {
                return new ResponseEntity<>(new QueryUsersResponse("Non-exist type", null, null,
                        null, null, null, null), HttpStatus.BAD_REQUEST);
            }
            typeId = type.get().getId();
        }

        String uidLike = (request.getUid() != null) ? "%" + request.getUid() + "%" : "%";
        String nameLike = (request.getName() != null) ? "%" + request.getName() + "%" : "%";
        List<UserEntity> ret = userRepository.findByUidLikeAndNameLike(uidLike, nameLike);

        // List<UserEntity> ret = queryService.queryUsers(request.getUid(), request.getName(), departmentId, typeId);
        List<String> uids = new ArrayList<>();

        List<String> names = new ArrayList<>();
        List<String> depts = new ArrayList<>();
        List<String> genders = new ArrayList<>();
        List<String> types = new ArrayList<>();
        List<Integer> years = new ArrayList<>();
        for (UserEntity user : ret) {
            DepartmentEntity dept = user.getDepartment();
            TypeGroupEntity type = user.getType();
            if ((departmentId != null && (dept == null || dept.getId() != departmentId)) ||
                    (typeId != null && (type == null || type.getId().equals(typeId)))) {
                continue;
            }
            uids.add(user.getUid());
            names.add(user.getName());
            depts.add(user.readDepartmentName());
            genders.add(user.getGender());
            types.add(user.readTypeName());
            years.add(user.getYear());

        }

        return new ResponseEntity<>(new QueryUsersResponse("OK", uids, names, depts, genders, types, years), HttpStatus.OK);
    }

    @PutMapping(path = "/photo")
    @Authorization
    public ResponseEntity<ModifyPhotoResponse> modifyPhoto(@CurrentUser UserEntity user,
                                                           @RequestBody ModifyPhotoRequest request) {
        File file = new File(Config.USER_RESOURCE_DIR + user.getUid());
        if (!file.exists() || !file.isDirectory()) {
            if (!file.mkdirs()) {
                return new ResponseEntity<>(new ModifyPhotoResponse("Cannot open user's resource file"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(String.format(Config.USER_PHOTO_DIR_PATTERN, user.getUid()));
            fout.write(request.getFile());
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new ModifyPhotoResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            try {
                if (fout != null) {
                    fout.close();
                }
            } catch (IOException e) {

            }
        }
        return new ResponseEntity<>(new ModifyPhotoResponse("OK"), HttpStatus.CREATED);
    }

    @GetMapping(path = "{uid}/photo")
    // @Authorization
    public void getPhoto(@PathVariable String uid, HttpServletResponse response) {
        File file = new File(String.format(Config.USER_PHOTO_DIR_PATTERN, uid));
        FileInputStream fin = null;
        OutputStream stream = null;
        if (file.exists()) {
            try {
                fin = new FileInputStream(file);
                byte[] data = new byte[fin.available()];
                fin.read(data);
                response.setContentType("image/jpg");
                stream = response.getOutputStream();
                stream.write(data);
                stream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fin != null) {
                        fin.close();
                    }
                    if (stream != null) {
                        stream.close();
                    }
                } catch (IOException e) {

                }
            }
        }

    }
}
