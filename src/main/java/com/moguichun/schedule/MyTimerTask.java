package com.moguichun.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;

import javax.servlet.ServletContext;

public class MyTimerTask extends TimerTask{
	
	private ServletContext context = null;
	
	// 当前任务的运行状态
	private static boolean isRunning = false;
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
	
	public MyTimerTask(ServletContext context) {
		
		this.context = context;
		
	}

	@Override
	public void run() {
		context.log("============= 时间：" + formatter.format(new Date()) + " 定时任务运行开始===============");

		if (!isRunning) {

			isRunning = true;

			/*
			 * 业务代码
			 */
			
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			isRunning = false;
		} else {
			context.log("============= 时间：" + formatter.format(new Date()) + " 定时任务正在运行===============");
		}

		context.log("============= 时间：" + formatter.format(new Date()) + " 定时任务运行结束===============");
		
	}

}
