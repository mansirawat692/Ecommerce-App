package com.example.testing.Model;

public class CartList {


    public String pid,pname,pprice,quantity,pdiscount,pdescription,image;

    public CartList() {
    }

    public CartList(String pid, String image, String pdescription,String pname, String pprice, String quantity, String pdiscount) {
        this.pid = pid;
        this.pname = pname;
        this.pprice = pprice;
        this.quantity = quantity;
        this.pdiscount = pdiscount;
        this.image=image;
        this.pdescription=pdescription;
    }



    public String getPid() { return pid; }
    public void setPid(String pid) { this.pid = pid; }


    public String getPname() { return pname; }
    public void setPname(String pname) { this.pname = pname; }



    public String getPprice() { return pprice; }
    public void setPprice(String pprice) { this.pprice = pprice; }



    public String getquantity() { return quantity; }
    public void setquantity(String quantity) { this.quantity = quantity; }



    public String getPdiscount() { return pdiscount; }
    public void setPdiscount(String pdiscount) { this.pdiscount = pdiscount; }


    public String getPdescription() { return pdescription; }
    public void setPdescription(String pdescription) { this.pdescription = pdescription; }



    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

}
