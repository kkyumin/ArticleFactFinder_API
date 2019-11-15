package org.factfinder.controller;


import java.util.List;

import org.factfinder.service.PoliticService;
import org.factfinder.vo.AgendaVO;
import org.factfinder.vo.DialogueVO;
import org.factfinder.vo.MinuteVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequestMapping("/politic")
@RestController
@Log4j2
@AllArgsConstructor
public class PoliticController {
	PoliticService service;
	@GetMapping(value = "/minutes", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<List<MinuteVO>> getMinutes() {
		return new ResponseEntity<>(service.getAllMinutes() ,HttpStatus.OK);
	}

	//DB Schema 잘못짬 이게 Agenda를 Get하는게 아니고 Minute을 Get헀어야했는데. Elastic하면서 반드시 고칠 것
	@GetMapping(value = "/minute/{mid}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<List<AgendaVO>> getAgenda( @PathVariable("mid") int mid) {
		 return new ResponseEntity<>(service.getAgenda(mid) ,HttpStatus.OK);
	}

	@GetMapping(value = "/minute/{aid}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<List<DialogueVO>> getDialouge( @PathVariable("aid") int aid) {
		 return new ResponseEntity<>(service.getDialogue(aid) ,HttpStatus.OK);
	}

	@GetMapping(value = "/minute/{membername}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	public ResponseEntity<List<DialogueVO>> getDialougeByPerson( @PathVariable("membername") int membername) {
		 return new ResponseEntity<>(service.getDialougeByPerson(membername) ,HttpStatus.OK);
	}
//	@GetMapping(value = "/minutes/{mid}", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
//	public MinuteVO getMinute() {
//		@PathVariable("mid")
//	}
}




