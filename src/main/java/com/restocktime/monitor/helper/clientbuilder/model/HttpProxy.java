package com.restocktime.monitor.helper.clientbuilder.model;

public class HttpProxy {
    private String host;
    private Integer port;
    private String username;
    private String password;

    public HttpProxy(String host, Integer port, String username, String password) {
        this.host = host;
        this.port = port;
        this.username = username;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
