package org.factfinder.controller;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.factfinder.vo.MinuteVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	private static final String domain = "http://localhost:8080";
	/**
	 * Simply selects the home view to render by returning its name.
	 */
//	@RequestMapping(value = "/", method = RequestMethod.GET)
//	public String home(Locale locale, Model model) {
//		logger.info("Welcome home! The client locale is {}.", locale);
//		
//		Date date = new Date();
//		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
//		
//		String formattedDate = dateFormat.format(date);
//		
//		model.addAttribute("serverTime", formattedDate );
//		
//		return "home";
//	}

//	@RequestMapping(value = "/", method = RequestMethod.GET)
//	public String listMinute(Locale locale, Model model) {
////		logger.info("Page for Looking List per Agenda", locale);
////		
////		RestTemplate restTemplate = new RestTemplate();
////		Date date = new Date();
////		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
////
////		//Mapping Problem When get by List
////		MinuteVO[] resultClasses = restTemplate.getForObject(domain+"/controller/politic/minutes", MinuteVO[].class); 
////		List<MinuteVO> list = Arrays.asList(resultClasses);
////
////		String json = null;
////		
////		
////		try {
////			json = new ObjectMapper().writeValueAsString(list);
////		} catch (JsonProcessingException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}	
////
////		String formattedDate = dateFormat.format(date);
////		
////		model.addAttribute("serverTime", formattedDate );
////		model.addAttribute("json",json);
//		return "listPerAgenda";
//	}	

	@RequestMapping(value = "/minutes/{id}", method = RequestMethod.GET)
	public String agendaPerMinute(Locale locale, Model model) {
		return "listPerAgenda";
	}
	
}
