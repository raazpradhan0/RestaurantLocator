package com.example.restaurantlocator.Model;

public class Comments
{
    public String comment, date, name, phone, time, profileImage;

    public Comments()
    {

    }

    public Comments(String comment, String date, String name, String phone, String time, String profileImage)
    {
        this.comment = comment;
        this.date = date;
        this.name = name;
        this.phone = phone;
        this.time = time;
        this.profileImage = profileImage;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
