package org.factfinder.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.factfinder.vo.AgendaVO;
import org.factfinder.vo.DialogueVO;
import org.factfinder.vo.MinuteVO;
import org.factfinder.vo.PolititionVO;

public interface PoliticMapper {
	public List<MinuteVO> getAllMinutesList();
	public List<AgendaVO> getAgenda(@Param("mid") int mid);
	public List<DialogueVO> getDialogue(@Param("aid") int aid);
	public List<PolititionVO> getSubscribe(@Param("uid") int uid);
//	public List<PolititionVO> setSubscribe(@Param("uid") int uid, );
	
}
