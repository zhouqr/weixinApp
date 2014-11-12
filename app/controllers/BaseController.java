package controllers;

import java.util.HashMap;
import java.util.Map;

import models.User;
import models.bean.ResultInfo;
import play.data.validation.Error;
import play.mvc.Before;
import play.mvc.Controller;

public class BaseController extends Controller{
	/**
	 * 参数验证
	 */
	@Before
	public static void checkParameter() {
		if (validation.hasErrors()) {
			StringBuffer errorBuffer = new StringBuffer();
			for (Error error : validation.errors()) {
				errorBuffer.append(error.message() + ",");
			}
			String errorString = errorBuffer.toString();
			if (errorString.length() > 0) {
				errorString = errorString.substring(0, errorString.length() - 1);
			}
			renderJSON(ResultInfo.error(errorString));
		}
	}
	
    @Before(unless = {"LoginService.login","LoginService.logout","LoginService.index"})
    public static void checkAuth() {
        if(!isLogin()){
            redirect("/login");
        }
    }

    private static boolean isLogin(){
        return session.get("username")!=null;
    }
   

//    private static Map<String, Object> getUserInfo(User user) {
//        Map<String,Object> userInfo=new HashMap<String,Object>();
//        userInfo.put("username", user.username);
//        userInfo.put("useid", user.id);
//        return userInfo;
//    }
    
    /**
     * 获得当前登录用户
     */
    public static User currentUser() {
        User user=User.find("username=?",session.get("username")).first();
        return user;
    }
}
