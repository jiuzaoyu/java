package com.changyou.erp.esb.quartz;

import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.changyou.erp.attendance.common.DateUtil;

@Controller
public class 计算代码执行时间的方法 {
	protected final Logger logger = Logger.getLogger(this.getClass());
	
	//方法一：
	public void test1() throws Exception{
		logger.info("开始同步EHR数据：" + DateUtil.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		long start = System.currentTimeMillis();
		this.synchronizeEmpNewService.syncEmp();
		logger.info("结束同步EHR数据：" + DateUtil.DateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
		logger.info("同步EHR数据用时：" + (System.currentTimeMillis() - start)/1000/60D + "分钟");
	}

}
