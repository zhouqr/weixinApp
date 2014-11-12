package controllers.init;

import play.data.validation.Error;
import play.mvc.Before;
import play.mvc.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class JsonController extends Controller {

    @Before
    public static void checkParams() {
        String message = "";
        if (validation.hasErrors()) {
            Map<String, List<Error>> map=validation.errorsMap();
            for (String key : map.keySet()) {
                List<Error> errors = map.get(key);
                for (Error e : errors) {
                    message+=key + " is " + e.message();
                }
            }
            error(400,message);
        }
    }





}
