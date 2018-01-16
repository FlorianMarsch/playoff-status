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

	public List<Probability> parse(String content) {
		System.out.println("parse");
		Document doc = Jsoup.parse(content);
		
		
		Elements probabilityLines = doc.select(".mncntnt tr");
		System.out.println("found " + probabilityLines.size() + " probabilityLines");
		List<Element> probabilityLineList = removeHeader(probabilityLines);

		List<Probability> tempProbabilities = new ArrayList<>();
		for (Element element : probabilityLineList) {
			String team = element.child(0).child(0).html();
			if(team.contains("Buccaneers")){
				team = "Buccaneers";
			}
			
			String win = element.child(2).html();
			String loose = element.child(3).html();
			String draw = element.child(4).html();
			Element child = element.child(5);
			
	
			String superBowlWinner = child.child(0).html();
			Probability tempProbability = getProbability(team, win, loose, draw, superBowlWinner);
			tempProbabilities.add(tempProbability);
		}

		
		return tempProbabilities;

	}

	Probability getProbability(String team, String win, String loose, String draw,
			String superBowlWinner) {
	
		Probability tempProbability = new Probability();
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
