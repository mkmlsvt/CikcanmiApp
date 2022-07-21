package com.example.cikcanmiapp;

import java.util.ArrayList;
import java.util.Date;

public class User {
    public static User userCurrent;
    public static ArrayList<User> userInGroup = new ArrayList<>();
    public String _id;
    public String username;
    public String password;
    public String mail;
    public String durum;
    public String tel;
    public Date birthday;
    public ArrayList<String> groups = new ArrayList<String>();
}
