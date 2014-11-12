package models;

import javax.persistence.Column;
import javax.persistence.Entity;

import play.db.jpa.Model;

/**
 * 用户
 * @author zhou
 *
 */
@Entity
public class User extends Model{
	
	/**
	 * 用户名
	 */
	@Column(nullable=false)
	public String username;
	
	/**
	 * 密码
	 */
	@Column(nullable=false)
	public String password;
}
