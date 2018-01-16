package de.florianmarsch.nfl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Parser {

	public List<Team> parse(String content) {
		System.out.println("parse");
		Document doc = Jsoup.parse(content);
		
		
		Elements probabilityLines = doc.select(".mncntnt tr");
		System.out.println("found " + probabilityLines.size() + " probabilityLines");
		List<Element> probabilityLineList = removeHeader(probabilityLines);

		List<Team> tempProbabilities = new ArrayList<>();
		for (Element element : probabilityLineList) {
			
			String name = element.child(0).child(0).html();
			String win = element.child(2).html();
			String loose = element.child(3).html();
			String draw = element.child(4).html();
			String superBowlWinner =  element.child(5).child(0).html();
			
			Team tempProbability = getProbability(name, win, loose, draw, superBowlWinner);
			tempProbabilities.add(tempProbability);
		}

		
		return tempProbabilities;

	}

	Team getProbability(String team, String win, String loose, String draw,
			String superBowlWinner) {
	
		Team tempProbability = new Team();
		tempProbability.setTeam(team);
		
		
		String stat=win+"-";
		if(!draw.equals("0")) {
			stat=stat+draw+"-";
		}
		stat=stat+loose+"-";
		tempProbability.setStat(stat);
		
		if (superBowlWinner.contains("&lt;") || superBowlWinner.contains("x") || superBowlWinner.contains("X") ) {
			tempProbability.setSuperBowlWinner(0);
		} else {
			tempProbability.setSuperBowlWinner(Integer.valueOf(superBowlWinner.replace("%", "").replace("&lt;", "")));
		}	
		
		return tempProbability;
	}

	

	List<Element> removeHeader(Elements childNodes) {
		Iterator<Element> iterator = childNodes.iterator();
		iterator.next();
		iterator.next();
		List<Element> tempReturn = new ArrayList<Element>();
		while (iterator.hasNext()) {
			tempReturn.add(iterator.next());
		}
		return tempReturn;
	}

}
