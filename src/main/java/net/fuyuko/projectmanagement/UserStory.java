package net.fuyuko.projectmanagement;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "userstory")
public class UserStory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "want", nullable = false)
    private String want;

    @Column(name = "sothat", nullable = false)
    private String soThat;

    public UserStory() {
    }

    public UserStory(Integer id, Integer userId, String want, String soThat) {
        this.id = id;
        this.userId = userId;
        this.want = want;
        this.soThat = soThat;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getWant() {
        return want;
    }

    public String getSoThat() {
        return soThat;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public void setWant(String want) {
        this.want = want;
    }

    public void setSoThat(String soThat) {
        this.soThat = soThat;
    }

}

