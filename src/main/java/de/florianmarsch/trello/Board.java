package de.florianmarsch.trello;

import java.util.List;

import org.trello4j.model.Card;

import de.florianmarsch.nfl.Team;

public class Board {

	
	private TrelloExtendedImpl trello ;  
	private org.trello4j.model.Board wrapped;
	private org.trello4j.model.List bucket;
	private List<Card> teams;
	
	public Board() {
		trello = new TrelloExtendedImpl(System.getenv("trello.apiKey"), System.getenv("trello.token"));
		wrapped = getTrello().getBoard(System.getenv("trello.board"));
		bucket = getTrello().getList(System.getenv("trello.outs"));
		System.out.println("Sync" + wrapped.getName());
		teams = getTrello().getCardsByBoard(wrapped.getId());
		
	}
	
	public void applyOuts(Team aLooser) {
		
		String looser = resolve(aLooser.getTeam());
		
		
		teams.stream()
			.filter(card -> !isOnBucketList(card))
			.filter(card -> card.getName().equals(looser))
			.map(card -> moveToBucketList(card))
			.forEach(card -> addComment(card, aLooser));
				
	}

	void addComment(Card card, Team aLooser) {
		getTrello().addComment(card, aLooser.getOutComment());
	}

	Card moveToBucketList(Card card) {
		card.setIdList(getBucketList().getId());
		getTrello().saveCard(card);
		return card;
	}

	TrelloExtendedImpl getTrello() {
		return trello;
	}

	org.trello4j.model.List getBucketList() {
		return bucket;
	}

	boolean isOnBucketList(Card card) {
		return card.getIdList().equals(getBucketList().getId());
	}
	
	
	public String resolve(String team) {
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
