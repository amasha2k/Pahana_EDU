package org.example.pahanaedu.dto;

public class CustomerRequest {
    private String accountNumber;
    private String name;
    private String address;
    private String telephone;
    private Integer unitsConsumed;

    // Getters and setters
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public Integer getUnitsConsumed() { return unitsConsumed; }
    public void setUnitsConsumed(Integer unitsConsumed) { this.unitsConsumed = unitsConsumed; }
} 