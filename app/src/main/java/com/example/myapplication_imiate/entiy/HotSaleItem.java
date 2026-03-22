package com.example.myapplication_imiate.entiy;

import java.util.ArrayList;
import java.util.List;

public class HotSaleItem {
    public int id;
    public  String tag;// 一旦赋值，不可改变
    public  String name;
    public  String percentRange;
    public  String period;
    public  String minInvestment;
    public int reservation;

    public HotSaleItem(String tag, String name, String percentRange, String period, String minInvestment) {
        this.tag = tag;
        this.name = name;
        this.percentRange = percentRange;
        this.period = period;
        this.minInvestment = minInvestment;
        this.reservation=0;
    }

    public HotSaleItem() {

    }


    public String getTag() {
        return tag;
    }

    public String getName() {
        return name;
    }

    public String getPercentRange() {
        return percentRange;
    }

    public String getPeriod() {
        return period;
    }

    public String getMinInvestment() {
        return minInvestment;
    }
    public int getReservation(){return reservation;}
}

