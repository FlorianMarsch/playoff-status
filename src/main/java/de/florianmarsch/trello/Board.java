package de.florianmarsch.trello;

import java.util.List;

import org.trello4j.model.Card;

public class Board {

	
	private TrelloExtendedImpl trello ;  
	private org.trello4j.model.Board wrapped;
	private org.trello4j.model.List out;
	private List<Card> teams;
	
	public Board() {
		trello = new TrelloExtendedImpl(System.getenv("trello.apiKey"), System.getenv("trello.token"));
		wrapped = trello.getBoard(System.getenv("trello.board"));
		out = trello.getList(System.getenv("trello.outs"));
		System.out.println("Sync" + wrapped.getName());
		teams = trello.getCardsByBoard(wrapped.getId());
		
	}
	
	public void applyOuts(List<String> loosers) {
		List<Card> filter = new NameMapper().filter(loosers, teams);
		for (Card card : filter) {
			if(!card.getIdList().equals(out.getId())) {
				card.setIdList(out.getId());
				System.out.println(card.getName() +" is out");
				trello.saveCard(card);
			}
		}
	}
}
