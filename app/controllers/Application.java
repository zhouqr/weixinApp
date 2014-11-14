package controllers;

import controllers.init.JSFile;
import play.mvc.Controller;

public class Application extends BaseController {

	 public static void index() {
	        String appjs=JSFile.get("/public/appp");
	        render(appjs);
	        //renderText(JSFile.get("/public/app"));
	 }

}