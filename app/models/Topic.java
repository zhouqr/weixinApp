package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Transient;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import controllers.BaseController;

import play.Logger;
import play.Play;
import play.cache.Cache;
import play.data.validation.Min;
import play.data.validation.Required;
import play.db.jpa.Model;
import util.DBUtil;
import util.DateUtil;

/**
 * 专题实体类
 * @author zhou
 *
 */
@Entity
public class Topic extends Model{
	
	/**
	 * 专题名
	 */
	@Column(nullable=false)
	public String name;
	
	/**
	 * 专题关键词
	 */
	@Column(nullable=false)
	public String keywords;
	
	/**
	 * 专题开始时间
	 */
	@Column(nullable=false)
	public Date startTime;
	
	/**
	 * 专题结束时间
	 */
	@Column(nullable=false)
	public Date endTime;
	
	/**
	 * 专题创建时间
	 */
	@Column(nullable=false)
	public Date createTime;
	
	/**
	 * 专题简介
	 */
	@Column(nullable=true)
	public String introduction;
	
	/**
	 * 删除标识，1删除，0未删除
	 */
	@Column(nullable=false)
	public Integer delete_flag;
	
	/**
	 * 创建专题的用户id
	 */
	@Column(nullable=false)
	public Long user_id;
	
	@Transient
	public Long todayCount;
	
	/**
	 * 添加专题
	 * @return
	 */
	public static Long addTopic(String name, String keywords, Date startTime, Date endTime, String introduction){
		User user = BaseController.currentUser();
		Long topic_id = 0l;
		//保存专题信息
		Topic topic = new Topic();
		topic.name = name;
		topic.keywords = keywords;
		topic.startTime = startTime;
		topic.endTime = endTime;
		topic.createTime = new Date();
		topic.delete_flag = 0;
		topic.introduction = introduction;
		topic.user_id = user.id;
		boolean result = topic.save().isPersistent();
		Logger.info("添加专题--"+name);
		//保存成功则返回专题id
		if(result)
			topic_id = topic.id;
		return topic_id;
	}
	
	/**
	 * 判断有木有重名的专题
	 * @param id
	 * @param name
	 * @return
	 */
	public static boolean hasSameTopic(Long id, String name){
		User user = BaseController.currentUser();
		String sql = "delete_flag=0 and user_id=? and name=?";
		if(id!=null && id!=0){
			sql += " and id<>" +id;
		}
		models.Topic topic = Topic.find(sql,user.id,name).first();
		
		if(topic==null)
			return false;
		else
			return true;
	}
	/**
	 * 修改专题
	 * @return
	 */
	public static boolean updateTopic(Long id, String name, String keywords, Date startTime, Date endTime, String introduction){
		Topic topic = Topic.findById(id);
		topic.name = name;
		topic.keywords = keywords;
		topic.startTime = startTime;
		topic.endTime = endTime;
		topic.introduction = introduction;
		boolean result = topic.save().isPersistent();
		Logger.info("修改专题--"+name);
		return result;
	}
	
	/**
	 * 删除专题
	 * @param id
	 * @return
	 */
	public static boolean delTopic(Long id){
		Topic topic = Topic.findById(id);
		//删除标识为置为1
		topic.delete_flag = 1;
		Logger.info("删除专题---"+topic.name);
		return topic.save().isPersistent();
	}
	
	/**
	 * 获取用户专题
	 * @return
	 */
	public static List<Topic> topicList(int page,int pageSize){
		User user = BaseController.currentUser();
		List<Topic> topics = Topic.find("delete_flag=0 and user_id=? order by id desc", user.id).fetch(page, pageSize);
		for(Topic topic:topics){
			topic.todayCount = countTodayDocs(topic.keywords);
		}
		return topics;
	}
	

	/**
	 * 统计今天的新增数据
	 * @return
	 */
	public static Long countTodayDocs(String keywords){
		//缓存取数据
		String key = String.format("/topic/countTodayDocs?keywords=%s", keywords);
		Logger.info(key+"---"+Cache.get(key));
		if(Cache.get(key)!=null)
			return (Long)Cache.get(key);
		
		Long todayCount = 0l;
		
		//开始时间
		Date st = DateUtil.String2Date(DateUtil.getTodayDateNoTime()+" 00:00:00");
		//结束时间
		Date et = DateUtil.String2Date(DateUtil.getTodayDateNoTime()+" 23:59:59");
		// 指定记录
    	Connection conn = null;
        PreparedStatement stmt = null;
		conn = DBUtil.getWeixinDBConn();  
        String sql_count = "";
        sql_count = " SELECT count(1) AS num" +
					" FROM yqpt_weixin_news_info_sg_sphinxse AS dse" +
					" LEFT JOIN yqpt_weixin_news_info_sg AS d USING(news_id) " +
        			" WHERE query='" + keywords +
					" ;range=pubtime,"+st.getTime()/1000+","+et.getTime()/1000+";mode=boolean;maxmatches=3200;offset=0;limit=3200;'";
        try {
			stmt = conn.prepareStatement(sql_count);
		
	        ResultSet resultSet = stmt.executeQuery();
	        if (resultSet.next()) {
	        	todayCount = resultSet.getLong("num");
	        }
	        resultSet.close();
	        stmt.close();
        } catch (SQLException e) {
			Logger.error("统计专题今日数据量错误："+e.getMessage());
		}finally{
			DBUtil.closeConn(conn);
		}
		
		Logger.info(sql_count+"--统计专题今日数据量:"+todayCount);
		
		//存入缓存
		Cache.set(key, todayCount, Play.configuration.getProperty("todayCountCacheTime"));
		return todayCount;
	}
	
	
	
	/**
	 * 统计某一天的新增数据
	 * @return
	 */
	public static Long countDocs(String date,String keywords){
		//如果有缓存
		String key = String.format("/topic/countDocs?date=%s&keywords=%s", date,keywords);
		Logger.info(key+"---"+Cache.get(key));
		if(Cache.get(key)!=null)
			return (Long)Cache.get(key);
		
		Long count = 0l;
		
		//开始时间
		Date st = DateUtil.String2Date(date+" 00:00:00");
		//结束时间
		Date et = DateUtil.String2Date(date+" 23:59:59");
		// 指定记录
    	Connection conn = null;
        PreparedStatement stmt = null;
		conn = DBUtil.getWeixinDBConn();  
        String sql_count = "";
        sql_count = " SELECT count(1) AS num" +
					" FROM yqpt_weixin_news_info_sg_sphinxse AS dse" +
					" LEFT JOIN yqpt_weixin_news_info_sg AS d USING(news_id) " +
        			" WHERE query='" + keywords +
					" ;range=pubtime,"+st.getTime()/1000+","+et.getTime()/1000+";mode=boolean;maxmatches=3200;offset=0;limit=3200;'";
        try {
			stmt = conn.prepareStatement(sql_count);
		
	        ResultSet resultSet = stmt.executeQuery();
	        if (resultSet.next()) {
	        	count = resultSet.getLong("num");
	        }
	        resultSet.close();
	        stmt.close();
        } catch (SQLException e) {
			Logger.error("统计专题今日数据量错误："+e.getMessage());
		}finally{
			DBUtil.closeConn(conn);
		}
		Logger.info(sql_count+"--统计专题"+date+"数据量:"+count);
		
		Cache.set(key, count, Play.configuration.getProperty("countCacheTime"));
		return count;
	}
	
	/**
	 * 统计专题的数据量
	 * @return
	 */
	public static Long countTopicDocs(Long id){
		//先取缓存数据
		String key = String.format("/topic/countTopicDocs?id=%d", id);
		Logger.info(key+"---"+Cache.get(key));
		if(Cache.get(key)!=null)
			return (Long)Cache.get(key);
		
		Topic topic = Topic.findById(id);
		String keywords = topic.keywords;
		
		// 指定记录
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        Long count = 0l;

        try {
        	        	
            conn = DBUtil.getWeixinDBConn();  
            
            //获取数据列表
            String sql = "";
            sql = " SELECT count(1) AS num" +
					" FROM yqpt_weixin_news_info_sg_sphinxse AS dse" +
					" LEFT JOIN yqpt_weixin_news_info_sg AS d USING(news_id) " +
        			" WHERE query='" + keywords +
					" ;mode=boolean;maxmatches=3200;limit=3200;'"; 
          
            stmt = conn.prepareStatement(sql);
            resultSet = stmt.executeQuery();
            if (resultSet.next()) {
	        	count = resultSet.getLong("num");
	        }
            resultSet.close();
            stmt.close(); 
        } catch (Exception e) {
        	Logger.error("Result CheckUserInfoODBC:"+e.getMessage());
        } finally {
        	DBUtil.closeConn(conn);
        }
        //缓存count
        Cache.set(key, count, Play.configuration.getProperty("countCacheTime"));
        
        return count;
	}
	
	/**
	 * 获取信息列表
	 * @param keywords
	 * @param page
	 * @param pageSize
	 */
	public static JSONArray getTopicDocs(@Required Long id, int page ,int page_num, boolean getContent){
		Topic topic = Topic.findById(id);
		String keywords = topic.keywords;
		
		// 指定记录
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        JSONArray jarrArtical = new JSONArray();

        try {
        	//计算数据开始结束为止
            int st = (page-1)*page_num;
        	        	
            conn = DBUtil.getWeixinDBConn();  
            
            //获取数据列表
            String sql = "";
            sql = " SELECT *" +
					" FROM yqpt_weixin_news_info_sg_sphinxse AS dse" +
					" LEFT JOIN yqpt_weixin_news_info_sg AS d USING(news_id) " +
        			" WHERE query='" + keywords +
					" ;sort=attr_desc:pubtime;mode=boolean;maxmatches=3200;offset="+st+";limit="+page_num+";'"; 
          
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
            	jsonTmp.put("source_name", _source_name);
            	jsonTmp.put("pubtime", _pubtime);
            	jsonTmp.put("content_url", _content_url);
            	jsonTmp.put("news_type", _news_type);
            	jsonTmp.put("image_url", _image_url);
            	
            	//是否需要返回内容
            	if(getContent){
	            	//微信文章内容,截取100字  content，已有摘要字段
	            	String _content = resultSet.getString("summary");
	            	if(_content.length()>100){
	            		_content = _content.substring(0, 100)+"...";
	            	}
	            	jsonTmp.put("content", _content);
            	}
            	jarrArtical.add(jsonTmp);
            }
            resultSet.close();
            stmt.close(); 
        } catch (Exception e) {
        	Logger.error("Result CheckUserInfoODBC:"+e.getMessage());
        } finally {
        	DBUtil.closeConn(conn);
        }
        
        return jarrArtical;
	}
	
	
	/**
	 * 获取信息列表
	 * @param keywords
	 * @param page
	 * @param pageSize
	 */
	public static List<Object> getTopicTopPerson(Long id, int num){
		//先取缓存数据
		String key = String.format("/topic/getTopicTopPerson?id=%d&num=%d", id,num);
		Logger.info(key+"---"+Cache.get(key));
		if(Cache.get(key)!=null)
			return (List<Object>)Cache.get(key);
		
		Topic topic = Topic.findById(id);
		String keywords = topic.keywords;
		
		// 指定记录
    	Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        List<Object> jarrPerson = new ArrayList<Object>();

        try {
        	
            conn = DBUtil.getWeixinDBConn();  
            
            //获取数据列表
            String sql = "";
            sql = " SELECT * FROM ("+
					" SELECT a.open_id,a.source_name, COUNT(1) AS num FROM( "+
					" SELECT open_id, source_name"+
					" FROM yqpt_weixin_news_info_sg_sphinxse AS dse"+
					" LEFT JOIN yqpt_weixin_news_info_sg AS d USING(news_id)"+ 
					" WHERE query='"+keywords+";sort=attr_desc:pubtime;mode=boolean;maxmatches=3200;offset=0;limit=3200;'"+
					") a GROUP BY a.open_id"+
					") b ORDER BY b.num DESC LIMIT 0,"+num; 
          
            stmt = conn.prepareStatement(sql);
            resultSet = stmt.executeQuery();
            while (resultSet.next()) {
            	List<Object> obj = new ArrayList<Object>();
            	//微信用户昵称
            	String _source_name = resultSet.getString("source_name");
            	//文章发布时间
            	Long count  = resultSet.getLong("num");
            	obj.add(_source_name);
            	obj.add(count);
            	jarrPerson.add(obj);
            }
            resultSet.close();
            stmt.close(); 
        } catch (Exception e) {
        	Logger.error("Result CheckUserInfoODBC:"+e.getMessage());
        } finally {
        	DBUtil.closeConn(conn);
        }
        
      //缓存count
        Cache.set(key, jarrPerson, Play.configuration.getProperty("countCacheTime"));
        
        return jarrPerson;
	}

}
