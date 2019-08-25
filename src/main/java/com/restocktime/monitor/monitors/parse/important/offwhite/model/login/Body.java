package com.restocktime.monitor.monitors.parse.important.offwhite.model.login;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Body {
    @JsonProperty("spree_user[email]")
    private String email;

    @JsonProperty("spree_user[password]")
    private String password;

    @JsonProperty("spree_user[remember_me]")
    private String rememberMe;

    private String commit;

    private String authenticity_token;
    private String utf8;

    public Body(String email, String password, String rememberMe, String commit) {
        this.email = email;
        this.password = password;
        this.rememberMe = rememberMe;
        this.commit = commit;
        this.authenticity_token = "";
        this.utf8="✓";
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(String rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getCommit() {
        return commit;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public String getAuthenticity_token() {
        return authenticity_token;
    }

    public void setAuthenticity_token(String authenticity_token) {
        this.authenticity_token = authenticity_token;
    }

    public String getUtf8() {
        return utf8;
    }

    public void setUtf8(String utf8) {
        this.utf8 = utf8;
    }
}
