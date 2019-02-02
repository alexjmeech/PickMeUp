package org.hacksmu.pickmeup.api.account.impl;
import org.hacksmu.pickmeup.api.account.api.IUserAccount;

public class UserAccount implements IUserAccount
{
    private int id;
    private String email;
    private String passwordHash;

    public UserAccount(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
    }

    @Override
    public int getID() {
        return id;
    } 
    
    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPasswordHash() {
        return passwordHash;
    }

    @Override
    public void setEmail(String newEmail) {
        this.email = newEmail;
    }

    @Override
    public void setPasswordHash(String newPassword) {
        this.passwordHash = newPassword;
    }
}