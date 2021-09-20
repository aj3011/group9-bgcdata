package com.example;

public class imgdata {

  public String imgname;
  public String alttext;
  public String imgurl;

  public void setAlttext(String t) {
    this.alttext = t;
  }

  public void setImgname(String n) {
    this.imgname = n;
  }

  public void setImgurl(String u) {
    this.imgurl = u;
  }

  public String getAlttext() {
    return this.alttext;
  }
  
  public String getImgname() {
    return this.imgname;
  }

  public String getImgurl() {
    return this.imgurl;
  }

 }