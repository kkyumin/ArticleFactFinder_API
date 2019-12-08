package org.factfinder.controller;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.apache.http.HttpHost;
import org.apache.http.util.EntityUtils;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.action.admin.indices.get.GetIndexResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.factfinder.service.PoliticService;
import org.factfinder.vo.DialogueVO;
import org.factfinder.vo.MinuteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.log.Log;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
@RequestMapping("/politic")
@RestController
@Log4j2
@AllArgsConstructor
@CrossOrigin(origins = "*")
public class PoliticController {
	

    public RestHighLevelClient createConnection() {
        return new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost("127.0.0.1",9200,"http")
                    )
         );
    }	
    
    private static final String startEm = "<em>"; 
    private static final String endEm = "</em>";    
	@GetMapping(value = "/test")
//    public Map<String,Object> test() {
	public String test() {
		  	String INDEX_NAME = "*plenary_session";
	        //문서 타입
	        
		  	String TYPE_NAME ="_doc";
		  	String FIELD_NAME = "dialogue.discussion";
		  	
	        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	        
	        RestHighLevelClient client = null;
	        Map<String, Object> sourceAsMap = null;

	        try {
	        client = createConnection();
	        
	        searchSourceBuilder.query(QueryBuilders.termQuery(FIELD_NAME, "문희상"));
	        searchSourceBuilder.from(0);
	        searchSourceBuilder.size(5);
	    

	        SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
	        searchRequest.types(TYPE_NAME);
	        searchRequest.source(searchSourceBuilder);
	        SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
	        SearchHits searchHits = searchResponse.getHits();
	        for(SearchHit hit: searchHits) {
	        	sourceAsMap = hit.getSourceAsMap();
	        }	        
	        String json = new ObjectMapper().writeValueAsString(sourceAsMap);
	        return json;
	        }catch(Exception e){
	        	
	        }finally {	 	        
	        	try{
	        		client.close();
	        	}catch(IOException ie) {
	        		
	        	}
      
	        }
	        return null;
	}
    
    
	@GetMapping(value = "/round")
//    public String[] allMinute() {
	public String minuteList() {
	        RestHighLevelClient client = null;
	        try {
	        client = createConnection();
	        GetIndexRequest request = new GetIndexRequest().indices("*plenary_session");
	        GetIndexResponse response = client.indices().get(request, RequestOptions.DEFAULT);
	        String[] indices = response.getIndices();
			String json = new ObjectMapper().writeValueAsString(indices);
			return json;
	        }catch(Exception e){
	        	
	        }finally {	 	        
	        	try{
	        		client.close();
	        	}catch(IOException ie) {
	        		
	        	} 
	        }
	        return null;
	}

	
	@RequestMapping(value = "/meetingInfo", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public String getMeetingInfo(@RequestParam("ordinal") int ordinal){
		String INDEX_NAME = ordinal+"th_*round_plenary_session";
		
        RestHighLevelClient client = null;
        try {
        	client = createConnection();
           
        	SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        		
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            searchSourceBuilder.fetchSource(false);
            searchRequest.source(searchSourceBuilder);
	        SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
	        
	        SearchHits searchHits = searchResponse.getHits();
	        
	        ArrayList<String> stringList = new ArrayList<String>();
	        String round = null;
	        String roundText = null;
	        String prevRoundText = null;
	        ArrayList<Map<String,Object>> dialogueList = new ArrayList<Map<String,Object>>();
			Map<String, Object> resultMap= new HashMap<>();
			
			ArrayList<Map<String,Object>> resultArray = new ArrayList<Map<String,Object>>();
			ArrayList<Map<String,Object>> idArray = new ArrayList<Map<String,Object>>();
	        int id;
	        String idText;
	        String prevRound= null;
	        String temp =null;
	        for(SearchHit hit: searchHits) {
	        	
	        	int flag = 0;
	        	
	        	temp =hit.getIndex().split("_")[1].split("round")[0];
	        	if(!temp.equals(round)){
	        		prevRound = round;
	        		round = temp;
	        		prevRoundText = prevRound+"회";
					flag = 1;
	        	}
	        	
				if(flag ==1) {
					ArrayList<Object> tempIdArray= (ArrayList<Object>) idArray.clone();
					idArray.clear();
					Map<String,Object> roundMap = new HashMap<String,Object>();
					roundMap.put("round",prevRound);
					roundMap.put("text",prevRoundText);
					roundMap.put("dialogue", tempIdArray);
					resultArray.add(roundMap);
				}	
	        	Map<String,Object> idMap = new HashMap<String,Object>();
				
	        	id = Integer.parseInt(hit.getId());
				idText= hit.getId()+"차";	        		        	
				idMap.put("time", id);
				idMap.put("text", idText);
				idArray.add(idMap);
			}

	    	ArrayList<Object> tempIdArray= (ArrayList<Object>) idArray.clone();
			idArray.clear();
			Map<String,Object> roundMap = new HashMap<String,Object>();
			roundMap.put("round",Integer.parseInt(round));
			roundMap.put("text",round+"회");
			roundMap.put("dialogue", tempIdArray);
			resultArray.add(roundMap);
			
			resultArray.remove(0);
//            Response response = client.getLowLevelClient().performRequest(request);
//	        String temp =  EntityUtils.toString(response.getEntity());
	        String json = new ObjectMapper().writeValueAsString(resultArray);
	        return json;
        }catch(Exception e){	
        }finally {	 	        
        	try{
        		client.close();
        	}catch(IOException ie) {
        	} 
        }
        return null;
	}

	
	@GetMapping(value = "/{rid}/round")
	public String documentCountInMinute(@PathVariable("rid") int rid ){
	  	String INDEX_NAME = "*_"+rid+"round_plenary_session";
	
        RestHighLevelClient client = null;
        try {
        	client = createConnection();
        	
//        	Request request = new Request("GET", INDEX_NAME);
           
        	SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
        	
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            String[] includeFields = new String[] {"_id"};

            //equals to _source
            searchSourceBuilder.fetchSource(includeFields, null);
	        searchRequest.source(searchSourceBuilder);
	        SearchResponse searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
	        
	        SearchHits searchHits = searchResponse.getHits();
	        Map<String, Object> idAsMap= new HashMap<>();
	        ArrayList<String> stringList = new ArrayList<String>();

	        for(SearchHit hit: searchHits) {
	        	stringList.add(hit.getId());
	        }	
	        idAsMap.put("time",stringList);
//            Response response = client.getLowLevelClient().performRequest(request);
//	        String temp =  EntityUtils.toString(response.getEntity());
	        String json = new ObjectMapper().writeValueAsString(idAsMap);
	        return json;
        }catch(Exception e){	
        }finally {	 	        
        	try{
        		client.close();
        	}catch(IOException ie) {
        	} 
        }
        return null;
	}
	

	
//	@GetMapping(value = "/{oid}/ordinal/{rid}/round/{tid}/time" , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//	@RequestMapping(value = "/dialogueInTime", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//	public String dialogueInTime(@RequestParam("round") int rid, @RequestParam("time") int tid,@RequestParam("meeting_type") String meeting_type){
//		String INDEX_NAME = "*"+rid+"round_"+meeting_type;
//        //문서 타입
//	  	String TYPE_NAME ="_doc";
//	  	
//
//	  	
//        GetRequest request = new GetRequest(INDEX_NAME,TYPE_NAME,Integer.toString(tid));
//        GetResponse response = null;
//       try(RestHighLevelClient client = createConnection();){
//            response = client.get(request, RequestOptions.DEFAULT);
//        }catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//            return null;
//        }
//       Map<String, Object> sourceAsMap = null;
//       if(response.isExists()) {
//           sourceAsMap = response.getSourceAsMap();
//       }
//       String json = null;
//       try {
//       json = new ObjectMapper().writeValueAsString(sourceAsMap);
//       }catch(Exception e) {
//       }
//       return json;
//	}

	@RequestMapping(value = "/dialogueInTime", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public String dialogueInTime(@RequestParam("round") int rid, @RequestParam("time") int tid,@RequestParam("meeting_type") String meeting_type){
		String INDEX_NAME = "*_"+rid+"round_"+meeting_type;
        //문서 타입
	  	String TYPE_NAME ="_doc";
	  	
	       SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
	       
	       SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	       
	       
	       searchSourceBuilder.query(QueryBuilders.idsQuery("_doc").addIds(Integer.toString(tid)));
	         
	       searchRequest.source(searchSourceBuilder);
	       System.out.println(searchRequest.source().toString());

	       SearchResponse searchResponse = null;
       try(RestHighLevelClient client = createConnection();){
           searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
       String json =null;
//
       
  
	   SearchHits searchHits = searchResponse.getHits();

	   // 아직 안하는 중

	    Map<String,Object> resultMap = new HashMap<String,Object>();
	        
       for(SearchHit hit: searchHits) {
    	   resultMap = hit.getSourceAsMap();
       }	
       
       ;
	   try{
		   json = new ObjectMapper().writeValueAsString(resultMap);
       }catch(Exception e) {
       }
      
   	   return json;
	}

	
	@RequestMapping(value = "/congressMember", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//	@GetMapping(value = "/congressMember/{oid}" , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public String documentCountInMinute(@RequestParam("ordinal") String oid){
		String INDEX_NAME = "congress_member_list";
        //문서 타입
	  	String TYPE_NAME ="_doc";
       GetRequest request = new GetRequest(INDEX_NAME,TYPE_NAME,oid);
       GetResponse response = null;
     

       if(oid == null)
    	   return null;
       
       try(RestHighLevelClient client = createConnection();){
            response = client.get(request, RequestOptions.DEFAULT);
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
       }
       
       Map<String, Object> sourceAsMap = null;
//
       String json = null;
       if(response.isExists()) {
    	   sourceAsMap = response.getSourceAsMap();
       }
       Object memberListBeforeSort= sourceAsMap.get("member_info_list");
       ArrayList<String> memberList = (ArrayList<String>) memberListBeforeSort;
       Collections.sort(memberList);
       
       ArrayList<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();
       for(int i=0; i<memberList.size();i++) {
    	   String[] tmp = memberList.get(i).split(" ");
    	   String name = tmp[0];
    	   String party = tmp[1];
    	   
    	   Map<String,Object> nameMap = new HashMap<String, Object>();       

    	   nameMap.put("name", name);
    	   nameMap.put("party", party);
    	   resultList.add(nameMap);
       }
       

       
       Map<String,Object> sourceAsMap2 = new HashMap<String, Object>();
       sourceAsMap2.put("member_info_list", resultList);
       try {
       json = new ObjectMapper().writeValueAsString(sourceAsMap2);
     
       }catch(Exception e) {
       }
       return json;
	}
	
	@RequestMapping(value = "/searchDiscussion"  , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public String searchDiscussion(@RequestParam("keyword") String keyword,@RequestParam("is_name") Boolean is_name){
		String INDEX_NAME = "*round*";
        //문서 타입
	  	String TYPE_NAME ="_doc";
	   String FIELD_NAME = "discussion";
       SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
 
       SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
         
    
       if(keyword.contains("\"")) {
    	   keyword = keyword.replaceAll("\"", "");
       }
       HighlightBuilder highlightBuilder = new HighlightBuilder();
       if(is_name == true) {
    	   searchSourceBuilder.query(QueryBuilders.termQuery("discussion", keyword)).highlighter(highlightBuilder.field("discussion").order("score").numOfFragments(3));  
       }
       else {
       searchSourceBuilder.query(QueryBuilders.matchQuery("discussion", keyword)).highlighter(highlightBuilder.field("discussion").order("score").numOfFragments(3));  
       }
       searchRequest.source(searchSourceBuilder);
       System.out.println(searchRequest.source().toString());

        SearchResponse searchResponse = null;
       try(RestHighLevelClient client = createConnection();){
           searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
           
//           System.out.println(searchResponse.toString());
       }catch (Exception e) {
           // TODO: handle exception
           e.printStackTrace();
           return null;
      }    


       String json =null;
//
       Map<String, SearchHits> map= new HashMap<String,SearchHits>();

       ArrayList<Map<String,Object>> resultArray = new ArrayList<Map<String,Object>>();
  
	   SearchHits searchHits = searchResponse.getHits();
	   ObjectMapper oMapper = new ObjectMapper();
	   // 아직 안하는 중

       
       for(SearchHit hit: searchHits) {
    	   
    	   
    	   String index = hit.getIndex();
		   String[] temp = index.split("th_")[1].split("round_");
		   String round = temp[0];
		   String meetingType = temp[1];
		   String time = hit.getId();
		   Map<String,Object> sourceMap = new HashMap<String,Object>();
		   sourceMap.put("round",Integer.parseInt(round));
		   sourceMap.put("time",Integer.parseInt(time));  
		   sourceMap.put("meeting_type",meetingType);

		   Text[] highlight =  hit.getHighlightFields().get("discussion").getFragments();
		   
		   String highlightString = "";
		   
		   for(int i =0; i<highlight.length;i++) {
			   String tmp = (i+1)+"."+highlight[i].toString()+"\n";
			   highlightString += tmp;
		   }
		   highlightString = highlightString.replaceAll(startEm, "");
		   highlightString = highlightString.replaceAll(endEm, "");
		   
		   highlightString = startEm+ highlightString + endEm;		   
		   
		   Map<String,Object> highlighted = new HashMap<String,Object>();
		   highlighted.put("discussion",highlightString);
		   
		   sourceMap.put("highlight",highlighted);	   
		   resultArray.add(sourceMap);
       }	
       
       
       Map<String,Object> resultMap = new HashMap<String,Object>();
       resultMap.put("result", resultArray);
	   try{
		   json = new ObjectMapper().writeValueAsString(resultMap);
       }catch(Exception e) {
       }
      
   	   return json;
       
	}

	@RequestMapping(value = "/searchDiscussionDetail"  , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public String searchHighlight(@RequestParam("round") int round,@RequestParam("time") int time,@RequestParam("meeting_type") String meeting_type,@RequestParam("keyword") String keyword){
    
       if(keyword.contains("\"")) {
    	   keyword = keyword.replaceAll("\"", "");
       }

       
		String INDEX_NAME = "*_"+round+"round_"+meeting_type;
        //문서 타입
	  	String TYPE_NAME ="_doc";
	  	
	    SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
	       
	    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	       
	     
	    searchSourceBuilder.query(QueryBuilders.idsQuery("_doc").addIds(Integer.toString(time)));
	         
	    searchRequest.source(searchSourceBuilder);
	    System.out.println(searchRequest.source().toString());

        SearchResponse searchResponse = null;
       try(RestHighLevelClient client = createConnection();){
           searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
           
//           System.out.println(searchResponse.toString());
       }catch (Exception e) {
           // TODO: handle exception
           e.printStackTrace();
           return null;
      }    


       String json =null;
//
       Map<String, SearchHits> map= new HashMap<String,SearchHits>();

       ArrayList<Map<String,Object>> resultArray = new ArrayList<Map<String,Object>>();
  
	   SearchHits searchHits = searchResponse.getHits();
	   ObjectMapper oMapper = new ObjectMapper();
	   // 아직 안하는 중
  
	   // 아직 안하는 중
       for(SearchHit hit: searchHits) {
    	   
		   Map<String, Object> source = hit.getSourceAsMap();
		   Object discussion = source.get("discussion");
		   ArrayList<String> discussionList = (ArrayList<String>) discussion;
		  

		   ArrayList<String> newStringList = new ArrayList<String>();
		   for(String eachDiscussion : discussionList) {
			   eachDiscussion = eachDiscussion.replaceAll(keyword, startEm+keyword+ endEm);
			   newStringList.add(eachDiscussion);
		   }
		   source.replace("discussion", newStringList);
		   source.put("time",time);
		   source.put("round",round);
		   source.put("meeting_type",meeting_type);
		   resultArray.add(source);
       }
	   
	    
	    Map<String,Object> resultMap = new HashMap<String,Object>();
	   
       
	    resultMap.put("result",resultArray);
	    
	   try{
		   json = new ObjectMapper().writeValueAsString(resultMap);
       }catch(Exception e) {
       }
      
   	   return json;
    
	}

	
	@RequestMapping(value = "/searchAgenda"  , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public String searchAgenda(@RequestParam("keyword") String keyword,@RequestParam("is_name") Boolean is_name){
		String INDEX_NAME = "*round*";
        //문서 타입

       SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
 
       SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
         
    
       if(keyword.contains("\"")) {
    	   keyword = keyword.replaceAll("\"", "");
       }
       
       HighlightBuilder highlightBuilder = new HighlightBuilder();
       
       if(is_name == true){
           searchSourceBuilder.query(QueryBuilders.termQuery("agenda", keyword)).highlighter(highlightBuilder.field("agenda").order("score").numOfFragments(5));  
       }
       
       searchSourceBuilder.query(QueryBuilders.matchQuery("agenda", keyword)).highlighter(highlightBuilder.field("agenda").order("score").numOfFragments(5));  
       searchRequest.source(searchSourceBuilder);
       System.out.println(searchRequest.source().toString());

        SearchResponse searchResponse = null;
       try(RestHighLevelClient client = createConnection();){
           searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);    
//           System.out.println(searchResponse.toString());
       }catch (Exception e) {
           // TODO: handle exception
           e.printStackTrace();
           return null;
      }    


       String json =null;
//
       Map<String, SearchHits> map= new HashMap<String,SearchHits>();

       ArrayList<Map<String,Object>> resultArray = new ArrayList<Map<String,Object>>();
  
	   SearchHits searchHits = searchResponse.getHits();
	   ObjectMapper oMapper = new ObjectMapper();
	   // 아직 안하는 중

       
	   
	   
       for(SearchHit hit: searchHits) {
    	   
    	   
    	   String index = hit.getIndex();
		   String[] temp = index.split("th_")[1].split("round_");
		   String round = temp[0];
		   String meetingType = temp[1];
		   String time = hit.getId();
		   Map<String,Object> sourceMap = new HashMap<String,Object>();
		   sourceMap.put("round",Integer.parseInt(round));
		   sourceMap.put("time",Integer.parseInt(time));  
		   sourceMap.put("meeting_type",meetingType);

		   Text[] highlight =  hit.getHighlightFields().get("agenda").getFragments();
		   String highlightString = "";
		   for(int i =0; i<highlight.length;i++) {
			   String tmp = (i+1)+"."+highlight[i].toString()+"\n";
			   highlightString += tmp;
		   }
		   //		   String highlightString = "1."+highlight[0].toString()+"\n2."+highlight[1].toString()+"\n3."+highlight[2].toString()+"\n4."+highlight[3].toString()+"\n 5."+highlight[4].toString() ;

		   // String highlightString = highlight[0].toString();
		   highlightString = highlightString.replaceAll(startEm, "");
		   highlightString = highlightString.replaceAll(endEm, "");
		   
		   highlightString = startEm+ highlightString + endEm;
		   
		   Map<String,Object> highlighted = new HashMap<String,Object>();
		   highlighted.put("agenda",highlightString);
		   
		   sourceMap.put("highlight",highlighted);	   
		   resultArray.add(sourceMap);
       }	
       
       
       Map<String,Object> resultMap = new HashMap<String,Object>();
       resultMap.put("result", resultArray);
	   try{
		   json = new ObjectMapper().writeValueAsString(resultMap);
       }catch(Exception e) {
       }
      
   	   return json;
       
	}

	@RequestMapping(value = "/searchAgendaDetail"  , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public String searchAgendaDetail(@RequestParam("round") int round,@RequestParam("time") int time,@RequestParam("meeting_type") String meeting_type,@RequestParam("keyword") String keyword,@RequestParam("is_name") boolean is_name){
    
       if(keyword.contains("\"")) {
    	   keyword = keyword.replaceAll("\"", "");
       }

       
		String INDEX_NAME = "*_"+round+"round_"+meeting_type;
        //문서 타입
	  	String TYPE_NAME ="_doc";
	  	
	    SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
	       
	    SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	       
	     
	    searchSourceBuilder.query(QueryBuilders.idsQuery("_doc").addIds(Integer.toString(time)));
	         
	    searchRequest.source(searchSourceBuilder);
	    System.out.println(searchRequest.source().toString());

        SearchResponse searchResponse = null;
       try(RestHighLevelClient client = createConnection();){
           searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
           
//           System.out.println(searchResponse.toString());
       }catch (Exception e) {
           // TODO: handle exception
           e.printStackTrace();
           return null;
      }    


       String json =null;
//
       Map<String, SearchHits> map= new HashMap<String,SearchHits>();

       ArrayList<Map<String,Object>> resultArray = new ArrayList<Map<String,Object>>();
  
	   SearchHits searchHits = searchResponse.getHits();
	   ObjectMapper oMapper = new ObjectMapper();
	   // 아직 안하는 중
  
	   // 아직 안하는 중
       for(SearchHit hit: searchHits) {
    	   
		   Map<String, Object> source = hit.getSourceAsMap();
		   Object agenda = source.get("agenda");
		   ArrayList<String> agendaList = (ArrayList<String>) agenda;
		  

		   ArrayList<String> newStringList = new ArrayList<String>();
		   for(String eachAgenda : agendaList) {
			   eachAgenda = eachAgenda.replaceAll(keyword, startEm+keyword+ endEm);
			   newStringList.add(eachAgenda);
		   }
		   source.replace("agenda", newStringList);
		   source.put("time",time);
		   source.put("round",round);
		   source.put("meeting_type",meeting_type);
		   resultArray.add(source);
       }
	   
	    
	    Map<String,Object> resultMap = new HashMap<String,Object>();
	   
       
	    resultMap.put("result",resultArray);
	    
	   try{
		   json = new ObjectMapper().writeValueAsString(resultMap);
       }catch(Exception e) {
       }
      
   	   return json;
    
	}

	
	@RequestMapping(value = "/searchAgendaTest"  , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public String searchAgendaTest(@RequestParam("keyword") String keyword){
		String INDEX_NAME = "*th_34*";
        //문서 타입

       SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
 
       SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
         
    
       if(keyword.contains("\"")) {
    	   keyword = keyword.replaceAll("\"", "");
       }
       
       HighlightBuilder highlightBuilder = new HighlightBuilder();
       
       searchSourceBuilder.query(QueryBuilders.termQuery("discussion", keyword)).highlighter(highlightBuilder.field("discussion").order("score").numOfFragments(5));         
       searchRequest.source(searchSourceBuilder);
       System.out.println(searchRequest.source().toString());

        SearchResponse searchResponse = null;
       try(RestHighLevelClient client = createConnection();){
           searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);    
//           System.out.println(searchResponse.toString());
       }catch (Exception e) {
           // TODO: handle exception
           e.printStackTrace();
           return null;
      }    


       String json =null;
//
       Map<String, SearchHits> map= new HashMap<String,SearchHits>();

       ArrayList<Map<String,Object>> resultArray = new ArrayList<Map<String,Object>>();
  
	   SearchHits searchHits = searchResponse.getHits();
	   ObjectMapper oMapper = new ObjectMapper();
	   // 아직 안하는 중
	   
	   int maxRound = 0;
	   int maxTime = 0;
	   int count = 0;
	   int offset = 0;
	   
       Map<String,Object> resultMap = new HashMap<String,Object>();
       
	   ArrayList<String> highlightStringResult = new ArrayList<String>();
       for(SearchHit hit: searchHits) {
    	   String index = hit.getIndex();
		   String[] temp = index.split("th_")[1].split("round_");
		   int round = Integer.parseInt(temp[0]);
		   int time = Integer.parseInt(hit.getId());
		   boolean speechFlag = false;
		   
		   ArrayList<String> highlightStringList = new ArrayList<String>();
		   Text[] highlight =  hit.getHighlightFields().get("discussion").getFragments();
		   for(int i =0; i<highlight.length;i++) {
			   String highlightString = highlight[i].toString();
			   highlightString = highlightString.replaceAll(startEm, "");
			   highlightString = highlightString.replaceAll(endEm, "");
			   String[] highlightStringTmp = highlightString.split(" ");
			   for(int j =0; j<2;j++) {
				   if(highlightStringTmp[j].contains(keyword)) {
					   speechFlag = true;
					   highlightStringList.add(highlightString);
					   break;
				   }	   
			   }
		   }
		   
		   if(speechFlag == true) {
			   if(maxRound < round) {
				   maxRound = round;
				   maxTime = time;
				   offset = count;
				   highlightStringResult.clear();
				   highlightStringResult = (ArrayList<String>) highlightStringList.clone();
			   }
			   
			   if(maxTime<time && maxRound == round) {
				   
				   maxTime= time;
				   offset = count;
				   highlightStringResult.clear();
				   highlightStringResult = (ArrayList<String>) highlightStringList.clone();
			   }
		   }

		   
		   count+=1;
      }
		
   
       count = 0;
       
       for(SearchHit hit: searchHits) {
    	   if(offset!= count) {
    		   count+=1;
    		   continue;
    	   }
    	   else {
		   Map<String,Object> sourceMap = hit.getSourceAsMap();
		   ArrayList<String> discussion = (ArrayList<String>) sourceMap.get("discussion");
		   ArrayList<String> agenda = (ArrayList<String>) sourceMap.get("agenda");
		   ArrayList<Integer> indexList = new ArrayList<Integer>();

		   ArrayList<Map<String,Object>> mapList = new ArrayList<Map<String,Object>>();
		   
		  
		   
	
		   for(String elemInhighLight : highlightStringResult) {
			   Map<String,Object> result = new HashMap<String,Object>();
			   int count2 = 0;
			   for(String eachDiscussion : discussion) {
				   if(eachDiscussion.contains(elemInhighLight)) {
					   result.put("highlight",elemInhighLight);
					   result.put("agenda",agenda.get(count2));
					   mapList.add(result);
					   break;
				   }
				   count2 +=1;
			   }

		   }

		   String index = hit.getIndex();
		   String[] temp = index.split("th_")[1].split("round_");
		   int round = Integer.parseInt(temp[0]);
		   int time = Integer.parseInt(hit.getId());
		   resultMap.put("round",round);
		   resultMap.put("time",time);
		   resultMap.put("result",mapList);
		   break;
    	   }
    	   
       }
       
       
       
       

//       resultMap.put("result", resultArray);
	   try{
		   json = new ObjectMapper().writeValueAsString(resultMap);
       }catch(Exception e) {
       }
      
   	   return json;
       
	}

	


//	@RequestMapping(value = "/searchAgendaTest"  , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//	public String searchAgendaTest(@RequestParam("keyword") String keyword){
//		String INDEX_NAME = "*th_34*";
//        //문서 타입
//
//       SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
// 
//       SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//         
//    
//       if(keyword.contains("\"")) {
//    	   keyword = keyword.replaceAll("\"", "");
//       }
//       
//       HighlightBuilder highlightBuilder = new HighlightBuilder();
//       
//       searchSourceBuilder.query(QueryBuilders.termQuery("discussion", keyword)).highlighter(highlightBuilder.field("discussion").order("score").numOfFragments(5));         
//       searchRequest.source(searchSourceBuilder);
//       System.out.println(searchRequest.source().toString());
//
//        SearchResponse searchResponse = null;
//       try(RestHighLevelClient client = createConnection();){
//           searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);    
////           System.out.println(searchResponse.toString());
//       }catch (Exception e) {
//           // TODO: handle exception
//           e.printStackTrace();
//           return null;
//      }    
//
//
//       String json =null;
////
//       Map<String, SearchHits> map= new HashMap<String,SearchHits>();
//
//       ArrayList<Map<String,Object>> resultArray = new ArrayList<Map<String,Object>>();
//  
//	   SearchHits searchHits = searchResponse.getHits();
//	   ObjectMapper oMapper = new ObjectMapper();
//	   // 아직 안하는 중
//	   
//	   int maxRound = 0;
//	   int maxTime = 0;
//	   int count = 0;
//	   int offset = 0;
//	   
//
//	   ArrayList<String> highlightStringResult = new ArrayList<String>();
//       for(SearchHit hit: searchHits) {
//    	   String index = hit.getIndex();
//		   String[] temp = index.split("th_")[1].split("round_");
//		   int round = Integer.parseInt(temp[0]);
//		   int time = Integer.parseInt(hit.getId());
//		   boolean speechFlag = false;
//		   
//		   ArrayList<String> highlightStringList = new ArrayList<String>();
//		   Text[] highlight =  hit.getHighlightFields().get("discussion").getFragments();
//		   for(int i =0; i<highlight.length;i++) {
//			   String highlightString = highlight[i].toString();
//			   highlightString = highlightString.replaceAll(startEm, "");
//			   highlightString = highlightString.replaceAll(endEm, "");
//			   String[] highlightStringTmp = highlightString.split(" ");
//			   for(int j =0; i<2;i++) {
//				   if(highlightStringTmp[i].contains(keyword)) {
//					   speechFlag = true;
//					   highlightStringList.add(highlightString);
//					   break;
//				   }	   
//			   }
//			   if(speechFlag == true) {
//				   break;
//			   }
//		   }
//		   
//		   if(speechFlag == true) {
//			   if(maxRound < round) {
//				   maxRound = round;
//				   maxTime = time;
//				   offset = count;
//				   highlightStringResult.clear();
//				   highlightStringResult = (ArrayList<String>) highlightStringList.clone();
//			   }
//			   
//			   if(maxTime<time && maxRound == round) {
//				   maxTime= time;
//				   offset = count;
//				   highlightStringResult.clear();
//				   highlightStringResult = (ArrayList<String>) highlightStringList.clone();
//
//			   }
//			   
//		   }
//
//		   
//		   count+=1;
//      }
//		
//   
//       count = -1;
//       
//       for(SearchHit hit: searchHits) {
//    	   count+= 1;
//		   Map<String,Object> sourceMap = new HashMap<String,Object>();
//
//    	   String index = hit.getIndex();
//		   String[] temp = index.split("th_")[1].split("round_");
//		   int round = Integer.parseInt(temp[0]);
//		   int time = Integer.parseInt(hit.getId());
//
//		   if(!(round == maxRound && time == maxTime)) {
//			   break;
//		   }
//  
//		  hit.get
//       }	
//       
//       
//       Map<String,Object> resultMap = new HashMap<String,Object>();
//       resultMap.put("result", resultArray);
//	   try{
//		   json = new ObjectMapper().writeValueAsString(resultMap);
//       }catch(Exception e) {
//       }
//      
//   	   return json;
//       
//	}
//
//	
//	@RequestMapping(value = "/searchAgendaHighlight"  , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//	public String searchAgendaHighlight(@RequestParam("round") int round,@RequestParam("time") int time,@RequestParam("meeting_type") String meeting_type,@RequestParam("keyword") String keyword){
//		String INDEX_NAME = "*"+round+"round_"+meeting_type;
//      
//       SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
// 
//       SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//         
//    
//       if(keyword.contains("\"")) {
//    	   keyword = keyword.replaceAll("\"", "");
//       }
//
//       BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
//       
//       searchSourceBuilder.query(boolQueryBuilder.must(QueryBuilders.matchPhraseQuery("agenda", keyword)).must(QueryBuilders.idsQuery().addIds(Integer.toString(time))));
//       
////       searchSourceBuilder.query(QueryBuilders.termQuery("discussion", keyword));  
//       searchRequest.source(searchSourceBuilder);
//       System.out.println(searchRequest.source().toString());
//
//        SearchResponse searchResponse = null;
//       try(RestHighLevelClient client = createConnection();){
//           searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
//           
////           System.out.println(searchResponse.toString());
//       }catch (Exception e) {
//           // TODO: handle exception
//           e.printStackTrace();
//           return null;
//      }    
//
//
//       String json =null;
////
//       Map<String, SearchHits> map= new HashMap<String,SearchHits>();
//
//       ArrayList<Map<String,Object>> resultArray = new ArrayList<Map<String,Object>>();
//  
//	   SearchHits searchHits = searchResponse.getHits();
//	   ObjectMapper oMapper = new ObjectMapper();
//	   // 아직 안하는 중
//
//       
//       for(SearchHit hit: searchHits) {
//    	   
//    	   String index = hit.getIndex();
//		   String[] temp = index.split("th_")[1].split("round_");
//		   String tempround = temp[0];
//		   String meetingType = temp[1];
//		   String temptime = hit.getId();
//		   Map<String,Object> sourceMap = new HashMap<String,Object>();
//		   sourceMap.put("round",Integer.parseInt(tempround));
//		   sourceMap.put("time",Integer.parseInt(temptime));  
//		   sourceMap.put("meeting_type",meetingType);
//
//		   Map<String, Object> source = hit.getSourceAsMap();
//		   String agenda = source.get("agenda").toString();
//		   agenda = agenda.replaceAll(keyword, startEm+ keyword + endEm);
//	
//		   source.replace("agenda", agenda);
//		   
//		   sourceMap.put("source", source);
//		  
//		   resultArray.add(sourceMap);
//       }	
//       
//       
//       Map<String,Object> resultMap = new HashMap<String,Object>();
//       resultMap.put("result", resultArray);
//	   try{
//		   json = new ObjectMapper().writeValueAsString(resultMap);
//       }catch(Exception e) {
//       }
//      
//   	   return json;
//       
//	}
	
	//아직 미완성 : 이 부분은 키워드 검색과 거의 유사. 따라서 보여주는 범위만 달리해주면 될 듯 함  
//	@RequestMapping(value = "/searchDiscussion"  , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//	public String documenSearchbyKeyword(@RequestParam("keyword") String keyword){
//		String INDEX_NAME = "*round_plenary_session";
//        //문서 타입
//	  	String TYPE_NAME ="_doc";
//	   String FIELD_NAME = "discussion";
//       SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
// 
//       SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//       
////       searchSourceBuilder.query(QueryBuilders.matchQuery(FIELD_NAME, name));
//   
//       searchSourceBuilder.fetchSource(false);
//       
//       //오늘추가함
//       
//       InnerHitBuilder innerHitBuilder = new InnerHitBuilder();
//       
//       searchSourceBuilder.query(QueryBuilders.nestedQuery("dialogue", QueryBuilders.boolQuery().should(QueryBuilders.matchQuery(FIELD_NAME, keyword)), ScoreMode.Avg).innerHit(innerHitBuilder.setTrackScores(true)));
//         
//       searchRequest.source(searchSourceBuilder);
//       System.out.println(searchRequest.source().toString());
//
//        SearchResponse searchResponse = null;
//       try(RestHighLevelClient client = createConnection();){
//           searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
//           
////           System.out.println(searchResponse.toString());
//       }catch (Exception e) {
//           // TODO: handle exception
//           e.printStackTrace();
//           return null;
//      }    
//
//       String json =null;
////
//       Map<String, SearchHits> map= new HashMap<String,SearchHits>();
//
//       ArrayList<Map<String,Object>> resultArray = new ArrayList<Map<String,Object>>();
//  
//	   SearchHits searchHits = searchResponse.getHits();
//	   ObjectMapper oMapper = new ObjectMapper();
//	   // 아직 안하는 중
//
//       
//       for(SearchHit hit: searchHits) {
//    	   map = hit.getInnerHits();
//    	   SearchHits tmp = map.get("dialogue");
//    	   for (SearchHit hittmp : tmp) {
//    		   if(hittmp.getScore() <2) {
//    			   continue;
//    		   }
//    		   String index = hittmp.getIndex();
//    		   String[] temp = index.split("th_")[1].split("round_");
//    		   String round = temp[0];
//    		   String meetingType = temp[1];
//    		   String time = hittmp.getId();
//    		   Map<String,Object> source = hittmp.getSourceAsMap();
//    		   
//    		   Map<String,Object> sourceMap = new HashMap<String,Object>();
//    		   sourceMap.put("round",Integer.parseInt(round));
//    		   sourceMap.put("time",Integer.parseInt(time));  
//    		   
//    		   sourceMap.put("meeting_type",meetingType);
//    		   sourceMap.put("source",source);   
//    		   resultArray.add(sourceMap);
//    		   
//    	   }
//       }	
//       
//       Map<String,Object> resultMap = new HashMap<String,Object>();
//       resultMap.put("result", resultArray);
//	   try{
//		   json = new ObjectMapper().writeValueAsString(resultMap);
//       }catch(Exception e) {
//       }
//      
//   	   return json;
//       
//	}
//	//	
	
//	@GetMapping(value = "/congressMember/{oid}" , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//	public String documentCountInMinute(@PathVariable("oid") String oid){
//		String INDEX_NAME = "congress_member_list";
//        //문서 타입
//	  	String TYPE_NAME ="_doc";
//       GetRequest request = new GetRequest(INDEX_NAME,TYPE_NAME,oid);
//       GetResponse response = null;
//     
//
//       try(RestHighLevelClient client = createConnection();){
//            response = client.get(request, RequestOptions.DEFAULT);
//        }catch (Exception e) {
//            // TODO: handle exception
//            e.printStackTrace();
//            return null;
//       }
//       
//       Map<String, Object> sourceAsMap = null;
////
//       String json = null;
//       if(response.isExists()) {
//    	   sourceAsMap = response.getSourceAsMap();
//       }
//       Object memberListBeforeSort= sourceAsMap.get("member_info_list");
//       ArrayList<String> memberListAfterSort = (ArrayList<String>) memberListBeforeSort;
//       Collections.sort(memberListAfterSort);
//       
//       Map<String,Object> sourceAsMap2 = new HashMap<String, Object>();
//       sourceAsMap2.put("member_info_list", memberListAfterSort);
//       try {
//       json = new ObjectMapper().writeValueAsString(sourceAsMap2);
//     
//       }catch(Exception e) {
//       }
//       return json;
//	}
//	
//	@GetMapping(value = "/minutes", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//	public ResponseEntity<List<MinuteVO>> getMinutes() {
//		return new ResponseEntity<>(service.getAllMinutes() ,HttpStatus.OK);
//	}
//
//	//DB Schema 잘못짬 이게 Agenda를 Get하는게 아니고 Minute을 Get헀어야했는데. Elastic하면서 반드시 고칠 것
//	@GetMapping(value = "/minute/{mid}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//	public ResponseEntity<List<AgendaVO>> getAgenda( @PathVariable("mid") int mid) {
//		 return new ResponseEntity<>(service.getAgenda(mid) ,HttpStatus.OK);
//	}
//
//	@GetMapping(value = "/minute/{aid}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//	public ResponseEntity<List<DialogueVO>> getDialouge( @PathVariable("aid") int aid) {
//		 return new ResponseEntity<>(service.getDialogue(aid) ,HttpStatus.OK);
//	}

//	@GetMapping(value = "/minute/{membername}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//	public ResponseEntity<List<DialogueVO>> getDialougeByPerson( @PathVariable("membername") int membername) {
//		 return new ResponseEntity<>(service.getDialougeByPerson(membername) ,HttpStatus.OK);
//	}
//	@GetMapping(value = "/minutes/{mid}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//	public MinuteVO getMinute() {
//		@PathVariable("mid")
//	}
}




