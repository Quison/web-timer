# 在JavaWeb中使用Timer和TimerTask处理定时任务

标签（空格分隔）： JavaWeb

---

## 1. 概述
　　在开发的过程中经常需要跑定时任务，比如每天定时跑数据库归档等等，经了解，java中实现定时任务调度有3中方式：
> 1. Quartz框架，一个开源的任务调度框架；
> 2. ThreadPoolExecutor，Java自带任务调度类，优于Timer，它可另行安排在给定的延迟后运行命令，或者定期执行命令，可以进行多线程任务调度；
> 3. Timer，Java自带的一个工具，专门用来安排以后在后头线程中执行的任务，可安排任务执行一次，或者定期重复执行。

　　通过了解上面的实现方式，我们可以在定时任务很简单的情况下使用Timer来实现就可以了，在web项目中，我们使用ServletContextLister作为Timer的守护线程，这样一来当我们部署项目是，Servlet的监听器也会被启动，我们的计时任务也就启动了。

## 2. TimerTask - 计时任务
　　TimerTask是由Timer安排为一次执行或者重复执行的任务，我们的具体业务代码就放在TimerTask里面。TimerTask是一个抽象类，所以我们需要继承它，并且实现其抽象方法“run”方法，我们的业务代码也是写在run方法里面的，具体代码如下：
```
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
```

## 3. 实现ServletContextListener接口
　　我们的Timer需要放在一个ServletContextListener接口的实现类中，具体代码如下：
```
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
```
## 4. web.xml的配置
　　在web.xml中，我们需要将我们实现的监听器配置上去，具体如下：
```
<!DOCTYPE web-app PUBLIC
 "-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"
 "http://java.sun.com/dtd/web-app_2_3.dtd" >

<web-app>

  <display-name>Archetype Created Web Application</display-name>
  
  <listener>
  	<listener-class>com.moguichun.listener.MyListener</listener-class>
  </listener>
  
</web-app>
```

## 5. 总结
　　Timer还是有缺陷的，当然在任务很简单的情况下使用它也是非常方便的，因为它的实现也很简单。本项目的源码地址 https://github.com/Quison/web-timer



