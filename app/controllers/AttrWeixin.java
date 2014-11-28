package controllers;

import java.util.List;

import net.sf.json.JSONObject;

import play.data.validation.Min;
import play.data.validation.Required;
import models.AttrWeixinPerson;
import models.User;
import models.bean.ResultInfo;

/**
 * 关注微信公众号服务
 * @author zhou
 *
 */
public class AttrWeixin extends BaseController{
	
	/**
	 * 关注
	 * @param open_id
	 * @param sourcename
	 */
	public static void attr(@Required String open_id,@Required String sourcename,String headim,String intro){
		User user = BaseController.currentUser();
		AttrWeixinPerson  person = AttrWeixinPerson.find("open_id=? and user_id=? and delete_flag=0", open_id.trim(),user.id).first();
		//判断是否已关注
		if(person!=null)
			renderJSON(ResultInfo.error("已关注过该微信公众号"));
		//添加关注
		person = new AttrWeixinPerson();
		person.open_id = open_id.trim();
		person.sourcename = sourcename.trim();
		person.headim = headim;
		person.user_id = user.id;
		person.intro = intro;
		if(person.save().isPersistent())
			renderJSON(ResultInfo.success());
		else
			renderJSON(ResultInfo.error("关注失败！"));
	}
	
	/**
	 * 取消关注
	 * @param open_id
	 */
	public static void cancelAttr(@Required String open_id){
		User user = BaseController.currentUser();
		AttrWeixinPerson  person = AttrWeixinPerson.find("open_id=? and user_id=? and delete_flag=0", open_id.trim(),user.id).first();
		if(person==null)
			renderJSON(ResultInfo.error("未关注过该微信公众号"));
		
		person.delete_flag = 1;
		if(person.save().isPersistent())
			renderJSON(ResultInfo.success());
		else
			renderJSON(ResultInfo.error("取消关注失败！"));
	}
	
	/**
	 * 获取关注微信号列表
	 * @param page
	 * @param pageSize
	 */
	public static void getAttrList(@Required @Min(1)int page,@Required @Min(1)int pageSize){
		User user = BaseController.currentUser();
		//获取关注微信号列表
		List<AttrWeixinPerson> persons = AttrWeixinPerson.find("delete_flag=0 and user_id=? order by id desc", user.id).fetch(page, pageSize);
		//统计总页数
		Long count = AttrWeixinPerson.count("delete_flag=0 and user_id=?", user.id);
		
		JSONObject obj = new JSONObject();
		obj.put("bigTotalItems", count);
		obj.put("weixinPersons", persons);
		renderJSON(ResultInfo.success(obj));
	}
}
