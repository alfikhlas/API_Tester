package com.example.apitester;

public class apiClass {
    String name;
    String url;
    String status;

    public apiClass(String name, String url, String status) {
        this.name = name;
        this.url = url;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
