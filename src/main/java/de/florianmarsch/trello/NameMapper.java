package de.florianmarsch.trello;

import java.util.ArrayList;
import java.util.List;

import org.trello4j.model.Card;

public class NameMapper {
	
	
	public List<Card> filter(List<String> outs, List<Card> someCards){
		List<String> resolvedOuts = new ArrayList<>();
		for (String team : outs) {
			resolvedOuts.add(resolve(team));
		}
		List<Card> filtered = new ArrayList<>();
		for (Card card : someCards) {
			if(resolvedOuts.contains(card.getName())) {
				filtered.add(card);
			}
		}
		return filtered;
	}
	
	String resolve(String team) {
		switch (team) {

		case "Saints":
			return "New Orleans Saints";

		case "Jaguars":
			return "Jacksonville Jaguars";

		case "Dolphins":
			return "Miami Dolphins";

		case "Chargers":
			return "Los Angeles Chargers";

		case "Jets":
			return "New York Jets";

		case "Colts":
			return "Indianapolis Colts";

		case "Forty-Niners":
			return "San Francisco 49ers";

		case "Panthers":
			return "Carolina Panthers";

		case "Bears":
			return "Chicago Bears";

		case "Browns":
			return "Cleveland Browns";
			
		case "Giants":
			return "New York Giants";

		case "Eagles":
			return "Philadelphia Eagles";

		case "Rams":
			return "Los Angeles Rams";

		case "Bengals":
			return "Cincinnati Bengals";

		case "Buccaneers":
			return "Tampa Bay Buccaneers";

		case "Lions":
			return "Detroit Lions";

		case "Cardinals":
			return "Arizona Cardinals";
			
		case "Chiefs":
			return "Kansas City Chiefs";

		case "Broncos":
			return "Denver Broncos";

		case "Redskins":
			return "Washington Redskins";

		case "Packers":
			return "Green Bay Packers";

		case "Titans":
			return "Tennesse Titans";

		case "Ravens":
			return "Baltimore Ravens";
			
		case "Vikings":
			return "Minnesota Vikings";

		case "Patriots":
			return "New England Patriots";

		case "Cowboys":
			return "Dallas Cowboys";

		case "Texans":
			return "Houston Texans";

		case "Seahawks":
			return "Seattle Seahawks";

		case "Steelers":
			return "Pittsburgh Steelers";

		case "Raiders":
			return "Oakland Raiders";

		case "Falcons":
			return "Atlanta Falcons";

		case "Bills":
			return "Buffalo Bills";	
			
		default:
			return "";
		}
	}


}
