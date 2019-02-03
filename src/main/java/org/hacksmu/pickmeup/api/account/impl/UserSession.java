package org.hacksmu.pickmeup.api.account.impl;

import java.util.UUID;

import org.hacksmu.pickmeup.api.account.AccessLevel;
import org.hacksmu.pickmeup.api.account.api.IUserSession;

public class UserSession implements IUserSession {
	private int id;
	private String token;
	private AccessLevel accesslevel;

	public UserSession(int id, AccessLevel accesslevel) {
		this.id = id;
		this.token = UUID.randomUUID().toString();
		this.accesslevel = accesslevel;
	}

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