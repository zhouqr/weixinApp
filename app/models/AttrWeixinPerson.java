package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import play.db.jpa.Model;





/**
 * 关注的微信公众号
 * @author zhou
 *
 */
@Entity
public class AttrWeixinPerson extends Model{
	
	/**
	 * 获取公众号详情的id
	 */
	@Column(nullable=false)
	public String open_id;
	
	/**
	 * 公众号昵称
	 */
	@Column(nullable=false)
	public String sourcename;
	
	/**
	 * 公众号头像
	 */
	@Column(nullable=true)
	public String headim;
	
	/**
	 * 添加公众号的用户id
	 */
	@Column(nullable=false)
	public Long user_id;
	
	/**
	 * 公众号简介
	 */
	@Column(nullable = true)
	public String intro;
	
	/**
	 * 删除标志位
	 */
	@Column(nullable=false)
	public Integer delete_flag=0;

}
