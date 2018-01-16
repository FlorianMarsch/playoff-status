import static spark.Spark.get;
import static spark.SparkBase.port;
import static spark.SparkBase.staticFileLocation;

import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

	private String contentCache;

	public void init() {
		port(Integer.valueOf(System.getenv("PORT")));
		staticFileLocation("/public");

		
		
		get("/api/sync", (request, response) -> {
			String content = getContent();
			List<Team> teams = new Parser().parse(content);
			
			
			
			
			List<String> loosers = teams.stream()
					.filter(team -> (team.getSuperBowlWinner().compareTo(0) == 0))
					.map(team->team.getTeam())
					.collect(Collectors.toList()) ;
			
			
			getTrelloBoard().applyOuts(loosers);
			

			return "done";
		} );
		
	}

	private Board getTrelloBoard() {
		return new Board();
	}

	public String getContent() {
		if (contentCache != null) {
			return contentCache;
		}

		try {
			String content = null;
			String urlString = "http://www.playoffstatus.com/nfl/nflpostseasonprob.html";
			InputStream is = (InputStream) new URL(urlString).getContent();
			content = IOUtils.toString(is, "UTF-8");
			contentCache = content;
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("abbruch", e);
		}
	}

}
