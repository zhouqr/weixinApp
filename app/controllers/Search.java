package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import models.AttrWeixinPerson;
import models.User;
import models.bean.ResultInfo;

import play.Logger;
import play.Play;
import play.cache.Cache;
import play.data.validation.Required;
import play.libs.WS;
import play.libs.WS.HttpResponse;

import util.DBUtil;
import util.DateUtil;

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
	public static void searchArticals(@Required String keywords, int page){
		if(page==0)
			page = 1;
		//取文章接口
		String search_url= DBUtil.rb.getString("search_article.url");
		
		//参数
		Map<String, String> params=new HashMap<String, String>();
		params.put("keywords", keywords);
		params.put("page", String.valueOf(page));
		
		HttpResponse res = WS.url(search_url).timeout("5s").setParameters(params).post();
		String res_string = res.getString();
		//转化为JSONObject
		JSONObject obj = JSONObject.fromObject(res_string);
		
		//计算总页数
		String totalnums= obj.getString("total_articals");
		Long count = 0l;
		if(!totalnums.equals(""))
			count = Long.parseLong(totalnums.replace(",", ""));
		obj.put("bigTotalItems", count);
		
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
	 * 根据关键词搜索文章
	 * @param keywords
	 */
	public static void searchArticals_local(@Required String keywords,@Required String st,@Required String et, int page){
		if(page==0)
			page = 1;
		// 指定记录
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        JSONArray jarrArtical = new JSONArray();
        
        //统计的总数
        Long count = 0l;

        try {
        	//计算数据开始结束为止
            int start = (page-1)*10;
        	        	
            conn = DBUtil.getWeixinDBConn();  
            
            //获取数据列表
            String sql = "";
            sql = " SELECT *" +
					" FROM yqpt_weixin_news_info_sg_sphinxse AS dse" +
					" LEFT JOIN yqpt_weixin_news_info_sg AS d USING(news_id) " +
        			" WHERE query='" + keywords +
					" ;sort=attr_desc:pubtime;range=pubtime,"+DateUtil.StringDate2Long(st)+","+DateUtil.StringDate2Long(et)+";mode=boolean;maxmatches=3200;offset="+start+";limit="+10+";'"; 
            System.out.println(sql);
            stmt = conn.prepareStatement(sql);
            resultSet = stmt.executeQuery();
            while (resultSet.next()) {
            	JSONObject jsonTmp = new JSONObject();
            	
            	//采集后的id
            	String _news_id = resultSet.getString("news_id");
            	
            	//微信文章标题
            	String _title = resultSet.getString("title");
            	//微信用户id
            	String _open_id = resultSet.getString("open_id");
            	//微信用户昵称
            	String _source_name = resultSet.getString("source_name");
            	//文章发布时间
            	String _pubtime = resultSet.getString("pubtime");
            	if(_pubtime.contains(".0")){
            		_pubtime = _pubtime.substring(0, _pubtime.lastIndexOf(".0"));
            	}
            	
            	//文章url 
            	String _content_url = resultSet.getString("content_url");
            	
            	//图片
            	String _image_url = resultSet.getString("imurl");
            	if(_image_url.contains("url=http"))
            		_image_url = _image_url.substring(_image_url.lastIndexOf("http"));
            	//文章类型  1文本消息             	2 图片消息            	3 图文消息 
            	String _news_type = resultSet.getString("news_type");

            	jsonTmp.put("news_id", _news_id);
            	jsonTmp.put("title", _title);
            	jsonTmp.put("open_id", _open_id);
            	jsonTmp.put("sourcename", _source_name);
            	jsonTmp.put("pubtime", _pubtime);
            	jsonTmp.put("url", _content_url);
            	jsonTmp.put("news_type", _news_type);
            	jsonTmp.put("imurl", _image_url);
            	
	            	//微信文章内容,截取100字  content，已有摘要字段
	            String _content = resultSet.getString("summary");
	            if(_content.length()>100){
	            	_content = _content.substring(0, 100)+"...";
	            }
	            jsonTmp.put("summary", _content);
            	jarrArtical.add(jsonTmp);
            }
            resultSet.close();
            stmt.close(); 
            
            
          //先取缓存数据
    		String key = String.format("/search/searchArticals_local?keywords=%s&st=%s&et=%s", keywords,st,et);
    		Logger.info(key+"---"+Cache.get(key));
    		if(Cache.get(key)!=null)
    			count = (Long)Cache.get(key);
    		else{
	            sql = " SELECT count(1) AS num" +
				" FROM yqpt_weixin_news_info_sg_sphinxse AS dse" +
				" LEFT JOIN yqpt_weixin_news_info_sg AS d USING(news_id) " +
				" WHERE query='" + keywords +
				" ;sort=attr_desc:pubtime;range=pubtime,"+DateUtil.StringDate2Long(st)+","+DateUtil.StringDate2Long(et)+";mode=boolean;maxmatches=3200;limit=3200'"; 
	            
	            stmt = conn.prepareStatement(sql);
	            resultSet = stmt.executeQuery();
	            if (resultSet.next()) {
		        	count = resultSet.getLong("num");
		        }
	            resultSet.close();
	            stmt.close(); 
	            Cache.set(key, count, Play.configuration.getProperty("countCacheTime"));
    		}
            
        } catch (Exception e) {
        	Logger.error("Result CheckUserInfoODBC:"+e.getMessage());
        } finally {
        	DBUtil.closeConn(conn);
        }
		
      //转化为JSONObject
		JSONObject obj = new JSONObject();
		obj.put("articals", jarrArtical);
		obj.put("bigTotalItems", count);
		renderJSON(ResultInfo.success(obj));
		
	}
	
	/**
	 * 根据关键词搜索公众号
	 * @param keywords
	 */
	public static void searchWxs(@Required String keywords, int page){
		if(page == 0)
			page = 1;
		//取文章接口
		String search_url= DBUtil.rb.getString("search_wx.url");
		
		//参数
		Map<String, String> params=new HashMap<String, String>();
		params.put("keywords", keywords);
		params.put("page", String.valueOf(page));
		
		HttpResponse res = WS.url(search_url).timeout("5s").setParameters(params).post();
		String res_string = res.getString();
		//转化为JSONObject
		JSONObject obj = JSONObject.fromObject(res_string);
		
		//计算总页数
		String totalnums= obj.getString("total_num");
		Long count =0l;
		if(!totalnums.equals(""))
			count = Long.parseLong(totalnums.replace(",", ""));
		obj.put("bigTotalItems", count);
		
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
	 * 获取公众号详情
	 * @param 
	 */
	public static void get_wx_openid(@Required String open_id, int page){
		if(page==0)
			page=1;
		open_id = open_id.trim();
		//取文章接口
		String search_url= DBUtil.rb.getString("search_wx_detail.url");
		
		//参数
		Map<String, String> params=new HashMap<String, String>();
		params.put("open_id", open_id);
		params.put("page", String.valueOf(page));
		
		HttpResponse res = WS.url(search_url).timeout("5s").setParameters(params).post();
		String res_string = res.getString();
		//转化为JSONObject
		JSONObject obj = JSONObject.fromObject(res_string);
		
		//计算总页数
		String totalnums= obj.getString("total_articals");
		Long count =0l;
		if(!totalnums.equals(""))
			count = Long.parseLong(totalnums.replace(",", ""));
		obj.put("bigTotalItems", count);
		
		/*字段
		 * weixin_id 微信id
		 * sourcename 微信号昵称,
		 * headim 头像,
		 * intro 简介,
		 * verti 认证信息,
		 * total_articals 文章总数
		 * arcticals 文章列表，结构同article{},
		 */
		User user = BaseController.currentUser();
		//是否已关注
		AttrWeixinPerson person = AttrWeixinPerson.find("open_id=? and user_id=? and delete_flag=0", open_id, user.id).first();
		if(person!=null){
			obj.put("isAttr", true);
		}else{
			obj.put("isAttr", false);
		}
		//关注和取消关注参数
		obj.put("open_id", open_id);
		
		renderJSON(ResultInfo.success(obj));
		
	}
	
	/**
	 * 检索某微信公众号发布的命中关键词的文章
	 * @param keywords
	 * @param open_id
	 */
	public static void search_by_openid(@Required String keywords,@Required String open_id,@Required String st,@Required String et, int page){
		if(page==0)
			page = 1;
		// 指定记录
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        JSONArray jarrArtical = new JSONArray();
        Long count = 0l;
        
        try {
        	
            conn = DBUtil.getWeixinDBConn();  
            int start = (page-1)*10;
            //获取数据列表
            String sql = "";
            sql = " SELECT * from ( select * " +
					" FROM yqpt_weixin_news_info_sg_sphinxse AS dse" +
					" LEFT JOIN yqpt_weixin_news_info_sg AS d USING(news_id) " +
        			" WHERE query='" + keywords +
					" ;sort=attr_desc:pubtime;range=pubtime,"+DateUtil.StringDate2Long(st)+","+DateUtil.StringDate2Long(et)+";mode=boolean;maxmatches=3200;limit=3200;' ) a where a.open_id='"+open_id +"' limit "+start+","+10+";";
            
         
            System.out.println(sql);
            stmt = conn.prepareStatement(sql);
            resultSet = stmt.executeQuery();
            while (resultSet.next()) {
            	JSONObject jsonTmp = new JSONObject();
            	
            	//采集后的id
            	String _news_id = resultSet.getString("news_id");
            	
            	//微信文章标题
            	String _title = resultSet.getString("title");
            	//微信用户id
            	String _open_id = resultSet.getString("open_id");
            	//微信用户昵称
            	String _source_name = resultSet.getString("source_name");
            	//文章发布时间
            	String _pubtime = resultSet.getString("pubtime");
            	if(_pubtime.contains(".0")){
            		_pubtime = _pubtime.substring(0, _pubtime.lastIndexOf(".0"));
            	}
            	
            	//文章url 
            	String _content_url = resultSet.getString("content_url");
            	
            	//图片
            	String _image_url = resultSet.getString("imurl");
            	if(_image_url.contains("url=http"))
            		_image_url = _image_url.substring(_image_url.lastIndexOf("http"));
            	//文章类型  1文本消息             	2 图片消息            	3 图文消息 
            	String _news_type = resultSet.getString("news_type");

            	jsonTmp.put("news_id", _news_id);
            	jsonTmp.put("title", _title);
            	jsonTmp.put("open_id", _open_id);
            	jsonTmp.put("sourcename", _source_name);
            	jsonTmp.put("pubtime", _pubtime);
            	jsonTmp.put("url", _content_url);
            	jsonTmp.put("news_type", _news_type);
            	jsonTmp.put("imurl", _image_url);
            	
	            	//微信文章内容,截取100字  content，已有摘要字段
	            String _content = resultSet.getString("summary");
	            if(_content.length()>100){
	            	_content = _content.substring(0, 100)+"...";
	            }
	            jsonTmp.put("summary", _content);
            	jarrArtical.add(jsonTmp);
            }
            resultSet.close();
            stmt.close(); 
            
            //统计总数
            //获取数据列表
          //先取缓存数据
    		String key = String.format("/search/search_by_openid?open_id=%s&keywords=%s&st=%s&et=%s", open_id,keywords,st,et);
    		Logger.info(key+"---"+Cache.get(key));
    		if(Cache.get(key)!=null)
    			count = (Long)Cache.get(key);
    		else{
	            sql = " SELECT count(1) AS num from ( select * " +
				" FROM yqpt_weixin_news_info_sg_sphinxse AS dse" +
				" LEFT JOIN yqpt_weixin_news_info_sg AS d USING(news_id) " +
				" WHERE query='" + keywords +
				" ;sort=attr_desc:pubtime;range=pubtime,"+DateUtil.StringDate2Long(st)+","+DateUtil.StringDate2Long(et)+";mode=boolean;maxmatches=3200;limit=3200;' ) a where a.open_id='"+open_id+"'"; 
	          
	            stmt = conn.prepareStatement(sql);
	            resultSet = stmt.executeQuery();
	            if (resultSet.next()) {
		        	count = resultSet.getLong("num");
		        }
	            resultSet.close();
	            stmt.close(); 
	            Cache.set(key, count, Play.configuration.getProperty("countCacheTime"));
    		}
        } catch (Exception e) {
        	Logger.error("Result CheckUserInfoODBC:"+e.getMessage());
        } finally {
        	DBUtil.closeConn(conn);
        }
		JSONObject obj = new JSONObject();
		obj.put("articals", jarrArtical);
		obj.put("bigTotalItems", count);
        renderJSON(ResultInfo.success(obj));
	}
	
}
