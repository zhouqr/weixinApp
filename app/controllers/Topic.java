package controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import play.mvc.Controller;

import util.DBUtil;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 专题相关
 * @author zhou
 *
 */
public class Topic extends Controller{
	
	/**
	 * 获取信息列表
	 * @param keywords
	 * @param page
	 * @param pageSize
	 */
	public static void getTopicDocs(String keywords, int page ,int page_num){
		JSONObject jsonRet = new JSONObject();
		// 总数
		int total_num = 0;
		// 指定记录
    	Connection conn = null;
        PreparedStatement stmt = null;
        JSONArray jarrArtical = new JSONArray();

        try {
            int st = (page-1)*page_num;
			int et = page_num;			
        	        	
            conn = DBUtil.getWeixinDBConn();  
            String sql_count = "";
            sql_count = " SELECT count(1) AS num" +
						" FROM yqpt_weixin_news_info_sphinxse AS dse" +
						" LEFT JOIN yqpt_weixin_news_info AS d USING(news_id) " +
            			" WHERE query='" + keywords +
						" ;mode=boolean;maxmatches=3200;offset=0;limit=3200;'";
            stmt = conn.prepareStatement(sql_count);
            ResultSet resultSet = stmt.executeQuery();
            if (resultSet.next()) {
            	total_num = resultSet.getInt("num");
            }
            resultSet.close();
            
            //获取数据列表
            String sql = "";
            sql = " SELECT *" +
					" FROM yqpt_weixin_news_info_sphinxse AS dse" +
					" LEFT JOIN yqpt_weixin_news_info AS d USING(news_id) " +
        			" WHERE query='" + keywords +
					" ;sort=attr_desc:pubtime;mode=boolean;maxmatches=3200;offset=0;limit=3200;'" +
        			" LIMIT " + st + ", " + et;
            sql = " SELECT e.*, f.* FROM(" +
            		sql + ") e" +
            		" LEFT OUTER JOIN yqpt_weixin_souce_info f" +
            		" ON e.source_id = f.source_id";
            //System.out.println(sql);
            stmt = conn.prepareStatement(sql);
            resultSet = stmt.executeQuery();
            while (resultSet.next()) {
            	JSONObject jsonTmp = new JSONObject();
            	String _weixin_id = resultSet.getString("weixin_id");
            	String _news_id = resultSet.getString("news_id");
            	String _weixin_news_id = resultSet.getString("weixin_news_id");
            	String _title = resultSet.getString("title");
            	String _source_id = resultSet.getString("source_id");
            	String _source_name = resultSet.getString("source_name");
            	String _pubtimetmp1 = resultSet.getString("pubtime");
//            	String _pubtime = "";
//            	if(_pubtimetmp1.trim() != ""){
//	            	Date tmp_date = sdf.parse(_pubtimetmp1);
//					_pubtime = sdf.format(tmp_date); 
//				}
            	//String _content = resultSet.getString("content");
            	String _html_content = resultSet.getString("html_content");
            	String _source_url = resultSet.getString("source_url");
            	String _content_url = resultSet.getString("content_url");
            	String _cover_url = resultSet.getString("cover_url");
            	String _author = resultSet.getString("author");
            	String _is_frist_news = resultSet.getString("is_frist_news");
            	String _news_type = resultSet.getString("news_type");
            	String _url_update_time1 = resultSet.getString("url_update_time");
            	String _url_update_time = "";
//            	if(_url_update_time1.trim() != ""){
//	            	Date tmp_date = sdf.parse(_url_update_time1);
//	            	_url_update_time = sdf.format(tmp_date); 
//				}
            	String _last_gather_time1 = resultSet.getString("last_gather_time");
            	String _last_gather_time = "";
//            	if(_last_gather_time1.trim() != ""){
//	            	Date tmp_date = sdf.parse(_last_gather_time1);
//	            	_last_gather_time = sdf.format(tmp_date); 
//				}
            	String _storage_time1 = resultSet.getString("storage_time");
            	String _storage_time = "";
//            	if(_storage_time1.trim() != ""){
//	            	Date tmp_date = sdf.parse(_storage_time1);
//	            	_storage_time = sdf.format(tmp_date); 
//				}

            	jsonTmp.put("weixin_id", _weixin_id);
            	jsonTmp.put("news_id", _news_id);
            	jsonTmp.put("weixin_news_id", _weixin_news_id);
            	jsonTmp.put("title", _title);
            	jsonTmp.put("source_id", _source_id);
            	jsonTmp.put("source_name", _source_name);
            	jsonTmp.put("pubtime", _pubtimetmp1);
            	//jsonTmp.put("content", _content);
            	jsonTmp.put("html_content", "");
            	jsonTmp.put("source_url", _source_url);
            	jsonTmp.put("content_url", _content_url);
            	jsonTmp.put("cover_url", _cover_url);
            	jsonTmp.put("author", _author);
            	jsonTmp.put("is_frist_news", _is_frist_news);
            	jsonTmp.put("news_type", _news_type);
            	jsonTmp.put("url_update_time", _url_update_time);
            	jsonTmp.put("last_gather_time", _last_gather_time);
            	jsonTmp.put("storage_time", _storage_time);
            	
            	jarrArtical.add(jsonTmp);
            }
            resultSet.close();
            stmt.close();
        } catch (Exception e) {
//        	logger.error("Result CheckUserInfoODBC:"+e.getMessage());
        } finally {
        	DBUtil.closeConn(conn);
        }
        
        jsonRet.put("total_num", total_num);
        jsonRet.put("jarrArtical", jarrArtical);
        System.out.println(jsonRet.toString());
        
        renderJSON(jsonRet);

	}
}
