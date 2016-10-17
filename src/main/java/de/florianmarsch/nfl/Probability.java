package de.florianmarsch.nfl;

public class Probability {

	private String team;
	private String conference;
	private Integer win;
	private Integer loose;
	private Integer draw;
	private Integer superBowlWinner;
	private String owner;
	
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	public String getConference() {
		return conference;
	}
	public void setConference(String conference) {
		this.conference = conference;
	}
	public Integer getWin() {
		return win;
	}
	public void setWin(Integer win) {
		this.win = win;
	}
	public Integer getLoose() {
		return loose;
	}
	public void setLoose(Integer loose) {
		this.loose = loose;
	}
	public Integer getDraw() {
		return draw;
	}
	public void setDraw(Integer draw) {
		this.draw = draw;
	}
	public Integer getSuperBowlWinner() {
		return superBowlWinner;
	}
	public void setSuperBowlWinner(Integer superBowlWinner) {
		this.superBowlWinner = superBowlWinner;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	
}
