package com.moboko.barcodecollect.entity;

public class DbFavoriteList {
    int _id;
    int favoriteFlag;


    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getFavoriteFlag() {
        return favoriteFlag;
    }

    public void setFavoriteFlag(int favoriteFlag) {
        this.favoriteFlag = favoriteFlag;
    }


}
