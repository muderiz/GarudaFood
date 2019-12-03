/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imi.dolphin.sdkwebservice.GarudafoodModel;

/**
 *
 * @author Deka
 */
public class LdapModel {

    private String username;
    private String password;
    private String serverInfo;
    private String searchBase;
    private String loginPrincipal;

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

    public String getServerInfo() {
        return serverInfo;
    }

    public void setServerInfo(String serverInfo) {
        this.serverInfo = serverInfo;
    }

    public String getSearchBase() {
        return searchBase;
    }

    public void setSearchBase(String searchBase) {
        this.searchBase = searchBase;
    }

    public String getLoginPrincipal() {
        return loginPrincipal;
    }

    public void setLoginPrincipal(String loginPrincipal) {
        this.loginPrincipal = loginPrincipal;
    }

}
