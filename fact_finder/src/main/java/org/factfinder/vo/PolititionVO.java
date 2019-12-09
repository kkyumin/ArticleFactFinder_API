package org.factfinder.vo;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PolititionVO {
	
	private String politionName;
	private String promiseList;
	private String party;
	private String history;
	private String election;

}
