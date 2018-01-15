import static spark.Spark.get;
import static spark.SparkBase.port;
import static spark.SparkBase.staticFileLocation;

import java.io.InputStream;
import java.net.URL;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import de.florianmarsch.nfl.Parser;
import de.florianmarsch.nfl.PlayOffStatus;
import de.florianmarsch.nfl.Probability;
import de.florianmarsch.trello.Board;
import spark.ModelAndView;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;

public class Main {

	public static void main(String[] args) {

		new Main().init();
		
		
	}

	private String contentCache;

	public void init() {
		port(Integer.valueOf(System.getenv("PORT")));
		staticFileLocation("/public");

		get("/api/status", (request, response) -> {

			String content = getContent();
			PlayOffStatus parse = new Parser().parse(content);

			Map<String, Object> attributes = new HashMap<>();
			attributes.put("data", parse.toString());

			return new ModelAndView(attributes, "json.ftl");
		} , new FreeMarkerEngine());
		
		
		
		
		
		get("/api/sync", (request, response) -> {
			String content = getContent();
			PlayOffStatus parse = new Parser().parse(content);
			
			List<String> loosers = new ArrayList<String>();
			
			for (Probability line : parse.getProbabilities()) {
			
				if(line.getSuperBowlWinner().compareTo(0) == 0) {
					System.out.println(line.getTeam());
					loosers.add(line.getTeam());
				}
			}
			
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
