package com.qzf.jackson;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qzf.jackson.model.Student;

public class JacksonTest1 {

	public static void main(String[] args) throws Exception{
		//test1();
		//test2();
		//test3();
		//test4();
		test5();
	}
	
	public static void test1() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		
		Student student1 = mapper.readValue("{\"name\":\"Bob\",\"age\":12,\"birthday\":\"2018-12-18\"}", Student.class);//Date 'yyyy-MM-dd'T'HH:mm:ss.SSSZ'
		System.out.println(student1);
		mapper.writeValue(new File("F:\\test\\student.json"), student1);
		Student student2 = mapper.readValue(new File("F:\\test\\student.json"), Student.class);
		
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);
		String json1 = mapper.writeValueAsString(student2);
		System.out.println(json1);
		
		mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);
		Map<String, Object> map = mapper.readValue(json1, Map.class);
		String json2 = "[{\"name\":\"Bob\",\"age\":12,\"birthday\":\"2018-12-18\"}]";
		List<Object> list = mapper.readValue(json2, List.class);
		
		List<Object> list2 = mapper.readValue(json2, new TypeReference<List<Object>>(){});
	}
	
	public static void test2() throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		ObjectNode root = (ObjectNode) mapper.readTree("{\"name\":\"Bob\",\"age\":12,\"birthday\":\"2018-12-18\"}");
		String node = root.get("name").asText();
		int age = root.get("age").asInt();
		System.out.println(node);
		//ArrayNode arrayNode 
		
		root.with("other").put("type", "student");
		String json = mapper.writeValueAsString(root);
		System.out.println(json);
	}
	
	public static void test3() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonFactory factory = mapper.getFactory();
		File file = new File("F:\\test\\student.json");
		JsonGenerator generator = factory.createGenerator(file, JsonEncoding.UTF8);
		generator.writeStartObject();
		generator.writeStringField("message", "hello world!");
		generator.writeEndObject();
		generator.close();
		
		JsonParser parser = factory.createParser(file);
		JsonToken token = parser.nextToken();
		token = parser.nextToken();
		if((token != JsonToken.FIELD_NAME) || !"message".equals(parser.getCurrentName())) {
			System.out.println("11111");
		}
		token = parser.nextToken();
		if(token != JsonToken.VALUE_STRING) {
			System.out.println("22222");
		}
		String msg = parser.getText();
		System.out.printf("My message to you is: %s!\n", msg);
		parser.close();
			
	}
	
	public static void test4() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		Student student = mapper.readValue(new File("F:\\test\\student.json"), Student.class);
		System.out.println(student);
	}
	
	public static void test5() throws IOException {
		Student student = new Student();
		student.setAge(10);
		student.setBirthday(new Date());
		student.setName("zhangsan");
		ObjectMapper mapper = new ObjectMapper();
		//mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		String str = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(student);
		System.out.println(str);
	}
	
}
