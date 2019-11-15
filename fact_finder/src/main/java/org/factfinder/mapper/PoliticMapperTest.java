package org.factfinder.mapper;

import java.util.List;

import org.factfinder.vo.AgendaVO;
import org.factfinder.vo.MinuteVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@RunWith(SpringRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/spring/root-context.xml")
@Log4j2
public class PoliticMapperTest {
	@Setter(onMethod_ = @Autowired)
	private PoliticMapper mapper;
	
	@Test
	public void getAllMinutesList(){
		List<MinuteVO> minutes = mapper.getAllMinutesList();
		minutes.forEach(minute->log.info(minute));
	}
	@Test
	public void getAgendaList(int mid){
		List<AgendaVO> agendas = mapper.getAgenda(mid);
		agendas.forEach(agenda->log.info(agenda));
	}

}
