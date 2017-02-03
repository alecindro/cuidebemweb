package br.com.cuidebem;

import org.apache.shiro.SecurityUtils;

public class UtilSecurity {

	
	public static String getUser(){
		if(SecurityUtils.getSubject().getPrincipal()== null){
			return null;
		}
		return SecurityUtils.getSubject().getPrincipal().toString();
	}
	
	public static void logout(){
		SecurityUtils.getSecurityManager().logout(SecurityUtils.getSubject());
	}
}
