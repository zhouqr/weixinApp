package controllers.init;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import play.data.binding.Global;
import play.data.binding.TypeBinder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Global
public class JsonArrayBinder implements TypeBinder<JsonArray> {

    @Override
    public JsonArray bind(String name, Annotation[] annotations, String value, Class actualClass, Type genericType) throws Exception {
        return (JsonArray)new JsonParser().parse(value);  //To change body of implemented methods use File | Settings | File Templates.
    }
}
