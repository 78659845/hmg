package com.oracle.gdms.web.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.alibaba.fastjson.JSONObject;
import com.oracle.gdms.entity.Buckup;
import com.oracle.gdms.entity.GoodsModel;
import com.oracle.gdms.entity.GoodsType;
import com.oracle.gdms.entity.ResponseEntity;
import com.oracle.gdms.service.GoodsService;
import com.oracle.gdms.util.Factory;

@Path("/hongyan")
public class HongYan {
	
	@Path("/sing")
	@GET
	public String sing() {
		System.out.println("红艳唱歌真好听");
		return "12323";
	}
	
	@Path("/sing/{name}")
	@GET
	public String sing(@PathParam("name") String name) {
		System.out.println("歌名：" + name);
		return "OK";
	}
	
	@Path("/sing/ci") //rest/hongyan/sing/ci?name=xxxx
	@GET
	public String singOne(@QueryParam("name") String name) {
		System.out.println("歌词：" + name);
		return "CI";
	}
	
	@Path("/push/one")
	@POST
	public String push(@FormParam("name") String name, @FormParam("sex") String sex) {
		System.out.println("商品名称：" + name);
		return "form";
	}
	
	@Path("/push/json")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	public String pushJson(String jsonparam) {
		System.out.println(jsonparam);
		JSONObject j = JSONObject.parseObject(jsonparam);
		String name = j.getString("name");
		String sex = j.getString("sex");
		String age = j.getString("age");
		System.out.println("name=" + name);
		System.out.println("sex=" + sex);
		System.out.println("age=" + age);
		return "form";
	}
	//这个用实体类封装的现在没有用到了
	@Path("/goods/update/type")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String updateGoodsType(String jsonparam) {
		System.out.println("str=" + jsonparam);
		JSONObject j = JSONObject.parseObject(jsonparam);
		String goodsid = j.getString("goodsid");
		String gtid = j.getString("gtid");
		System.out.println("要修改的商品id=" + goodsid + "   目标类别id=" + gtid);
		GoodsService service = (GoodsService) Factory.getInstance().getObject("goods.service.impl");
		Buckup obj = new Buckup();
		obj.setGtid(gtid);
		obj.setGoodsid(goodsid);
		System.out.println("gtid:"+ obj.getGtid() + "    goodsid:" + obj.getGoodsid());
		service.updateType(obj);
		return j.toJSONString();
	}
	
	//试试用map来传
	@Path("goods/update/typemap")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public String updateGoodTypeMap(String jsonparam) {
		JSONObject j = JSONObject.parseObject(jsonparam);
		String goodsid = j.getString("goodsid");
		String gtid = j.getString("gtid");
		GoodsService s = (GoodsService) Factory.getInstance().getObject("goods.service.impl");
		s.updateTypeMap(goodsid, gtid);
		return j.toJSONString();
	}
	
	
	@Path("/goods/push/bytype")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public List<GoodsModel> findByType(GoodsType type){//为什么写goodstype,传的是gtid,自动封装到这个类里
		List<GoodsModel> list = null;
		GoodsService service = (GoodsService) Factory.getInstance().getObject("goods.service.impl");
		list = service.findByType(type.getGtid());
		System.out.println("size=" + list.size());
		return list;
		
	}
	
	@Path("/goods/push/one")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public ResponseEntity pushGoodsOne(String jsonparam) {
		
		ResponseEntity r = new ResponseEntity();
		try {
			//{"goods":{"goodsid":"42","name":"矿泉水"，"price":"3.5"}}
			//{"goods" :{"goodsid":42, "name":"矿泉水", "price":"3.5"}}
			JSONObject j = JSONObject.parseObject(jsonparam);
			String gs = j.getString("goods");
			GoodsModel goods = JSONObject.parseObject(gs, GoodsModel.class);
			System.out.println("服务端收到了：");
			System.out.println("商品ID=" + goods.getGoodsid());
			System.out.println("商品名称=" + goods.getName());
			r.setCode(0);
			r.setMessage("推送成功");
			//http://localhost:8080/gdms-web/ws/hongyan/goods/push/one
		}catch (Exception e){
			e.printStackTrace();
			r.setCode(1184);
			r.setMessage("推送商品失败,商品的数据不合法：" + jsonparam);
		}
		
		return r;
	}
	
	
}
