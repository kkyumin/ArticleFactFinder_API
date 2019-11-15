package org.factfinder.controller;

import org.factfinder.vo.SampleVO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;

import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/sample")
@Log4j2
public class SampleController {
	@GetMapping(value = "/getText", produces = "text/plain; charset = UTF-8")
	public String getText() {
		log.info("MIME TYPE:"+ MediaType.TEXT_PLAIN_VALUE);
		return "안녕하세요";
	}
	@GetMapping(value = "/getSamp", produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
	public SampleVO getSample() {
			return new SampleVO(112,"스타","로드");
	}
}
