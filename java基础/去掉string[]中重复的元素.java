package com.changyou.erp.common;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.apache.log4j.Logger;


public class CopyOfStringUtil {
   
	/**
	 * 把String[]去重
	 * @param arr
	 * @return
	 */
	public static void unique(String [] arr){
        	//实例化一个set集合
        	Set<String> set = new HashSet<String>();
        	if(arr == null){
        		return;
        	}
       		//遍历数组并存入集合,如果元素已存在则不会重复存入
		for (int i = 0; i < arr.length; i++) {
		    set.add(arr[i]);
		    arr[i] = "";
		}
        
		int j = 0;
		for(String str : set){
			arr[j] = str;
			j++;
		}
    	} 
}
