package com.library.dto;

public class ContactDto {
    private String email;
    private String phone;
    private String address;
    private String workingHours;

    public ContactDto(String email, String phone, String address, String workingHours) {
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.workingHours = workingHours;
    }

    // Геттери та сеттери
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getWorkingHours() { return workingHours; }
    public void setWorkingHours(String workingHours) { this.workingHours = workingHours; }
}
