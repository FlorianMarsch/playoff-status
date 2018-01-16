package de.florianmarsch.nfl;

import org.trello4j.model.Card;

public class Team {

	private String team;
	private String stat;
	private Integer superBowlWinner;
	
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}

	public Integer getSuperBowlWinner() {
		return superBowlWinner;
	}
	public void setSuperBowlWinner(Integer superBowlWinner) {
		this.superBowlWinner = superBowlWinner;
	}
	public String getStat() {
		return stat;
	}
	public void setStat(String stat) {
		this.stat = stat;
	}
	
	public Boolean isOut() {
		return getSuperBowlWinner().compareTo(0) == 0;
	}
	public String getOutComment(Card aCard) {
		return "Die "+ aCard.getName() +" sind mit "+stat+" raus.";
	}
	
	
	
}
