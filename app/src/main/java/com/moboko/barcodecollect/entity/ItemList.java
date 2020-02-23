package com.moboko.barcodecollect.entity;

public class ItemList {
    int _id;
    String registerDay;
    String janCd;
    String categoryCd;
    int Price;
    String taxDiv;
    String memo1;
    String memo2;
    int favoriteFlag;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getRegisterDay() {
        return registerDay;
    }

    public void setRegisterDay(String registerDay) {
        this.registerDay = registerDay;
    }

    public String getJanCd() {
        return janCd;
    }

    public void setJanCd(String janCd) {
        this.janCd = janCd;
    }

    public String getCategoryCd() {
        return categoryCd;
    }

    public void setCategoryCd(String categoryCd) {
        this.categoryCd = categoryCd;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public String getTaxDiv() {
        return taxDiv;
    }

    public void setTaxDiv(String taxDiv) {
        this.taxDiv = taxDiv;
    }

    public String getMemo1() {
        return memo1;
    }

    public void setMemo1(String memo1) {
        this.memo1 = memo1;
    }

    public String getMemo2() {
        return memo2;
    }

    public void setMemo2(String memo2) {
        this.memo2 = memo2;
    }

    public int getFavoriteFlag() {
        return favoriteFlag;
    }

    public void setFavoriteFlag(int favoriteFlag) {
        this.favoriteFlag = favoriteFlag;
    }
}