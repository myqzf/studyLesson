package com.qzf.jsonlib;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtils;

import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONFunction;
import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;
import net.sf.json.util.PropertyFilter;

public class JsonLibTest {

	public static void main(String[] args) throws Exception{
		//test1();
		//test2();
		//test3();
		//test4();
		//test5();
		//test6();
		//test7();
		//test8();
		//test9();
		test10();
	}
	
	public static void test1() {
		boolean [] booleanArray = new boolean[] {true,false,true};
		JSONArray jsonArray1 = JSONArray.fromObject(booleanArray);
		System.out.println(jsonArray1);
		
		List<String> list = new ArrayList<>();
		list.add("first");
		list.add("second");
		JSONArray jsonArray2 = JSONArray.fromObject(list);
		System.out.println(jsonArray2);
		
		JSONArray jsonArray3 = JSONArray.fromObject("['1','2','3']");
		System.out.println(jsonArray3);
	}
	
	public static void test2() {
		Map<String,Object> map = new HashMap<>();
		map.put("name", "json");
		map.put("bool", Boolean.TRUE);
		map.put("int", new Integer(1));
		map.put("array", new String[] {"a","b"});
		map.put("func", "function (i) {return this.arr[i];} ");
		JSONObject jsonObject = JSONObject.fromObject(map);
		System.out.println(jsonObject);
		JSONFunction func2 = new JSONFunction(new String[]{"i"},"return this.options[i];");  
	}
	
	public static void test3() {
//		OstaCert osta = new OstaCert();
//		osta.setId(1L);
//		osta.setCertNum("100101");
//		osta.setBirthday(new Date());
//		osta.setOccuOrient("车工");
//		JSONObject jsonObject = JSONObject.fromObject(osta);
//		System.out.println(jsonObject);
	}
	
	@SuppressWarnings("unchecked")
	public static void test4() throws Exception{
		String json = "{name:\"json\",bool:true,int:1,double:2.2,func:function(a) {return a;},array:[1,2]}";
		JSONObject jsonObject = JSONObject.fromObject(json);
		Object bean = JSONObject.toBean(jsonObject);
		System.out.println(jsonObject.get("name"));
		System.out.println(PropertyUtils.getProperty(bean, "name"));
		Collection<JSONObject> collection = JSONArray.toCollection(jsonObject.getJSONArray("array"));
		System.out.println(collection);
	}
	
	public static void test5() {
		String json = "{\"birthday\":\"2018-12-14\",\"certNum\":\"100101\",\"id\":1,\"occuOrient\":\"车工\",\"ww\":3}";
		JSONObject jsonObject = JSONObject.fromObject(json);
//		OstaCert ostaCert = (OstaCert) JSONObject.toBean(jsonObject, OstaCert.class);
//		System.out.println(ostaCert.getCertNum());
	}
	
	public static void test7() {
		String json = "{\"data\":[{\"birthday\":\"2018-12-14\",\"certNum\":\"100101\",\"id\":1,\"occuOrient\":\"车工\"}]}";
		JsonConfig jsonConfig = new JsonConfig();
		//jsonConfig.setJavaIdentifierTransformer(javaIdentifierTransformer);
		jsonConfig.setJsonPropertyFilter(new PropertyFilter() {
			@Override
			public boolean apply(Object source, String name, Object value) {
				if(value != null && Number.class.isAssignableFrom(value.getClass())) {
					return true;
				}
				if("birthday".equals(name) || "certNum".equals(name)) {
					return true;
				}
				return false;
			}
		});
		JSON json1 = JSONSerializer.toJSON(json);
		JSONObject jsonObject = JSONObject.fromObject(json, jsonConfig);
		System.out.println(jsonObject);
	}
	
	public static void test8() {
		JSONObject json = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		//jsonArray.
		String[] s = new String[] {"1","2"};
		json.element("test", s);
		json.accumulate("test", 4);
		json.replace("test1", 6);
	//	json.element("test", 3);
		System.out.println(json);
    System.out.println(json.isArray());
    System.out.println(json.containsKey("text"));
    //XMLSerializer
	}
	
	public static void test9() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.element("name", "zhangsan").element("age", 12).element("sex", "男");
		for(Object str : jsonObject.keySet()) {
			String key = (String)str;
			System.out.println(key);
		}
		Set<Object> entrys = jsonObject.entrySet();
		for(Object entry : entrys) {
			System.out.println(entry);
		}
		Collection<Object> col = jsonObject.values();
		Iterator<Object> it = col.iterator();
		while(it.hasNext()) {
			System.out.println(it.next());
		}
	}
	
	
	public static void test10() {
		String str = "[\"zhangsan\",\"lisi\"]";
		List<String> list = new ArrayList<>();
		list.add("zhangsan");
		list.add("lisi");
		JSON json1 = JSONSerializer.toJSON(str);
		JSON json2 = JSONSerializer.toJSON(list);
		
		System.out.println(json1);
		System.out.println(json2.toString());
		
		
	}
}
