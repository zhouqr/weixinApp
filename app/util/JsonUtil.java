package util;

import java.lang.reflect.Type;
import java.text.DateFormat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonUtil {
	public static String bean2json(Object bean) {
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(java.sql.Date.class,
						new SQLDateSerializer())
				.registerTypeAdapter(java.util.Date.class,
						new UtilDateSerializer())
				.setDateFormat(DateFormat.LONG).setPrettyPrinting().create();
		return gson.toJson(bean);
	}

	public static <T> T json2bean(String json, Type type) {
		Gson gson = new GsonBuilder()
				.registerTypeAdapter(java.sql.Date.class,
						new SQLDateDeserializer())
				.registerTypeAdapter(java.util.Date.class,
						new UtilDateDeserializer())
				.setDateFormat(DateFormat.LONG).create();
		return gson.fromJson(json, type);

	}
	
	public static String toJson(Object src){
		if(src!=null){
			return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create().toJson(src);
		}else{
			return "{}";
		}
		
	}
	
	public static String toViewJson(Object src){
		if(src!=null){
			return new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create().toJson(src);
		}else{
			return "{}";
		}
		
	}

}
