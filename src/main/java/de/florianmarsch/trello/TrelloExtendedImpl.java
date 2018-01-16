package de.florianmarsch.trello;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;

import org.trello4j.TrelloException;
import org.trello4j.TrelloImpl;
import org.trello4j.TrelloURL;
import org.trello4j.model.Card;

public class TrelloExtendedImpl extends TrelloImpl {

    private static final String METHOD_DELETE   = "DELETE";
    private static final String METHOD_GET      = "GET";
    private static final String METHOD_POST     = "POST";
    private static final String METHOD_PUT      = "PUT";
	private static final String GZIP_ENCODING   = "gzip";
	
	
	
	
	String apiKey;
	String token;
	
	public TrelloExtendedImpl(String apiKey, String token) {
		super(apiKey, token);
		this.apiKey = apiKey;
		this.token = token;
	}

	public void saveCard(Card card) {
		String id = card.getId();
		String idList = card.getIdList();
		
		final String url = TrelloURL.CARD_POST_URL+"/"+id+"?idList="+idList+"&key="+apiKey+"&token="+token;
		
		System.out.println(url);
		HashMap<String, String> keyValueMap = new HashMap<String, String>();

		doRequest(url, METHOD_PUT, keyValueMap);
		
	}
	

	public void addComment(Card card, String aComment) {
		String id = card.getId();
		
		String comment;
		try {
			comment = URLEncoder.encode(aComment, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(aComment+" can not be parsed");
		}
		final String url = TrelloURL.CARD_POST_URL+"/"+id+"/actions/comments?text="+comment+"&key="+apiKey+"&token="+token;
		
		System.out.println(url);
		HashMap<String, String> keyValueMap = new HashMap<String, String>();

		doRequest(url, METHOD_POST, keyValueMap);
		
	}

	
	
	InputStream doRequest(String url, String requestMethod, Map<String, String> map) {
		try {
			
			
			HttpsURLConnection conn = (HttpsURLConnection) new URL(url)
					.openConnection();
			conn.setRequestProperty("Accept-Encoding", "gzip, deflate");
            conn.setDoOutput(requestMethod.equals(METHOD_POST) || requestMethod.equals(METHOD_PUT));
            conn.setRequestMethod(requestMethod);

            if(map != null && !map.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (String key : map.keySet()) {
                    sb.append(sb.length() > 0 ? "&" : "")
                        .append(key)
                        .append("=")
                        .append(URLEncoder.encode(map.get(key), "UTF-8"));
                }
                conn.getOutputStream().write(sb.toString().getBytes());
                conn.getOutputStream().close();
            }

			if (conn.getResponseCode() > 399) {
				return null;
			} else {
				return getWrappedInputStream(
                    conn.getInputStream(), GZIP_ENCODING.equalsIgnoreCase(conn.getContentEncoding())
                );
			}
		} catch (IOException e) {
			throw new TrelloException(e.getMessage());
		}
	}
	
	InputStream getWrappedInputStream(InputStream is, boolean gzip)
			throws IOException {
		/*
		 * TODO: What about this? ---------------------- "Java clients which use
		 * java.util.zip.GZIPInputStream() and wrap it with a
		 * java.io.BufferedReader() to read streaming API data will encounter
		 * buffering on low volume streams, since GZIPInputStream's available()
		 * method is not suitable for streaming purposes. To fix this, create a
		 * subclass of GZIPInputStream() which overrides the available()
		 * method."
		 * 
		 * https://dev.twitter.com/docs/streaming-api/concepts#gzip-compression
		 */
		if (gzip) {
			return new BufferedInputStream(new GZIPInputStream(is));
		} else {
			return new BufferedInputStream(is);
		}
	}

	
	
}
