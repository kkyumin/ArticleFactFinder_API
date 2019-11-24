package org.factfinder.vo;

import org.factfinder.controller.HomeController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgendaVO {
	private int mid;
	private int aid;
	private String aname;
	private int anumber;
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
}
