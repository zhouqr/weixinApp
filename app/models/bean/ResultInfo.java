package models.bean;

import java.io.Serializable;

/**
 * 返回的操作结果类
 * @author zhou
 *
 */

public class ResultInfo implements Serializable{
	
	/**
	 * 200 ok
	 * 401 请求错误
	 * 
	 */
	public int code;
	
	/**
	 * 错误提示消息: ｛错误内容｝
	 */
	public String msg;
	
	/**
	 * 返回请求的当前页码
	 */
	public Integer page;
	
	/**
	 * 数据结果 : object
	 */
	public Object info;
	
	
	/**
	 * 获取一个通用的操作成功的对象
	 * @param info 查询操作所返回的数据
	 * @param access_token http请求标识是否过期的标识
	 * @return
	 */
	public static ResultInfo success(Object info) {
		ResultInfo result = new ResultInfo();
		result.msg = "操作成功";
		result.code = 200;
		result.info = info;
		return result;
	}
	
	/**
	 * 操作成功，无返回对象
	 * @param info
	 * @return
	 */
	public static ResultInfo success() {
		return success(null);
	}
	
	
	/**
	 * 获取一个通用的操作失败的对象
	 * @param access_token http请求标识是否过期的标识
	 * @return
	 */
	public static ResultInfo error() {
		return error("操作失败");
	}
	
	/**
	 * 获取一个通用的操作失败的对象
	 * @param msg 错误提示消息
	 * @param access_token http请求标识是否过期的标识
	 * @return
	 */
	public static ResultInfo error(String msg) {
		ResultInfo result = new ResultInfo();
		result.msg = msg;
		result.code = 401;
		return result;
	}
	
	
	
}

