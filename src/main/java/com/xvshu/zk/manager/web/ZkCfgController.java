package com.xvshu.zk.manager.web;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.xvshu.zk.manager.util.ZkCache;
import com.xvshu.zk.manager.util.ZkCfgFactory;
import com.xvshu.zk.manager.util.ZkManagerImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xvshu.zk.manager.util.ZkCfgManager;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/zkcfg")
public class ZkCfgController {
	
	private static final Logger log = LoggerFactory.getLogger(ZkCfgController.class);

	static ZkCfgManager zkCfgManager = ZkCfgFactory.createZkCfgManager();
	
	@RequestMapping(value="/queryZkCfg")
	public @ResponseBody Map<String, Object> queryZkCfg(@RequestParam(required=false) int page,@RequestParam(required=false) int rows,HttpSession session){
		
		try {
			log.info(new Date()+"");
			Map<String, Object> _map = new HashMap<String, Object>();
			_map.put("rows", zkCfgManager.query(page,rows));
			_map.put("total", zkCfgManager.count());
			return _map;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return null;
	}

	@RequestMapping(value="/queryZkManager")
	public @ResponseBody Map<String, Object> queryZkManager(HttpSession session){

		try {
			log.info(new Date()+"");
			Map<String, Object> _map = new HashMap<String, Object>();
			_map.put("userM", session.getAttribute("userM"));
			return _map;
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return null;
	}
	
	@RequestMapping(value="/addZkCfg",produces="text/html;charset=UTF-8")
	public @ResponseBody String addZkCfg(
			@RequestParam(required=false) String des,
			@RequestParam(required=false) String connectstr,
			@RequestParam(required=false) String sessiontimeout){
		
		try {
			String id = UUID.randomUUID().toString().replaceAll("-", "");
			if(ZkCfgFactory.createZkCfgManager().add(id,des, connectstr, sessiontimeout)){
				ZkCache.put(id, ZkManagerImpl.createZk().connect(connectstr,Integer.parseInt(sessiontimeout)));
			};
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return "添加失败";
		}
		return "添加成功";
	}
	
	@RequestMapping(value="/queryZkCfgById")
	public @ResponseBody Map<String, Object> queryZkCfg(
			@RequestParam(required=false) String id){
		
		try {
			return ZkCfgFactory.createZkCfgManager().findById(id);
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return null;
	}
	
	@RequestMapping(value="/updateZkCfg",produces="text/html;charset=UTF-8")
	public @ResponseBody String updateZkCfg(
			@RequestParam(required=true) String id,
			@RequestParam(required=false) String des,
			@RequestParam(required=false) String connectstr,
			@RequestParam(required=false) String sessiontimeout){
		
		try {
			if(ZkCfgFactory.createZkCfgManager().update(id,des, connectstr, sessiontimeout)){
				ZkCache.put(id, ZkManagerImpl.createZk().connect(connectstr,Integer.parseInt(sessiontimeout)));
			};
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return "保存失败";
		}
		return "保存成功";
	}
	
	@RequestMapping(value="/delZkCfg",produces="text/html;charset=UTF-8")
	public @ResponseBody String delZkCfg(
			@RequestParam(required=true) String id){
		
		try {
			if(ZkCfgFactory.createZkCfgManager().delete(id)){
				ZkCache.remove(id);
			};
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e.getMessage());
			return "删除失败";
		}
		return "删除成功";
	}
}
