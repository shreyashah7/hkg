package com.argusoft.hkg.web.security;

/**
 * Bean to store user information
 * @author raj
 */
public class TokenTransfer {

    private String username;
    private String password;
    private final String token;

    public TokenTransfer(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
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

}
