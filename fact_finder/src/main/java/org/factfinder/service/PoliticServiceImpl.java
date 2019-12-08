package org.factfinder.service;

import java.util.List;

import org.factfinder.mapper.PoliticMapper;
import org.factfinder.vo.AgendaVO;
import org.factfinder.vo.DialogueVO;
import org.factfinder.vo.MinuteVO;
import org.factfinder.vo.PolititionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PoliticServiceImpl implements PoliticService{
	@Setter(onMethod_ = @Autowired)
	private PoliticMapper mapper;
	@Override
	public List<MinuteVO> getAllMinutes(){
		log.info("getAllMinutes");
		return mapper.getAllMinutesList();
	}
	@Override
	public List<AgendaVO> getAgenda(int mid){
		log.info("getAgenda of mid: "+ mid);
		return mapper.getAgenda(mid);
	}
	
	@Override
	public List<DialogueVO> getDialogue(int aid){
		log.info("getDialogue of aid: "+ aid);
		return mapper.getDialogue(aid);
	}
//	@Override
//	public List<PolititionVO> getSubscribe(int uid){
//		log.info("get Subscribed List of uid: "+ uid);
//		return mapper.getSubsribe(uid);
//	}
//	
//	@Override
//	public void setSubscribe(int aid,String pname){
//		log.info("set Subscribed List of aid: "+ aid, pname);
//		return mapper.setSubsribe(aid,pname);
//	}
//	
//	@Override
//	public List<PolititionVO> getPolititionData(String pname){
//		log.info("get Subscribed List of aid: "+ pname);
//		return mapper.getPolititionData(pname);
//	}
	
}	
