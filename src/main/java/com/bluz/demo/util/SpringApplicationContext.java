package com.bluz.demo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SpringApplicationContext {

	/**
	 * spring容器
	 */
	private ApplicationContext context;
	
	/**
	 * 构造函数
	 * @param context spring容器
	 */
	@Autowired
	public SpringApplicationContext(ApplicationContext context) {
		this.context = context;
	}
	
	/**
	 * 获取bean
	 * @param clazz bean类型
	 * @return bean实例
	 */
	public <T> T getBean(Class<? extends T> clazz) {
		return context.getBean(clazz);
	}

	/**
	 * 根据bean name 获取bean实例
	 * @param beanName
	 * @return
	 */
	public Object getBean(String beanName) {
		return context.getBean(beanName);
	}
	
}