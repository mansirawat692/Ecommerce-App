package com.example.testing.Model;

public class Orders {

    private String phone,city,date,time,address,pname,TotalAmount;



    public Orders(){

    }


    public Orders(String phone, String city, String date, String time, String address, String pname,String TotalAmount) {
        this.phone = phone;
        this.city = city;
        this.date = date;
        this.time = time;
        this.address = address;
        this.pname = pname;
    }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }



    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }



    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }



    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }



    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }



    public String getPname() { return pname; }
    public void setPname(String pname) { this.pname = pname; }


    public String getTotalAmount() { return TotalAmount; }
    public void setTotalAmount(String TotalAmount) { this.TotalAmount = TotalAmount; }
}
