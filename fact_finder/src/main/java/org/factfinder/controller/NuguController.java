package org.factfinder.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHost;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.InnerHitBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.factfinder.service.PoliticService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2

@CrossOrigin(origins = "*")
public class NuguController {
		

	public static ArrayList<String> politicianList= new ArrayList<String>();
	
    public RestHighLevelClient createConnection() {
        return new RestHighLevelClient(
                    RestClient.builder(
                            new HttpHost("127.0.0.1",9200,"http")
                    )
         );
    }	
    
//    {
//        "version": "2.0",
//        "resultCode": "OK",
//        "output": {
//          "datetime": "오늘",
//          KEY1: VALUE1,
//          KEY2: VALUE2,
//          ...
//        }
//    }
	@RequestMapping(value = "/subscribePolitician"  , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public String subscribePolitition(@RequestBody Map<String, Object> json){
		   Map<String, Object> action =(HashMap<String,Object>) json.get("action");
	       Map<String, Object> parameters = (HashMap<String,Object>) action.get("parameters");
	       Map<String, Object> subscribe_name = (HashMap<String,Object>) parameters.get("subscribe_name");
	       String politician_name = subscribe_name.get("value").toString();       
	       
	       politicianList.add(politician_name);
	       
	       Map<String, Object> resultMap = new HashMap<String,Object>();
	       resultMap.put("version", "2.0");
	       resultMap.put("resultCode", "OK");
	       
	       String result= null;
	       try {
	    	   result = new ObjectMapper().writeValueAsString(resultMap);   
	       }catch(Exception e) {
	    	   
	       }
	       return result;
	       
	}
	@RequestMapping(value = "/unscribePolitician"  , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public String unSubscribePolitition(@RequestBody Map<String, Object> json){
		   Map<String, Object> action =(HashMap<String,Object>) json.get("action");
	       Map<String, Object> parameters = (HashMap<String,Object>) action.get("parameters");
	       Map<String, Object> subscribe_name = (HashMap<String,Object>) parameters.get("subscribe_name");
	       String politician_name = subscribe_name.get("value").toString();       
	        
	       politicianList.remove(politician_name);
	       
	       Map<String, Object> resultMap = new HashMap<String,Object>();
	       resultMap.put("version", "2.0");
	       resultMap.put("resultCode", "OK");
	       
	       String result= null;
	       try {
	    	   result = new ObjectMapper().writeValueAsString(resultMap);   
	       }catch(Exception e) {
	    	   
	       }
	       return result;
	}

//    {
//  "version": "2.0",
//  "resultCode": "OK",
//  "output": {
//    "datetime": "오늘",
//    KEY1: VALUE1,
//    KEY2: VALUE2,
//    ...
//  }
//}

	
	@RequestMapping(value = "/getSubscribedList"  , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public String getSubscribeList(@RequestBody Map<String, Object> json){
//		   Map<String, Object> action =(HashMap<String,Object>) json.get("action");
//	       Map<String, Object> parameters = (HashMap<String,Object>) action.get("parameters");
//	       Map<String, Object> agendaName = (HashMap<String,Object>) action.get("agenda_name");
//	       Map<String, Object> politician_name = (HashMap<String,Object>) action.get("politician_name");       
//	       String value = action.get("value").toString();

		   Map<String, Object> resultMap = new HashMap<String,Object>();
	       resultMap.put("version", "2.0");
	       resultMap.put("resultCode", "OK");
	       
	       Map<String, Object> outputMap = new HashMap<String,Object>();
	       
	       String politicianString = "";
	       for(String politician : politicianList) {
	    	   politicianString += (politician + "  ");
	       }
	       if(!politicianString.equals("")) {
	    	   outputMap.put("politicianList", politicianString);
	       }
	       resultMap.put("output", outputMap);
	       String result= null;
	       try {
	    	   result = new ObjectMapper().writeValueAsString(resultMap);   
	       }catch(Exception e) {
	    	   
	       }
	       return result;
	       
	}
	   	@RequestMapping(value = "/getSubscribedList_exist"  , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
		public String getSubscribeListExist(@RequestBody Map<String, Object> json){
			   Map<String, Object> action =(HashMap<String,Object>) json.get("action");
		       Map<String, Object> parameters = (HashMap<String,Object>) action.get("parameters");
		       Map<String, Object> politicanLists  = (HashMap<String,Object>) parameters.get("politicianList");
		       String value = politicanLists.get("value").toString();

//		       politicanList2 =politicanList2;
//		    		   politicanList2= politicanList2;
//		    		   politicanList2 = politicanList2;
//		       String value = politicanList.get("value").toString();
//
			   Map<String, Object> resultMap = new HashMap<String,Object>();
		       resultMap.put("version", "2.0");
		       resultMap.put("resultCode", "OK");
		       
		       Map<String, Object> outputMap = new HashMap<String,Object>();
		       
//		       String politicianString = "";
//		       for(String politician : politicianList) {
//		    	   politicianString += (politician + "  ");
//		       }
//		       if(!politicianString.equals("")) {
//		    	   outputMap.put("politicianList", politicianString);
//		       }
	    	   outputMap.put("politicianList", value);

		       resultMap.put("output", outputMap);
//		       String result= null;
	   			String result = null;
		       try {
		    	   result = new ObjectMapper().writeValueAsString(resultMap);
		       }catch(Exception e) {
		       }
		       return result;

//	       return null;

	
	}
	@RequestMapping(value = "/searchAgendaByName"  , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public String searchAgendaByName(@RequestBody Map<String, Object> json){
		
       log.info(json);
       
       Map<String, Object> action =(HashMap<String,Object>) json.get("action");
       
       Map<String, Object> parameters = (HashMap<String,Object>) action.get("parameters");
       
       Map<String, Object> agendaName = (HashMap<String,Object>) action.get("agenda_name");
       
       Map<String, Object> politician_name = (HashMap<String,Object>) action.get("politician_name");       

       String value = action.get("value").toString();

	   String INDEX_NAME = "*round_plenary_session";
        //문서 타입
	   
	   
      SearchRequest searchRequest = new SearchRequest(INDEX_NAME);

      SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        
   
      if(value.contains("\"")) {
    	  value = value.replaceAll("\"", "");
      }
      
      HighlightBuilder highlightBuilder = new HighlightBuilder();
      searchSourceBuilder.query(QueryBuilders.matchQuery("discussion", value)).highlighter(highlightBuilder.field("discussion").order("score").numOfFragments(1));  
      searchRequest.source(searchSourceBuilder);
      System.out.println(searchRequest.source().toString());

       SearchResponse searchResponse = null;
      try(RestHighLevelClient client = createConnection();){
          searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);    
//          System.out.println(searchResponse.toString());
      }catch (Exception e) {
          // TODO: handle exception
          e.printStackTrace();
          return null;
     }    

//
//      String json =null;
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
		   String highlightString = highlight[0].toString();
//		   highlightString = highlightString.replaceAll(startEm, "");
//		   highlightString = highlightString.replaceAll(endEm, "");
//		   
		   
		   
		   Map<String,Object> highlighted = new HashMap<String,Object>();
		   highlighted.put("agenda",highlightString);
		   
		   sourceMap.put("highlight",highlighted);	   
		   resultArray.add(sourceMap);
      }	
      
      
      Map<String,Object> resultMap = new HashMap<String,Object>();
      resultMap.put("result", resultArray);
             
       
       
//       searchSourceBuilder.query(QueryBuilders.nestedQuery("dialogue", QueryBuilders.boolQuery().should(QueryBuilders.matchQuery(FIELD_NAME, name)), ScoreMode.Avg).innerHit(innerHitBuilder.setTrackScores(true)));
//         
//       searchRequest.source(searchSourceBuilder);
//       System.out.println(searchRequest.source().toString());
//
//        SearchResponse searchResponse = null;
//       try(RestHighLevelClient client = createConnection();){
//           searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
//           
//
//        }catch (Exception e) {
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
//    		   if(hittmp.getScore() <1) {
//    			   continue;
//    		   }
//    		   String index = hittmp.getIndex();
//    		   String[] temp = index.split("th_")[1].split("round_");
//    		   String round = temp[0];
//    		   String time = hittmp.getId();
//   
//    		   Map<String,Object> sourceMap = new HashMap<String,Object>();
//    		   sourceMap.put("round",Integer.parseInt(round));
//    		   sourceMap.put("time",Integer.parseInt(time));  
//    		   resultArray.add(sourceMap); 		   
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
       return null;
	}

    

//	@RequestMapping(value = "/agendaAnswer"  , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//	public String documenSearchbyName(@RequestBody Map<String, Object> json){
//		
//	   json =json;
//	   String INDEX_NAME = "*round_plenary_session";
//        //문서 타입
//	   String TYPE_NAME ="_doc";
//	   String FIELD_NAME = "dialogue.discussion";
//       SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
// 
//       SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//              
//       InnerHitBuilder innerHitBuilder = new InnerHitBuilder();
//       
//       searchSourceBuilder.query(QueryBuilders.nestedQuery("dialogue", QueryBuilders.boolQuery().should(QueryBuilders.matchQuery(FIELD_NAME, name)), ScoreMode.Avg).innerHit(innerHitBuilder.setTrackScores(true)));
//         
//       searchRequest.source(searchSourceBuilder);
//       System.out.println(searchRequest.source().toString());
//
//        SearchResponse searchResponse = null;
//       try(RestHighLevelClient client = createConnection();){
//           searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
//           
//
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
//    		   if(hittmp.getScore() <1) {
//    			   continue;
//    		   }
//    		   String index = hittmp.getIndex();
//    		   String[] temp = index.split("th_")[1].split("round_");
//    		   String round = temp[0];
//    		   String time = hittmp.getId();
//   
//    		   Map<String,Object> sourceMap = new HashMap<String,Object>();
//    		   sourceMap.put("round",Integer.parseInt(round));
//    		   sourceMap.put("time",Integer.parseInt(time));  
//    		   resultArray.add(sourceMap); 		   
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
    
}
