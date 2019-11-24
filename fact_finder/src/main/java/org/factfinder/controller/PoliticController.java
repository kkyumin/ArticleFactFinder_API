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
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.factfinder.service.PoliticService;
import org.factfinder.vo.AgendaVO;
import org.factfinder.vo.DialogueVO;
import org.factfinder.vo.MinuteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.log.Log;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
@RequestMapping("/politic")
@RestController
@Log4j2
@AllArgsConstructor
public class PoliticController {
	
	PoliticService service;
    public RestHighLevelClient createConnection() {
        return new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost("127.0.0.1",9200,"http")
                    )
         );
    }	
    
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
	        
	        
	        for(SearchHit hit: searchHits) {
	        	idAsMap.put("_id",hit.getId());
	        }	
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
	

	@GetMapping(value = "/{oid}/ordinal/{rid}/round/{tid}/time" , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public String dialogueInTime(@PathVariable("oid") String oid,@PathVariable("rid") String rid, @PathVariable("tid") String tid ){
		String INDEX_NAME = oid+"th_"+rid+"round_plenary_session";
        //문서 타입
	  	String TYPE_NAME ="_doc";
	  	
        GetRequest request = new GetRequest(INDEX_NAME,TYPE_NAME,tid);
        GetResponse response = null;
       try(RestHighLevelClient client = createConnection();){
            response = client.get(request, RequestOptions.DEFAULT);
        }catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
       Map<String, Object> sourceAsMap = null;
       if(response.isExists()) {
           sourceAsMap = response.getSourceAsMap();
       }
       String json = null;
       try {
       json = new ObjectMapper().writeValueAsString(sourceAsMap);
       }catch(Exception e) {
       }
       return json;
	}
	
	@GetMapping(value = "/congressMember/{oid}" , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public String documentCountInMinute(@PathVariable("oid") String oid){
		String INDEX_NAME = "congress_member_list";
        //문서 타입
	  	String TYPE_NAME ="_doc";
       GetRequest request = new GetRequest(INDEX_NAME,TYPE_NAME,oid);
       GetResponse response = null;
     

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
       ArrayList<String> memberListAfterSort = (ArrayList<String>) memberListBeforeSort;
       Collections.sort(memberListAfterSort);
       
       Map<String,Object> sourceAsMap2 = new HashMap<String, Object>();
       sourceAsMap2.put("member_info_list", memberListAfterSort);
       try {
       json = new ObjectMapper().writeValueAsString(sourceAsMap2);
     
       }catch(Exception e) {
       }
       return json;
	}

//	@GetMapping(value = "/document/{name}" , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//	public String documenSearchbyName(@PathVariable("name") String name){
//		String INDEX_NAME = "*round_plenary_session";
//        //문서 타입
//	  	String TYPE_NAME ="_doc";
//	   String FIELD_NAME = "dialogue.discussion";
//       SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
// 
//       SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//       
//       searchSourceBuilder.query(QueryBuilders.matchQuery(FIELD_NAME, name));
//   
//       QueryBuilders.
//       searchRequest.source(searchSourceBuilder);
//
//        SearchResponse searchResponse;
//       try(RestHighLevelClient client = createConnection();){
//           searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
//       }catch (Exception e) {
//           // TODO: handle exception
//           e.printStackTrace();
//           return null;
//      }    	
////       	Request request = new Request("GET", INDEX_NAME);
//          
//       		
//	        SearchHits searchHits = searchResponse.getHits();
//	        Map<String, Object> idAsMap= new HashMap<>();
//	        
//	        
//	        for(SearchHit hit: searchHits) {
//	        	idAsMap.put("_id",hit.getId());
//	        }	
//
//	        String json = new ObjectMapper().writeValueAsString(idAsMap);
//	   return json;
//       
//	}
	
	
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




