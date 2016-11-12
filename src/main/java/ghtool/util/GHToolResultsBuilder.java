package ghtool.util;

import org.json.JSONArray;
import org.json.JSONObject;

public class GHToolResultsBuilder {

	// when more than 100 result requested after each 100s load to continue from previous page count
	public static int count = 0;
	
	/**
	 * @param s
	 * @return
	 * 
	 * This method is used for basic display of results (details displayed are repository ID, Full Name and Language)
	 */
	public static String displayBasic(String s) {
		String response = "";
		
		JSONArray ja = new JSONArray((new JSONObject(s)).get("items").toString());
		
		for (Object object : ja) {
			count++;
			response += count + " ID: " + ((JSONObject) object).get("id")
						+ "\t\tFull name: " + ((JSONObject) object).getString("full_name") 
						+ " -----> " + "Language: " + ((JSONObject) object).get("language") + "\n";
		}
		
		return response;
	}

	/**
	 * @param s
	 * @return
	 * 
	 * This method is used for displaying description of results 
	 * (details displayed are repository ID, Full Name, Owner(by his login name), if repository is private, Description, Size)
	 */
	public static String displayRepoDescription(String s) {
		String response = "";
		
		JSONArray ja = new JSONArray((new JSONObject(s)).get("items").toString());
		
		int count = 0;
		for (Object object : ja) {
			count++;
			response += count + " ID: " + ((JSONObject) object).get("id")
						+ "\n\tFull name: " + ((JSONObject) object).getString("full_name")
						+ "\n\tOwner: " + ((JSONObject) object).getJSONObject("owner").get("login")
						+ "\n\tIs private: " + ((JSONObject) object).get("private")
						+ "\n\tDescription: " + ((JSONObject) object).getString("description")
						+ "\n\tSize: " + ((JSONObject) object).get("size")
						+ "\n";
		}
		
		return response;
	}

}
