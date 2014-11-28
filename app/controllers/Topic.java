package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import play.Logger;
import play.data.validation.Min;
import play.data.validation.Required;
import play.mvc.Controller;

import util.DBUtil;
import util.DateUtil;
import util.JsonUtil;


import models.User;
import models.bean.ResultInfo;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 专题相关
 * @author zhou
 *
 */
public class Topic extends BaseController{
	
	
	/**
	 * 添加专题
	 * @param name
	 * @param keywords
	 * @param startTime
	 * @param endTime
	 * @param introduction
	 */
	public static void addTopic(@Required String name, @Required String keywords,@Required Date startTime,@Required Date endTime, String introduction){
		if(models.Topic.hasSameTopic(null, name))
			renderJSON(ResultInfo.error("已有同名的主题！"));
		Long topic_id = models.Topic.addTopic(name, keywords, startTime, endTime, introduction);
		renderJSON(ResultInfo.success(topic_id));
	}
	
	/**
	 * 判断是否有重名的专题
	 * @param id 如果是修改主题，请传入修改的主题的id
	 * @param name
	 * @return  result true有同名的主题  false没有同名的主题
	 */
	public static void hasSameTopic(Long id, @Required String name){
		boolean result = models.Topic.hasSameTopic(id, name);
		renderJSON(ResultInfo.success(result));
	}
	
	/**
	 * 修改专题
	 * @param id
	 * @param name
	 * @param keywords
	 * @param startTime
	 * @param endTime
	 * @param introduction
	 */
	public static void updateTopic(@Required Long id,@Required String name,@Required String keywords, @Required Date startTime,@Required Date endTime, String introduction){
		if(models.Topic.hasSameTopic(id, name))
			renderJSON(ResultInfo.error("已有同名的主题！"));
		
		boolean result = models.Topic.updateTopic(id, name, keywords, startTime, endTime, introduction);
		if(result)
		    renderJSON(ResultInfo.success());
		else
			renderJSON(ResultInfo.error("修改失败！"));
	}
	/**
	 * 删除
	 * @param id
	 */
	public static void delTopic(Long id){
		boolean result = models.Topic.delTopic(id);
		if(result)
			renderJSON(ResultInfo.success(null));
		else
			renderJSON(ResultInfo.error());
	}
	
	
	/**
	 * 获取当前用户的专题列表
	 */
	public static void topicList(@Required @Min(1)int page,@Required @Min(1)int pageSize){
		//专题列表
		List<models.Topic> topics = models.Topic.topicList(page,pageSize);
		User user = BaseController.currentUser();
		//统计页码
		Long count = models.Topic.count("delete_flag=0 and user_id=?", user.id);
		JSONObject obj = new JSONObject();
		obj.put("bigTotalItems", count);
		obj.put("topics", topics);
		
		renderJSON(ResultInfo.success(obj));
	}
	
	/**
	 * 获取专题详情
	 * @param id
	 */
	public static void topicDetail(@Required Long id){
		models.Topic topic = models.Topic.findById(id);
		renderJSON(JsonUtil.toViewJson(ResultInfo.success(topic)));
	}
	
	/**
	 * 获取最新的五条信息
	 * @param id
	 */
	public static void getTopicDocsTop5(@Required Long id){
		JSONArray docs = models.Topic.getTopicDocs(id, 1, 5,false);
		renderJSON(ResultInfo.success(docs));
	}

	/**
	 * 获取专题文章列表
	 * @param id
	 * @param page
	 * @param pageSize
	 */
	public static void getTopicDocs(@Required Long id, @Required @Min(1) int page ,@Required @Min(1)int pageSize){
		JSONArray docs = models.Topic.getTopicDocs(id, page, pageSize, true);
		Long count = models.Topic.countTopicDocs(id);
		JSONObject obj = new JSONObject();
		obj.put("bigTotalItems", count);
		obj.put("articles", docs);
		renderJSON(ResultInfo.success(obj));
	}
	
	/**
	 * 专题统计趋势图
	 * @param day
	 */
	public static void getDayCount(@Required Long id, @Required @Min(7) int day){
		models.Topic topic = models.Topic.findById(id);
		//横轴分类
		String[] categories = new String[day];
		//count数据
		Long[] data = new Long[day];
		//不统计当天，向前顺延1天
		day +=1;
		for(int i=day; i>1 ; i--){
			String date = DateUtil.getDayBeforeNoTime(i-1);
			categories[day-i] = date;
			data[day-i] = models.Topic.countDocs(date, topic.keywords);
		}
		
		JSONObject obj = new JSONObject();
		obj.put("categories", categories);
		obj.put("data", data);
		renderJSON(ResultInfo.success(obj));
	}
	
	/**
	 * 统计专题数量
	 */
	public static void topicCount() {
		User user = BaseController.currentUser();
		long count = models.Topic.count("delete_flag=0 and user_id=?", user.id);
		renderJSON(ResultInfo.success(count));
	}
	
	/**
	 * 获取发文章数目最多的公众号
	 * @param id
	 */
	public static void getTopPerson(@Required Long id,int num){
		if(num==0)
			num = 5;
		List<Object> persons = models.Topic.getTopicTopPerson(id,num);
		renderJSON(ResultInfo.success(persons));
	}
}
