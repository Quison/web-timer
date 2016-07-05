package com.moguichun.listener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.moguichun.schedule.MyTimerTask;

public class MyListener implements ServletContextListener {
	
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");

	private Timer timer = null;

	/**
	 * 
	 * <p>Title: contextInitialized</p>
	 * <p>Description: 初始化监听器</p>
	 * @param arg0
	 * @see javax.servlet.ServletContextListener#contextInitialized(javax.servlet.ServletContextEvent)
	 */
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		arg0.getServletContext().log("============时间：" + formatter.format(new Date()) +"初始化定时器==============");
		// 将Servlet的监听器作为其守护线程
		timer = new Timer(true);
		
		// 启动后延迟一分钟执行
		long delay = 60 * 1000;
		
		// 每两分钟执行一次
		int period = 2 * 60 * 1000;
		
		// 添加调度任务
		timer.schedule(new MyTimerTask(arg0.getServletContext()), delay, period);
		
		arg0.getServletContext().log("============时间：" + formatter.format(new Date()) +"定时器初始化完成==============");
		
	}
	
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		arg0.getServletContext().log("============时间：" + formatter.format(new Date()) +"初始化定时器==============");
		timer.cancel();
	}

}
