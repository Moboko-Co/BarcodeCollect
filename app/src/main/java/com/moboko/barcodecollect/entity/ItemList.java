package com.moboko.barcodecollect.entity;

public class ItemList {
    int _id;
    String registerDay;
    String janCd;
    String itemNm;
    String categoryCd;
    int price;
    String taxDiv;
    int salePer;
    int taxPrice;
    String memo1;
    int favoriteFlag;
    boolean cbSelected;
    int seq;

    public int getTaxPrice() {
        return taxPrice;
    }

    public void setTaxPrice(int taxPrice) {
        this.taxPrice = taxPrice;
    }

    public boolean getCbSelected() {
        return cbSelected;
    }

    public void setCbSelected(boolean cbSelected) {
        this.cbSelected = cbSelected;
    }

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
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
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

    public int getFavoriteFlag() {
        return favoriteFlag;
    }

    public void setFavoriteFlag(int favoriteFlag) {
        this.favoriteFlag = favoriteFlag;
    }

    public String getItemNm() {
        return itemNm;
    }

    public void setItemNm(String itemNm) {
        this.itemNm = itemNm;
    }

    public int getSalePer() {
        return salePer;
    }

    public void setSalePer(int salePer) {
        this.salePer = salePer;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }
}