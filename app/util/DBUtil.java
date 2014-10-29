package util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;




/**
 * 数据库工具类
 * @author zhou
 *
 */
public class DBUtil {
	
	public static ResourceBundle rb = ResourceBundle.getBundle("config");
	/**
	 * 获得微信的数据库连接
	 * @return
	 */
	public static Connection getWeixinDBConn(){
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(rb.getString("weixin_news_info"), rb.getString("weixin_news_info_user"), rb.getString("weixin_news_info_password"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return conn;
	}
	
	/**
	 * 关闭mysql数据库连接
	 * @param conn
	 */
	public static void closeConn(Connection conn){
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
