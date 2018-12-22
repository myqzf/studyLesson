package com.qzf.jackson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.mockito.internal.util.reflection.Fields;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.cfg.MapperConfig;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.qzf.jackson.model.Animal;
import com.qzf.jackson.model.Bird;
import com.qzf.jackson.model.BirdMixIn;
import com.qzf.jackson.model.Dataset;
import com.qzf.jackson.model.DatasetFilter;
import com.qzf.jackson.model.Elephant;
import com.qzf.jackson.model.Lion;
import com.qzf.jackson.model.Zoo;

public class JacksonTest2 {

	public static void main(String[] args) throws Exception{
		//javaBeanToJson();
		//generatorJson();
		//streamParserJson();
		//streamGeneratorJson();
		//treeModelParser();
		//dataBindingToBean();
		//dataBindingFilter();
		//serializeExample1();
		//deSerializeExample1();
		//serializeZoo();
		//deserializaZoo();
		//serializeAnimals();
		serializeExample();
	}
	
	public static void javaBeanToJson() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		
		Album album = new Album("Kind Of Blue");
		album.setLinks(new String[] {"link1", "link2"});
		
		List<String> songs = new ArrayList<>();
		songs.add("So What");
		songs.add("Flamenco Sketches");
		songs.add("Friddie Freeloader");
		album.setSongs(songs);
		
		mapper.configure(SerializationFeature.INDENT_OUTPUT, true);	//格式化
		
		Artist artist = new Artist();
		artist.name = "Miles Davies";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		artist.birthDate = format.parse("2018-12-19");
		album.setArtist(artist);
		
		SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
		mapper.setDateFormat(outputFormat);	//设置日期格式
		
		mapper.configure(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS, true);//按自然顺序对key排序
		
		album.addMusician("Miles Davis", "Trumpet, Band leader");
		album.addMusician("Julian Adderley", "Alto Saxophone");
		album.addMusician("Paul Chambers", "double bass");
		
		mapper.setPropertyNamingStrategy(new PropertyNamingStrategy() {	//设置字段的命名策略
			private static final long serialVersionUID = 1L;
			@Override
			public String nameForField(MapperConfig<?> config, AnnotatedField field, String defaultName) {
				if(field.getFullName().equals("com.qzf.jackson.Artist#name"))
					return "Artist-Name";
				return super.nameForField(config, field, defaultName);
			}
			@Override
			public String nameForGetterMethod(MapperConfig<?> config, AnnotatedMethod method, String defaultName) {
				if(method.getAnnotated().getDeclaringClass().equals(Album.class) && defaultName.equals("title"))
					return "Album-title";
				return  super.nameForGetterMethod(config, method, defaultName);
			}
			
		});
		
		mapper.setSerializationInclusion(Include.NON_EMPTY);	//忽略空的属性
		mapper.writeValue(System.out, album);
		//String json = mapper.writeValueAsString(album);
		//System.out.println(json);
	}
	
	public static void generatorJson() throws IOException {
		JsonNodeFactory factory = new JsonNodeFactory(false);	//创建一个JsonNodeFactory来创建节点
		JsonFactory jsonFactory = new JsonFactory();	//从JsonFactory创建JsonGenerator并指定输出方法。(如:打印到控制台)
		JsonGenerator generator = jsonFactory.createGenerator(System.out);	//创建一个ObjectMapper，它将使用jsonGenerator和根节点来创建JSON。
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, true);
		ObjectNode album = factory.objectNode();
		album.put("Ablum-Title", "kind Of Blue");
		
		ArrayNode links = factory.arrayNode();
		links.add("links1").add("links2");
		album.set("links", links);
		
		ObjectNode artist = factory.objectNode();
		artist.put("Artist-Name", "Miles Davis");
		artist.put("birthDate", "2018-12-20");
		album.set("artist", artist);
		
		ObjectNode musicians = factory.objectNode();
		musicians.put("Julian Adderley", "Alto Saxophone");
		musicians.put("Miles Davis", "Trumpet, Band leader");
		album.set("musicians", musicians);
		
		mapper.writeTree(generator, album);
	}
	
	public static void streamParserJson() throws JsonParseException, IOException {
		String url = "http://119.36.213.27:9005/cpnsp/admin/sp/login/17030000/123456";
	
		JsonFactory jsonFactory = new JsonFactory();
		JsonParser parser = jsonFactory.createParser(new URL(url));//从JsonFactory中获取JsonParser实例
		while(!parser.isClosed()) {
			JsonToken token = parser.nextToken();  //获取token
			if(token == null)	//如果是最后一个token，跳出循环
				break;
			if(JsonToken.FIELD_NAME.equals(token) && "data".equals(parser.getCurrentName())) {
				
				//token = parser.nextToken();
				//if(!JsonToken.START_ARRAY.equals(token)) {
				//	break;
				//}
				
				token = parser.nextToken();
				if(!JsonToken.START_OBJECT.equals(token)) {
					break;
				}
				
				while(true) {
					token = parser.nextToken();
					if(token == null)
						break;
					if(JsonToken.FIELD_NAME.equals(token)) {
						//token = parser.nextToken();
						System.out.println(parser.getText());
					}
				}
			}
		}
	
	}
	
	public static void streamGeneratorJson() throws IOException {
		JsonFactory factory = new JsonFactory();
		JsonGenerator generator = factory.createGenerator(new FileWriter(new File("F://test//albums.json")));
		
		generator.writeStartObject();
		generator.writeFieldName("title");
		generator.writeString("Free Music Archive - Albums");
		generator.writeFieldName("dataset");
		
		generator.writeStartArray();
		generator.writeStartObject();
		generator.writeStringField("album_-title", "A.B.A.Y.A.M");
		generator.writeEndObject();
		generator.writeEndArray();
		generator.writeEndObject();
		
		generator.close();
	}
	
	public static void treeModelParser() throws MalformedURLException, IOException  {
		String url = "http://119.36.213.27:9005/cpnsp/admin/sp/login/17030000/123456";
		String generJson = IOUtils.toString(new URL(url), "utf-8");	//获取json字符串
		ObjectMapper mapper = new ObjectMapper();
		JsonNode node = mapper.readTree(generJson);	//读取json字符串并创建树
		System.out.println(node.getNodeType());
		System.out.println(node.isContainerNode());
		Iterator<String> fieldNames = node.fieldNames();
		while(fieldNames.hasNext()) {
			String fieldName = fieldNames.next();
			System.out.println(fieldName);
		}
		
		System.out.println(node.has("title"));
		System.out.println(node.get("status").asText());
		
		JsonNode data = node.get("data");
		System.out.println(data.getNodeType());
		Iterator<JsonNode> dataElements = data.iterator();
		while(dataElements.hasNext()) {
			//System.out.println(dataElements.next().path("id").asText());//path方法类似于get（）方法，但如果该节点不存在，则它不返回null而是返回MissingNode类型的对象
			JsonNode dataElement = dataElements.next();
			System.out.println(dataElement);
			Iterator<String> dataElementFields = dataElement.fieldNames();
			while(dataElementFields.hasNext()) {
				String dataElementField = dataElementFields.next();
				System.out.println(dataElementField);
			}
		}
	}
	
	//{"title":"Free Music Archive - Albums","message":"","errors":["invalid or disabled api_key"],"http_status":403,"dataset":[{"album_id":"1001","album_title":"kind Of Blue","album_url":"link1"}]}
	public static void dataBindingToBean() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);//禁用映射器遇到未知属性时导致断开的功能，将忽略bean中不存在的属性。
		Album albums = mapper.readValue(new File("F://test//albums.json"), Album.class);
		Dataset[] datasets = albums.getDataset();
		for (Dataset dataset : datasets) {
			System.out.println(dataset.getAlbum_title());
		}
	}
	
	//注解
	public static void dataBindingFilter () throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		AlbumsFilter albums = mapper.readValue(new File("F://test//albums.json"), AlbumsFilter.class);
		System.out.println(albums.getTotal_pages());
		System.out.println(albums.getTitle());
		for (DatasetFilter dataset : albums.getDatasetFilter()) {
			System.out.println(dataset.getAlbum_id());
			System.out.println(dataset.getAlbum_title());
			System.out.println(dataset.get("album_url"));
			System.out.println(dataset.get("tags"));
		}
	}
	
	//多态性
	public static void serializeExample1() throws JsonGenerationException, JsonMappingException, IOException {
		Zoo zoo = new Zoo("Samba Wild Park", "Paz");
		Lion lion = new Lion("Simba");
		Elephant elephant = new Elephant("Manny");
		List<Animal> animals = new ArrayList<>();
		animals.add(lion);
		animals.add(elephant);
		zoo.setAnimals(animals);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.writerWithDefaultPrettyPrinter().writeValue(new FileWriter("F://test//zoo.json"), zoo);
	}
	
	public static void deSerializeExample1() throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		Zoo zoo = mapper.readValue(FileUtils.readFileToByteArray(new File("F://test//zoo.json")), Zoo.class);
		System.out.println(zoo.name);
	}
	
	public static void serializeZoo() throws JsonGenerationException, JsonMappingException, IOException {
		Zoo zoo = new Zoo("London Zoo", "London");
		Lion lion = new Lion("Simba");
		Elephant elephant = new Elephant("Manny");
		zoo.addAnimal(elephant).add(lion);
		ObjectMapper mapper = new ObjectMapper();
		mapper.writerWithDefaultPrettyPrinter().writeValue(System.out, zoo);
	}
	
	public static void deserializaZoo() throws JsonParseException, JsonMappingException, IOException {
		String json = "{\"@class\":\".Zoo\",\"name\":\"London Zoo\",\"city\":\"London\",\"animals\":[{\"@class\":\"com.qzf.jackson.model.Elephant\",\"name\":\"Manny\",\"sound\":\"trumpet\",\"type\":\"herbivorous\",\"endangered\":false},{\"@class\":\"com.qzf.jackson.model.Lion\",\"name\":\"Simba\",\"sound\":\"Roar\",\"type\":\"carnviorous\",\"endangered\":true}]}";
		ObjectMapper mapper = new ObjectMapper();
		Zoo zoo = mapper.readValue(json, Zoo.class);
		System.out.println(zoo);
		
	}
	
	public static void serializeAnimals() throws JsonGenerationException, JsonMappingException, IOException {
		List<Animal> animals = new ArrayList<>();
		Lion lion = new Lion("Samba");
		Elephant elephant = new Elephant("Manny");
		animals.add(lion);
		animals.add(elephant);
		ObjectMapper mapper = new ObjectMapper();
		//mapper.writeValue(System.out, animals);
		mapper.writerFor(new TypeReference<List<Animal>>(){}).writeValue(System.out, animals);
	}
	
	public static void serializeExample() throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		mapper.addMixIn(Bird.class, BirdMixIn.class);
		Bird bird = new Bird("scarlet Ibis");
		bird.setSound("eee");
		bird.setHabitat("water");
		
		mapper.writerWithDefaultPrettyPrinter().writeValue(new File("F://test//bird.json"), bird);
	}
}
