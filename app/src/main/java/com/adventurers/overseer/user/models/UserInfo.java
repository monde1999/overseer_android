package com.adventurers.overseer.user.models;

public class UserInfo {
    private int id = -1; //means a non-existent user
    private String username;
    private String firstName;
    private String password;
    private String lastName;
    public UserInfo(String username,String password,String firstName,String lastName){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }
    public UserInfo(String username,String password,String firstName,String lastName,int id){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.id = id;
    }
    public String getUsername(){return username;}
    public String getFirstName(){return firstName;}
    public String getLastName(){return lastName;}
    public String getPassword(){return  password;}
    public void setId(int id){this.id = id;}
    public int getId(){return id;}

    @Override
    public String toString() {
        return "UserInfo{" +
                "username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", password='" + password + '\'' +
                ", lastName='" + lastName + '\'' +
                ", id ='" + id + '\'' +
                '}';
    }
}