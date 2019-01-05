package com.play.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StringSpliter {
	public Map<String, Integer> spliter(String str) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		String[] rs = str.split(";");
		for (int i = 0; i < rs.length; i++) {
			String[] rstemp = rs[i].split(":");
			map.put(rstemp[0], Integer.parseInt(rstemp[1]));
		}
		return map;
	}
	
	public List<Integer> spliter_interest(String interest) { //interest : "1；2；3；4"
		List<Integer> list = new ArrayList<Integer>();
		String[] rs = interest.split(";");
		for (int i = 0; i < rs.length; i++) {
			list.add(Integer.parseInt(rs[i]));
		}
		return list;
	}
}
