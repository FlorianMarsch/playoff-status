import static spark.Spark.get;
import static spark.SparkBase.port;

import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;

import de.florianmarsch.nfl.Parser;
import de.florianmarsch.nfl.Team;
import de.florianmarsch.trello.Board;

public class Main {

	public static void main(String[] args) {

		new Main().init();
		
		
	}

	
	void init() {
		port(Integer.valueOf(System.getenv("PORT")));
		
		get("/api/sync", (request, response) -> handleSync());
		
	}


	String handleSync() {
		Board trelloBoard = getTrelloBoard();
		
		getTeams()
			.filter(team -> team.isOut())
			.forEach(looser -> trelloBoard.applyOuts(looser));

		return "done";
	}

	Stream<Team> getTeams() {
		String content = getContent();
		List<Team> teams = new Parser().parse(content);
		return teams.stream();
	}

	Board getTrelloBoard() {
		return new Board();
	}

	String getContent() {
		

		try {
			String content = null;
			String urlString = "http://www.playoffstatus.com/nfl/nflpostseasonprob.html";
			InputStream is = (InputStream) new URL(urlString).getContent();
			content = IOUtils.toString(is, "UTF-8");
			
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("abbruch", e);
		}
	}

}
