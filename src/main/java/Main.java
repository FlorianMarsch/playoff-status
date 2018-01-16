import static spark.Spark.get;
import static spark.SparkBase.port;
import static spark.SparkBase.staticFileLocation;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.IOUtils;

import de.florianmarsch.nfl.Parser;
import de.florianmarsch.nfl.Team;
import de.florianmarsch.trello.Board;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

public class Main {

	public static void main(String[] args) {

		new Main().init();
		
		
	}

	
	public void init() {
		port(Integer.valueOf(System.getenv("PORT")));
		staticFileLocation("/public");

		
		
		get("/api/sync", (request, response) -> {
			
			getTeams()
				.filter(team -> (team.getSuperBowlWinner().compareTo(0) == 0))
				.map(team->team.getTeam())
				.forEach(looser -> getTrelloBoard().applyOuts(looser));	

			return "done";
		} );
		
	}

	private Stream<Team> getTeams() {
		String content = getContent();
		List<Team> teams = new Parser().parse(content);
		return teams.stream();
	}

	private Board getTrelloBoard() {
		return new Board();
	}

	public String getContent() {
		

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
