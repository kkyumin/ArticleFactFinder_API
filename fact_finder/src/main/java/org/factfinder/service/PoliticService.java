package org.factfinder.service;

import java.util.List;

import org.factfinder.mapper.PoliticMapper;
import org.factfinder.vo.AgendaVO;
import org.factfinder.vo.DialogueVO;
import org.factfinder.vo.MinuteVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;

public interface PoliticService {
	public List<MinuteVO> getAllMinutes();
	public List<AgendaVO> getAgenda(int mid);
	public List<DialogueVO> getDialogue(int aid);
//	public List<VO> getDialougeByPerson(String membername);
}
