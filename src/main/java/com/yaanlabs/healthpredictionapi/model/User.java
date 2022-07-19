package com.yaanlabs.healthpredictionapi.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yaanlabs.healthpredictionapi.util.CommonUtils;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    @JsonProperty("user_id")
    private Integer userID;

    private String name;

    private String email;

    @Column(name = "mobile_no")
    @JsonProperty("mobile_no")
    private String mobileNo;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String password;

    private String address;

    private String gender;

    private Date birthday;

    private Double height;

    private Double weight;

    private String provider;

    @Lob
    @Column(name = "profile_image")
    private String profileImage;

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public boolean setName(String name) {
        if (CommonUtils.isNonEmptyDifferentString(name, this.name)) {
            this.name = name;
            return true;
        }
        return false;
    }

    public String getEmail() {
        return email;
    }

    public boolean setEmail(String email) {
        if (this.email == null || this.email.isEmpty()) {
            this.email = email;
            return true;
        }
        return false;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public boolean setMobileNo(String mobileNo) {
        if (CommonUtils.isNonEmptyDifferentString(mobileNo, this.mobileNo)) {
            this.mobileNo = mobileNo;
            return true;
        }
        return false;
    }

    public String getPassword() {
        return password;
    }

    public boolean setPassword(String password) {
        if (CommonUtils.isNonEmptyDifferentString(password, this.password)) {
            this.password = password;
            return true;
        }
        return false;
    }

    public void removePasswordInAPIResponse() {
        this.password = null;
    }

    public String getAddress() {
        return address;
    }

    public boolean setAddress(String address) {
        if (address != null) {
            this.address = address;
            return true;
        }
        return false;
    }

    public String getGender() {
        return gender;
    }

    public boolean setGender(String gender) {
        if (CommonUtils.isNonEmptyDifferentString(gender, this.gender)) {
            this.gender = gender;
            return true;
        }
        return false;
    }

    public Date getBirthday() {
        return birthday;
    }

    public boolean setBirthday(Date birthday) {
        if (birthday != null && !birthday.equals(this.birthday)) {
            this.birthday = birthday;
            return true;
        }
        return false;
    }

    public Double getHeight() {
        return height;
    }

    public boolean setHeight(Double height) {
        if (CommonUtils.isDifferentDouble(height, this.height)) {
            this.height = height;
            return true;
        }
        return false;
    }

    public Double getWeight() {
        return weight;
    }

    public boolean setWeight(Double weight) {
        if (CommonUtils.isDifferentDouble(weight, this.weight)) {
            this.weight = weight;
            return true;
        }
        return false;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

}
