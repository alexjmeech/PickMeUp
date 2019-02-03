package org.hacksmu.pickmeup.postmates;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;

public class PostmatesWebCall<T>
{
	private static final Gson GSON = new Gson();
	
	private final String _apiKey;
	private final String _method;
	private final String _path;
	
	public PostmatesWebCall(String apiKey, String method, String customerId, String path) throws UnsupportedEncodingException
	{
		_apiKey = Base64.getEncoder().encodeToString((apiKey + ":").getBytes("utf-8"));
		_method = method;
		_path = "https://api.postmates.com/v1/" + path.replace(":customer_id", customerId);
	}
	
	public T call(Class<T> clazz, Map<String, String> paramData) throws IOException
	{
		CloseableHttpClient client = HttpClientBuilder.create().disableAuthCaching().build();
		HttpRequestBase http;
		if (_method.equalsIgnoreCase("GET"))
		{
			http = new HttpGet(_path);
		}
		else if (_method.equalsIgnoreCase("POST"))
		{
			http = new HttpPost(_path);
			List<NameValuePair> params = new ArrayList<>();
			paramData.forEach((key, value) -> params.add(new BasicNameValuePair(key, value)));
			((HttpPost)http).setEntity(new UrlEncodedFormEntity(params));
		}
		else
		{
			client.close();
			return null;
		}
		http.addHeader("Authorization", "Basic " + _apiKey);
		CloseableHttpResponse response = client.execute(http);
		int status = response.getStatusLine().getStatusCode();
		if (status == 200)
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer content = new StringBuffer();
			String inputLine;
			while ((inputLine = in.readLine()) != null)
			{
				content.append(inputLine);
			}
			in.close();
			response.close();
			client.close();
			
			return GSON.fromJson(content.toString(), clazz);
		}
		else
		{
			System.out.println(status);
			BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			StringBuffer content = new StringBuffer();
			String inputLine;
			while ((inputLine = in.readLine()) != null)
			{
				content.append(inputLine);
			}
			in.close();
			response.close();
			client.close();
			System.out.println(content.toString());
		}
		
		response.close();
		client.close();
		return null;
	}
}