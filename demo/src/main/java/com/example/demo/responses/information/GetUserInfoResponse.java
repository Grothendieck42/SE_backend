package com.example.demo.responses.information;

import org.jetbrains.annotations.Nls;

public class GetUserInfoResponse {
    private final @Nls
    String status;
    private final String uid;
    private final String name;
    private final String gender;
    private final String type;
    private final String email;
    private final String telephone;
    private final String intro;
    private final String department;
    private final String majorClass;
    private final Integer year;

    public GetUserInfoResponse(String status, String uid, String name, String type, String email,
                               String telephone, String intro, String gender, String department,
                               String majorClass, Integer year) {
        this.status = status;
        this.uid = uid;
        this.name = name;
        this.gender = gender;
        this.type = type;
        this.email = email;
        this.telephone = telephone;
        this.intro = intro;
        this.department = department;
        this.majorClass = majorClass;
        this.year = year;
    }

    public String getStatus() {
        return status;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public String getTelephone() {
        return telephone;
    }

    public String getIntro() {
        return intro;
    }

    public String getDepartment() {
        return department;
    }

    public String getMajorClass() {
        return majorClass;
    }

    public Integer getYear() {
        return year;
    }
}
