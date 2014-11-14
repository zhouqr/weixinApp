package controllers;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import models.bean.ResultInfo;

import play.data.validation.Required;
import play.libs.WS;
import play.libs.WS.HttpResponse;

import util.DBUtil;

/**
 * 检索接口
 * @author zhou
 *
 */
public class Search extends BaseController{
	
	/**
	 * 根据关键词搜索文章
	 * @param keywords
	 */
	public static void searchArticals(@Required String keywords){
		//取文章接口
		String search_url= DBUtil.rb.getString("search_article.url");
		
		//参数
		Map<String, String> params=new HashMap<String, String>();
		params.put("keywords", keywords);
		
		HttpResponse res = WS.url(search_url).timeout("5s").setParameters(params).post();
		String res_string = res.getString();
		//转化为JSONObject
		JSONObject obj = JSONObject.fromObject(res_string);
		
		JSONArray articals = obj.getJSONArray("articals");
		
		//处理imurl中的图片链接
		for(int i=0;i<articals.size();i++){
			JSONObject json = articals.getJSONObject(i);
			String url = json.getString("imurl");
			if(url.contains("url=http"))
				json.put("imurl", url.substring(url.lastIndexOf("http")));
		}
		
		/*字段
		 * kewords 检索的关键词
		 * total_articals 检索总数目，大约数目
		 * articals 检索文章列表，字段如下
		 * {title 微信文章标题
		 * pubtime 微信文章发布时间
		 * summary 摘要,
		 * imurl 图片,
		 * url 文章链接,
		 * open_id 用来获取微信公众号详情，
		 * sourcename 公众号昵称,
		 *},
		 */
		
		renderJSON(ResultInfo.success(obj));
		
	}
	
	/**
	 * 根据关键词搜索公众号
	 * @param keywords
	 */
	public static void searchWxs(String keywords){
		//取文章接口
		String search_url= DBUtil.rb.getString("search_wx.url");
		
		//参数
		Map<String, String> params=new HashMap<String, String>();
		params.put("keywords", keywords);
		
		HttpResponse res = WS.url(search_url).timeout("5s").setParameters(params).post();
		String res_string = res.getString();
		//转化为JSONObject
		JSONObject obj = JSONObject.fromObject(res_string);
		/*字段
		 * total_num 检索数量
		 * kewords 检索关键词
		 * wxlist 微信公众号列表，下面是公众号相关字段
		 * {weixin_id 微信id
		 * open_id 用来获取微信公众号详情
		 * sourcename 微信号昵称,
		 * headim 头像,
		 * intro 简介,
		 * verti 认证信息,
		 * artical_title 最新文章标题,
		 * artical_pubtime 文章发布时间,
		 * artical_url 文章链接},
		 */
		renderJSON(ResultInfo.success(obj));
		
	}
	
	
	/**
	 * 根据关键词搜索公众号
	 * @param keywords
	 */
	public static void get_wx_openid(String open_id){
		//取文章接口
		String search_url= DBUtil.rb.getString("search_wx_detail.url");
		
		//参数
		Map<String, String> params=new HashMap<String, String>();
		params.put("open_id", open_id);
		
		HttpResponse res = WS.url(search_url).timeout("5s").setParameters(params).post();
		String res_string = res.getString();
		//转化为JSONObject
		JSONObject obj = JSONObject.fromObject(res_string);
		/*字段
		 * weixin_id 微信id
		 * sourcename 微信号昵称,
		 * headim 头像,
		 * intro 简介,
		 * verti 认证信息,
		 * total_articals 文章总数
		 * arcticals 文章列表，结构同article{},
		 */
		renderJSON(ResultInfo.success(obj));
		
	}
	
}
