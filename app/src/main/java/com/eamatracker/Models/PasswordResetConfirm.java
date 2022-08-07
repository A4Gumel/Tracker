package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

public class PasswordResetConfirm {

    @SerializedName("new_password1")
    private String newPassword1;

    @SerializedName("new_password2")
    private String newPassword2;

    @SerializedName("uid")
    private String emailCode;

    private String token;

    public PasswordResetConfirm() {
    }


    public String getNewPassword1() {
        return newPassword1;
    }

    public void setNewPassword1(String newPassword1) {
        this.newPassword1 = newPassword1;
    }

    public String getNewPassword2() {
        return newPassword2;
    }

    public void setNewPassword2(String newPassword2) {
        this.newPassword2 = newPassword2;
    }

    public String getEmailCode() {
        return emailCode;
    }

    public void setEmailCode(String emailCode) {
        this.emailCode = emailCode;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
