package org.hacksmu.pickmeup;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.internalServerError;
import static spark.Spark.notFound;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hacksmu.pickmeup.api.NumberUtil;
import org.hacksmu.pickmeup.api.PickMeUpAuthorizer;

import spark.ModelAndView;
import spark.Spark;
import spark.template.velocity.VelocityTemplateEngine;

public class Main
{
	public static void main(String[] args)
	{
		{
			int port = 8080;
			
			try
			{
				File configFile = new File(new File(".").getCanonicalPath() + File.separator + "config.dat");
				if (configFile.exists())
				{
					List<String> lines = Files.readAllLines(configFile.toPath(), Charset.defaultCharset());
					
					for (String line : lines)
					{
						if (line.startsWith("#"))
						{
							continue;
						}
						
						String[] split = line.split(" ");
						
						if (split.length > 1)
						{
							String key = split[0];
							String value = split[1];
							
							if (key.equalsIgnoreCase("port") && NumberUtil.isInteger(value))
							{
								port = Integer.parseInt(value);
							}
						}
					}
				}
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
			
			Spark.port(port);
		}
		
		internalServerError((req, res) ->
		{
			res.type("application/json");
			return "{\"statusCode\":\"500\",\"error\":\"Internal Server Error\",\"message\":\"An internal server error occurred\"}";
		});
		notFound((req, res) ->
		{
			res.type("application/json");
			return "{\"statusCode\":\"404\",\"error\":\"Not Found\"}";
		});
		
		//before(PickMeUpAuthorizer.getInitialFilter());
		get("/", (request, response) -> "PickMeUp Web System");
		get("/testvelocity", (request, response) ->
		{
			Map<String, Object> model = new HashMap<>();
			model.put("name", "Alex");
		    return new VelocityTemplateEngine().render(
		        new ModelAndView(model, "testvelocity.vm")
		    );
		});
	}
}