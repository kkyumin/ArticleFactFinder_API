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
	
    private static final String startEm = "<strong>"; 
    private static final String endEm = "</strong>";    

    
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

	   	
	   	
		@RequestMapping(value = "/getAgendaByName"  , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
		public String getAgendaByName(@RequestBody Map<String, Object> input){

			Map<String, Object> action =(HashMap<String,Object>) input.get("action");
		    Map<String, Object> parameters = (HashMap<String,Object>) action.get("parameters");
		    Map<String, Object> subscribe_name = (HashMap<String,Object>) parameters.get("politician_name");
		    String politician_name = subscribe_name.get("value").toString();			
			String INDEX_NAME = "*th_37*";	
	        //문서 타입

	       SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
	 
	       SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	         
	    
	       if(politician_name.contains("\"")) {
	    	   politician_name = politician_name.replaceAll("\"", "");
	       }
	       
	       HighlightBuilder highlightBuilder = new HighlightBuilder();
	       
	       searchSourceBuilder.query(QueryBuilders.termQuery("discussion", politician_name)).highlighter(highlightBuilder.field("discussion").order("score").numOfFragments(10).preTags(startEm).postTags(endEm));         
	       searchRequest.source(searchSourceBuilder);
	       System.out.println(searchRequest.source().toString());

	        SearchResponse searchResponse = null;
	       try(RestHighLevelClient client = createConnection();){
	           searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);    
//	           System.out.println(searchResponse.toString());
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
					   if(highlightStringTmp[j].contains(politician_name)) {
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
						
						   String agendaFetched = agenda.get(count2);
						   int flag = 0;
						   for(Map<String,Object> mapIter : mapList) {
							   if(mapIter.containsValue(agendaFetched)) {
								   flag = 1;
								   ArrayList<String> tmpArrayList= (ArrayList<String>) mapIter.get("highlight");
								    
								   tmpArrayList.add(elemInhighLight);
								   mapIter.put("highlight", tmpArrayList);
								   mapIter.put("discussionCount", ((int) mapIter.get("discussionCount") +1));
								   break;
							   }
						   }
							   
						   if(flag == 0) {
						   result.put("agenda",agenda.get(count2));
						   ArrayList<String> tmpArrayList = new ArrayList<String>();
						   tmpArrayList.add(elemInhighLight);
						   result.put("highlight",tmpArrayList);
						   result.put("discussionCount",1);
						   mapList.add(result);
						   }
						   

						   break;
					   }
					   count2 +=1;
				   }

			   }

			   int maxDiscussionCount = 0;
			   int maxIndex = 0;
			   int currentIndex = 0;
			   for(Map<String,Object> objIter: mapList) {
				   if( (int)objIter.get("discussionCount") > maxDiscussionCount) {
					   maxDiscussionCount = (int)objIter.get("discussionCount");
					   maxIndex = currentIndex;
				   }
				   currentIndex+=1;
			   }
			   
			   
			   Map<String,Object> realResult = mapList.get(maxIndex);
			   
			   String index = hit.getIndex();
			   String[] temp = index.split("th_")[1].split("round_");
			   int round = Integer.parseInt(temp[0]);
			   int time = Integer.parseInt(hit.getId());
	
			   resultMap.put("result_agenda", realResult.get("agenda").toString());
			   resultMap.put("round",round);
			   resultMap.put("time",time);
			   resultMap.put("result",realResult);
			   break;
	    	   }
	    	   
	       }

	       
		   Map<String, Object> finalMap = new HashMap<String,Object>();
		   finalMap.put("version", "2.0");
		   finalMap.put("resultCode", "OK");
		   finalMap.put("output", resultMap);
		       
	      try{
			   json = new ObjectMapper().writeValueAsString(finalMap);
			   System.out.println(json);
	       }catch(Exception e) {
	       }
	      
	   	   return json;
	       
		}



		@RequestMapping(value = "/getAgendaByName_exist"  , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
		public String getAgendaByNameExist(@RequestBody Map<String, Object> input){
			Map<String, Object> action =(HashMap<String,Object>) input.get("action");
		    Map<String, Object> parameters = (HashMap<String,Object>) action.get("parameters");
		    parameters = parameters;
		    parameters = parameters;
		    parameters = parameters;
		    
		 
//	       String value = politicanLists.get("value").toString();

		    String json = null;
		    try{
				   json = new ObjectMapper().writeValueAsString(input);
		       }catch(Exception e) {
		       }
		 	 System.out.println(json);
		    return json;
		}
	   	//	@RequestMapping(value = "/searchAgendaByName"  , produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//	public String searchAgendaByName(@RequestBody Map<String, Object> json){
//		
//       log.info(json);
//       
//       Map<String, Object> action =(HashMap<String,Object>) json.get("action");
//       
//       Map<String, Object> parameters = (HashMap<String,Object>) action.get("parameters");
//       
//       Map<String, Object> agendaName = (HashMap<String,Object>) action.get("agenda_name");
//       
//       Map<String, Object> politician_name = (HashMap<String,Object>) action.get("politician_name");       
//
//       String value = action.get("value").toString();
//
//	   String INDEX_NAME = "*round_plenary_session";
//        //문서 타입
//	   
//	   
//      SearchRequest searchRequest = new SearchRequest(INDEX_NAME);
//
//      SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//        
//   
//      if(value.contains("\"")) {
//    	  value = value.replaceAll("\"", "");
//      }
//      
//      HighlightBuilder highlightBuilder = new HighlightBuilder();
//      searchSourceBuilder.query(QueryBuilders.matchQuery("discussion", value)).highlighter(highlightBuilder.field("discussion").order("score").numOfFragments(1));  
//      searchRequest.source(searchSourceBuilder);
//      System.out.println(searchRequest.source().toString());
//
//       SearchResponse searchResponse = null;
//      try(RestHighLevelClient client = createConnection();){
//          searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);    
////          System.out.println(searchResponse.toString());
//      }catch (Exception e) {
//          // TODO: handle exception
//          e.printStackTrace();
//          return null;
//     }    
//
////
////      String json =null;
////
//      Map<String, SearchHits> map= new HashMap<String,SearchHits>();
//
//      ArrayList<Map<String,Object>> resultArray = new ArrayList<Map<String,Object>>();
// 
//	   SearchHits searchHits = searchResponse.getHits();
//	   ObjectMapper oMapper = new ObjectMapper();
//	   // 아직 안하는 중
//
//      
//	   
//	   
//      for(SearchHit hit: searchHits) {
//   	   
//   	   
//   	   String index = hit.getIndex();
//		   String[] temp = index.split("th_")[1].split("round_");
//		   String round = temp[0];
//		   String meetingType = temp[1];
//		   String time = hit.getId();
//		   Map<String,Object> sourceMap = new HashMap<String,Object>();
//		   sourceMap.put("round",Integer.parseInt(round));
//		   sourceMap.put("time",Integer.parseInt(time));  
//		   sourceMap.put("meeting_type",meetingType);
//
//		   Text[] highlight =  hit.getHighlightFields().get("agenda").getFragments();
//		   String highlightString = highlight[0].toString();
////		   highlightString = highlightString.replaceAll(startEm, "");
////		   highlightString = highlightString.replaceAll(endEm, "");
////		   
//		   
//		   
//		   Map<String,Object> highlighted = new HashMap<String,Object>();
//		   highlighted.put("agenda",highlightString);
//		   
//		   sourceMap.put("highlight",highlighted);	   
//		   resultArray.add(sourceMap);
//      }	
//      
//      
//      Map<String,Object> resultMap = new HashMap<String,Object>();
//      resultMap.put("result", resultArray);
//             
//      
//      
//      
//      
//       
//       
////       searchSourceBuilder.query(QueryBuilders.nestedQuery("dialogue", QueryBuilders.boolQuery().should(QueryBuilders.matchQuery(FIELD_NAME, name)), ScoreMode.Avg).innerHit(innerHitBuilder.setTrackScores(true)));
////         
////       searchRequest.source(searchSourceBuilder);
////       System.out.println(searchRequest.source().toString());
////
////        SearchResponse searchResponse = null;
////       try(RestHighLevelClient client = createConnection();){
////           searchResponse = client.search(searchRequest,RequestOptions.DEFAULT);
////           
////
////        }catch (Exception e) {
////           // TODO: handle exception
////           e.printStackTrace();
////           return null;
////      }    
////
////       String json =null;
//////
////       Map<String, SearchHits> map= new HashMap<String,SearchHits>();
////
////       ArrayList<Map<String,Object>> resultArray = new ArrayList<Map<String,Object>>();
////  
////	   SearchHits searchHits = searchResponse.getHits();
////	   ObjectMapper oMapper = new ObjectMapper();
////	   // 아직 안하는 중
////
////       
////       for(SearchHit hit: searchHits) {
////    	   map = hit.getInnerHits();
////    	   SearchHits tmp = map.get("dialogue");
////    	   for (SearchHit hittmp : tmp) {
////    		   if(hittmp.getScore() <1) {
////    			   continue;
////    		   }
////    		   String index = hittmp.getIndex();
////    		   String[] temp = index.split("th_")[1].split("round_");
////    		   String round = temp[0];
////    		   String time = hittmp.getId();
////   
////    		   Map<String,Object> sourceMap = new HashMap<String,Object>();
////    		   sourceMap.put("round",Integer.parseInt(round));
////    		   sourceMap.put("time",Integer.parseInt(time));  
////    		   resultArray.add(sourceMap); 		   
////    	   }
////       }	
////       
////       Map<String,Object> resultMap = new HashMap<String,Object>();
////       resultMap.put("result", resultArray);
////	   try{
////		   json = new ObjectMapper().writeValueAsString(resultMap);
////       }catch(Exception e) {
////       }
////      
////   	   return json;
//       return null;
//	}

    

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
