package com.example.demo.entities;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(
        name = "session",
        indexes = {
                @Index(name = "uidIndex", columnList = "uid", unique = true),
                @Index(name = "tokenIndex", columnList = "token", unique = true)
        }
)
public class SessionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String uid;

    /**
     * TODO: expire control
     */
    @GeneratedValue()
    private Timestamp timestamp;

    private Timestamp opt;

    private String token;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Timestamp getOpt() {
        return opt;
    }

    public void setOpt(Timestamp opt) {
        this.opt = opt;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
