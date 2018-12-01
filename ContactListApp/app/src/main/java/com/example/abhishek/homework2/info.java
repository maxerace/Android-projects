package com.example.abhishek.homework2;

public class info {
    private String name;
    private Boolean check ;

    public info(String name) {
        this.name = name;
        this.check=false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }
}
