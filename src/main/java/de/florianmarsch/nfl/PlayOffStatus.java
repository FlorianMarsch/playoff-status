package de.florianmarsch.nfl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlayOffStatus {

	private String week;
	private String stand;

	List<Probability> probabilities;

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getStand() {
		return stand;
	}

	public void setStand(String stand) {
		this.stand = stand;
	}

	public List<Probability> getProbabilities() {
		return probabilities;
	}

	public void setProbabilities(List<Probability> probabilities) {
		this.probabilities = probabilities;
	}

	@Override
	public String toString() {
		try {
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("week", week);
			JSONArray probabilityArray = new JSONArray();
			for (Probability probability : probabilities) {
				JSONObject value = new JSONObject();
				value.put("team", probability.getTeam());
				value.put("conference", probability.getConference());
				value.put("stat", probability.getWin() + "-" + probability.getLoose());
				value.put("superBowlWinner", probability.getSuperBowlWinner());
				value.put("owner", probability.getOwner());
				probabilityArray.put(value);
			}
			jsonObject.put("probabilities", probabilityArray);
			JSONArray chanceArray= new JSONArray();
			Map<String,Integer>chanceMap = geChanceMap(probabilities);
			for (String owner : chanceMap.keySet()) {
				JSONObject chance = new JSONObject();
				chance.put("owner", owner);
				chance.put("chance", chanceMap.get(owner));
				chanceArray.put(chance );
			}
			jsonObject.put("chance", chanceArray);
			return jsonObject.toString();
		} catch (JSONException e) {
			throw new RuntimeException("Error stringify PlayOffStatus", e);
		}
	}

	private Map<String, Integer> geChanceMap(List<Probability> probabilities2) {
		Map<String, Integer> chanceMap = new HashMap<>();
		for (Probability probability : probabilities2) {
			String owner = probability.getOwner();
			if(!owner.isEmpty()){
				Integer integer = chanceMap.get(owner);
				if(integer == null){
					integer = 0;
				}
				integer = integer + probability.getSuperBowlWinner();
				chanceMap.put(owner, integer);
			}
			
		}
		return chanceMap;
	}
}
