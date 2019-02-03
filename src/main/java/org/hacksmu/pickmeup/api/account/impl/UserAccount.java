package org.hacksmu.pickmeup.api.account.impl;
import org.hacksmu.pickmeup.api.account.AccessLevel;
import org.hacksmu.pickmeup.api.account.api.IUserAccount;

public class UserAccount implements IUserAccount
{
    private int id;
    private String email;
    private String passwordHash;
    private AccessLevel accessLevel;

    public UserAccount(int id, String email, String passwordHash, AccessLevel accessLevel) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.accessLevel = accessLevel;
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
    public AccessLevel getAccessLevel()
    {
    	return accessLevel;
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