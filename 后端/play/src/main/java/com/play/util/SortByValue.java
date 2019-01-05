package com.play.util;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

public class SortByValue implements Comparator<Map.Entry<Integer, Double>>{

	@Override
	public int compare(Entry<Integer, Double> o1, Entry<Integer, Double> o2) {
		// TODO Auto-generated method stub
		if (o2.getValue() > o1.getValue()){
			return 1;
		}else{
			return -1;
		}
	}

}
