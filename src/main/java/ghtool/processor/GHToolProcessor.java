package ghtool.processor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import ghtool.util.GHToolComandsParser;

public class GHToolProcessor {

	private static HttpURLConnection getConnection(String url) {
		HttpURLConnection connection = null;
		try {
			URL obj = new URL(url);
			connection = (HttpURLConnection) obj.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type","application/json");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return connection;
	}
	
	
	
	public static String processCommand() throws IOException {
		
		StringBuffer response = new StringBuffer();
		
		Map<String, String> currentCommandsMap = GHToolComandsParser.getCurrentCommandsMap();
		
		if (currentCommandsMap != null && !currentCommandsMap.isEmpty()) {
			
			// If GHTool instructions are requested 
			if (currentCommandsMap.size() == 1) {
				GHToolComandsParser.showGHToolInstructions();
			}
			
			// If GHTool description instructions are requested
			if (currentCommandsMap.size() == 2 && currentCommandsMap.containsKey(GHToolComandsParser.DESC) && currentCommandsMap.get(GHToolComandsParser.DESC).isEmpty()) {
				GHToolComandsParser.showGHToolDescInstructions();
			}
			
			// if results from GitHub are requested
			else if (currentCommandsMap.size() > 1) {
				
				String url = GHToolProcessorHelper.buildURL();
				//System.out.println(url);
				HttpURLConnection connection = getConnection(url);
				
				int responseCode = connection.getResponseCode();
	
				if (responseCode == 200) {
					BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
					
					String inputLine;
					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();
				} else {
					System.err.println("[ERROR] ResponseCode: " + responseCode);
				}
			}
		}
		
		return response.toString();
	}

}
