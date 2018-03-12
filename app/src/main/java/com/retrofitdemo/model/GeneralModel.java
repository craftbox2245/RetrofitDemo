package com.retrofitdemo.model;

/**
 * Created by CRAFT BOX on 2/24/2018.
 */

public class GeneralModel {

    String id;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String name;

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }

    boolean isCheck;
}
