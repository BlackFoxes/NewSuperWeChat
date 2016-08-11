package cn.ucai.fulicenter.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/11.
 */
public class BillBean implements Serializable {
    String orderName;
    String orderPhone;
    String orderProvince;
    String orderStreet;

    public BillBean(String orderName, String orderPhone, String orderProvince, String orderStreet) {
        this.orderName = orderName;
        this.orderPhone = orderPhone;
        this.orderProvince = orderProvince;
        this.orderStreet = orderStreet;

    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderPhone() {
        return orderPhone;
    }

    public void setOrderPhone(String orderPhone) {
        this.orderPhone = orderPhone;
    }

    public String getOrderProvince() {
        return orderProvince;
    }

    public void setOrderProvince(String orderProvince) {
        this.orderProvince = orderProvince;
    }

    public String getOrderStreet() {
        return orderStreet;
    }

    public void setOrderStreet(String orderStreet) {
        this.orderStreet = orderStreet;
    }

    @Override
    public String toString() {
        return "BillBean{" +
                "orderName='" + orderName + '\'' +
                ", orderPhone='" + orderPhone + '\'' +
                ", orderProvince='" + orderProvince + '\'' +
                ", orderStreet='" + orderStreet + '\'' +
                '}';
    }
}
