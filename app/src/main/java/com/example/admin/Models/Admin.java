package com.example.admin.Models;

public class Admin
{
    private String Password,name,phone,department;

    public Admin()
    {

    }

    public Admin(String password, String name, String phone, String department) {
        Password = password;
        this.name = name;
        this.phone = phone;
        this.department = department;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
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

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
