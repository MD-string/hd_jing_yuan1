package com.hand.handtruck.Widget;

public class ViewData {
    public String name; //名字
    public int value;   //数值

    public int color;   //颜色
    public float percentage; //百分比
    public float angle; //角度

    public ViewData(int value, String name, float angle) {
    this.value = value;
    this.name = name;
    this.angle = angle;
    }
    
    public ViewData(int value, String name) {
    this.value = value;
    this.name = name;
    }
}