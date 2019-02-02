package org.hacksmu.pickmeup.api.account.api;

import org.hacksmu.pickmeup.api.webserver.AccessLevel;

public interface IUserSession
{
	int getAccountID();
	
	String getToken();
	
	AccessLevel getAccessLevel();
}