package com.tools.linkmode;

public class User {
    private String username;
    private String password;
    private int login_attempts;

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", login_attempts=" + login_attempts +
                '}';
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLogin_attempts() {
        return login_attempts;
    }
    public void setLogin_attempts(int login_attempts) {
        this.login_attempts = login_attempts;
    }


}
