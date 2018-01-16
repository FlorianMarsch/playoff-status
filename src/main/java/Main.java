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
import de.florianmarsch.nfl.Probability;
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
			List<Probability> playoffstatus = new Parser().parse(content);
			
			
			
			
			List<String> loosers = playoffstatus.stream()
					.filter(team -> (team.getSuperBowlWinner().compareTo(0) == 0))
					.map(team->team.getTeam())
					.collect(Collectors.toList()) ;
			
			
			new Board().applyOuts(loosers);
			

			Map<String, Object> attributes = new HashMap<>();
			attributes.put("data", "synced");

			return new ModelAndView(attributes, "json.ftl");
		} , new FreeMarkerEngine());
		
	}

	public String getContent() {
		if (contentCache != null) {
			return contentCache;
		}

		try {
			System.out.println("get Content");
			String content = null;
			String urlString = "http://www.playoffstatus.com/nfl/nflpostseasonprob.html";
			InputStream is = (InputStream) new URL(urlString).getContent();
			content = IOUtils.toString(is, "UTF-8");
			System.out.println("recive " + content.length() + " bytes");
			contentCache = content;
			return content;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("abbruch", e);
		}
	}

}
