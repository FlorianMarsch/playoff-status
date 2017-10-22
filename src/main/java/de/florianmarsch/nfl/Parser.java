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

	public PlayOffStatus parse(String content) {
		System.out.println("parse");
		Document doc = Jsoup.parse(content);
		Elements dateLines = doc.select(".datetime");
		System.out.println("found " + dateLines.size() + " datelines");
		String dateLine = dateLines.iterator().next().html();
		Elements weekLines = doc.select(".wkinfo");
		System.out.println("found " + weekLines.size() + " weekLines");
		String weekLine = weekLines.iterator().next().html().split("&nbsp;")[3];
		Elements probabilityLines = doc.select(".mncntnt tr");
		System.out.println("found " + probabilityLines.size() + " probabilityLines");
		List<Element> probabilityLineList = removeHeader(probabilityLines);

		List<Probability> tempProbabilities = new ArrayList<>();
		for (Element element : probabilityLineList) {
			String team = element.child(0).child(0).html();
			if(team.contains("Buccaneers")){
				team = "Buccaneers";
			}
			String conference = element.child(1).html();
			String win = element.child(2).html();
			String loose = element.child(3).html();
			String draw = element.child(4).html();
			Element child = element.child(5);
			
			Boolean playoff = child.html().contains("#008000");
			String superBowlWinner = child.child(0).html();
			Probability tempProbability = getProbability(team, conference, win, loose, draw, superBowlWinner,playoff);
			tempProbabilities.add(tempProbability);
		}

		PlayOffStatus tempReturn = new PlayOffStatus();
		tempReturn.setStand(dateLine);
		tempReturn.setWeek(weekLine);
		tempReturn.setProbabilities(tempProbabilities);
		System.out.println(tempReturn.toString());
		return tempReturn;

	}

	Probability getProbability(String team, String conference, String win, String loose, String draw,
			String superBowlWinner, Boolean playOff) {
		System.out.println("getProbability " + team + " " + conference + " " + win + " " + loose + " " + draw + " "
				+ superBowlWinner+" "+playOff);
		Probability tempProbability = new Probability();
		tempProbability.setTeam(team);
		tempProbability.setConference(conference);
		tempProbability.setWin(Integer.valueOf(win));
		tempProbability.setLoose(Integer.valueOf(loose));
		tempProbability.setDraw(Integer.valueOf(draw));
		if (superBowlWinner.contains("&lt;") || superBowlWinner.contains("x") || superBowlWinner.contains("X") ) {
			tempProbability.setSuperBowlWinner(0);
		} else {
			tempProbability.setSuperBowlWinner(Integer.valueOf(superBowlWinner.replace("%", "").replace("&lt;", "")));
		}
		tempProbability.setOwner(getOwner(team));
		if(playOff){
			tempProbability.setTeam(tempProbability.getTeam()+" *");
		}
		return tempProbability;
	}

	String getOwner(String team) {
		System.out.println("getOwner for " + team);
		switch (team) {

		case "Vikings":
			return "Frederik";

		case "Patriots":
			return "Daniel";

		case "Cowboys":
			return "Frederik";

		case "Texans":
			return "Daniel";

		case "Seahawks":
			return "Frederik";

		case "Steelers":
			return "Daniel";

		case "Raiders":
			return "Daniel";

		case "Falcons":
			return "Frederik";

		case "Bills":
			return "Florian";

		case "Chiefs":
			return "Florian";

		case "Broncos":
			return "Florian";

		case "Redskins":
			return "Florian";

		case "Packers":
			return "Daniel";

		case "Titans":
			return "Daniel";

		case "Ravens":
			return "Frederik";

		case "Giants":
			return "Frederik";

		case "Eagles":
			return "Florian";

		case "Rams":
			return "Florian";

		case "Bengals":
			return "Frederik";

		case "Buccaneers":
			return "Daniel";

		case "Lions":
			return "Florian";

		case "Cardinals":
			return "Frederik";

		case "Saints":
			return "Florian";

		case "Jaguars":
			return "Daniel";

		case "Dolphins":
			return "Frederik";

		case "Chargers":
			return "";

		case "Jets":
			return "Florian";

		case "Colts":
			return "Daniel";

		case "Forty-Niners":
			return "Frederik";

		case "Panthers":
			return "Daniel";

		case "Bears":
			return "Florian";

		case "Browns":
			return "";

		default:
			return "";
		}
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
