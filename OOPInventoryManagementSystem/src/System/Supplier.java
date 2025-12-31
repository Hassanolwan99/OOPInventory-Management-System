package System;

public class Supplier {

    
    private String name;
    private String phone;
    private String email;

    // Constructor
    public Supplier(String name, String phone, String email) {
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

 
    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    
    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

   
    public void printInfo() {
        System.out.println("Supplier: " + name + ", Phone: " + phone + ", Email: " + email);
    }
}
