package controllers;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import play.data.validation.Required;
import play.libs.Crypto;

import com.google.gson.JsonObject;

import controllers.init.JSFile;
import models.User;
import models.bean.ResultInfo;

/**
 * 登录服务
 * @author zhou
 *
 */
public class LoginService extends BaseController{
	
	public static void index() {
        String appjs=JSFile.get("/public/appp");
        render(appjs);
        //renderText(JSFile.get("/public/app"));
    }
	/**
	 * 登入
	 * @param body
	 */
	 public static void login(@Required String username,@Required String password){

	        User user=User.find("username=?",username).first();
	        
	        if(user==null) {
	            renderJSON(ResultInfo.error("用户名不存在！"));
	        }
	        //密码sha256加密
	        if(!Crypto.passwordHash(password, Crypto.HashType.SHA256).equals(user.password)) {
	            renderJSON(ResultInfo.error("密码错误！"));
	        }

	        session.put("username", username);
	        session.put("userid",user.id);
	        //返回用户信息
	        renderJSON(ResultInfo.success(user));
	    }
	 	
	 	/**
	 	 * 登出
	 	 */
	    public static void logout() {
	        session.remove("username");
	        session.remove("userid");
	        renderJSON(ResultInfo.success());
	    }
	    
	    /**
	     * 获取当前用户
	     */
	    public static void getUser(){
	    	User user = BaseController.currentUser();
	    	JSONObject obj = new JSONObject();
	    	obj.put("id",user.id);
	    	obj.put("username", user.username);
	    	renderJSON(ResultInfo.success(obj));
	    }
}
