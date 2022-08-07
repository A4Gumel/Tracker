package com.eamatracker.Models;

import com.google.gson.annotations.SerializedName;

public class PasswordChange {

    @SerializedName("old_password")
    private String oldPassword;

    @SerializedName("new_password1")
    private String newPassword1;

    @SerializedName("new_password2")
    private String newPassword2;

    public PasswordChange() {
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

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
}
