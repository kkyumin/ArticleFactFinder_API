package org.factfinder.vo;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVO {
	private String uid;
	private String userName;
	private String password;
	private String loginFlag;
	private ArrayList<PolititionVO> polititionList;

}
