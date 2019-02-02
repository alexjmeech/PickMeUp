package org.hacksmu.pickmeup.api.account.impl;
import org.hacksmu.pickmeup.api.account.api.IUserSession;
import org.hacksmu.pickmeup.api.webserver.AccessLevel;

public class UserSession implements IUserSession {
    private int id;
    private String token;
    private AccessLevel accesslevel;

    @Override
    public int getAccountID() {
        return id;
    }
    public String getToken() {
        return token;
    }
    public AccessLevel getAccessLevel() {
        return accesslevel;
    }
}




