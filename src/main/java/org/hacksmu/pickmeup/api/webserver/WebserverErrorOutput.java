package org.hacksmu.pickmeup.api.webserver;

import java.io.PrintStream;
import java.io.PrintWriter;

@SuppressWarnings("serial")
public class WebserverErrorOutput extends RuntimeException
{
    public WebserverErrorOutput()
    {
        super();
    }
    
    public WebserverErrorOutput(String msg)
    {
        super(msg);
    }
    
	@Override
	public void printStackTrace() {}
	
	@Override
	public void printStackTrace(PrintStream ps) {}
	
	@Override
	public void printStackTrace(PrintWriter pw) {}
}