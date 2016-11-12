package ghtool.util;

import java.util.HashMap;
import java.util.Map;

import ghtool.main.GHToolMain;

public class GHToolComandsParser {
	
	private static Map<String, String> currentCommandsMap;

	public static final String GHTOOL				= "-ghtool";
	public static final String LIST_REPOSITORIES	= "list";
	public static final String NUMBER_OF_RESULTS	= "-n";
	public static final String LANGUAGE_SEARCH		= "-language";
	public static final String DESC					= "desc";
	public static final String SORT_BY_DATE_UPDATED = "-sort";
	
	public static void prepareMapForOptions() {
		if (currentCommandsMap == null) {
			currentCommandsMap = new HashMap<String, String>();
		}
		currentCommandsMap.clear();
	}

	/**
	 * @param line
	 * @throws Exception
	 * 
	 * This method is used to parse options user supplied and validate them
	 */
	public static void parseOptions(String line) throws Exception {
		
		// validate first level commands (eg. -ghtool, -ghtool list, -ghtool desc)
		validateFirsLevelCommand(line);
		
		prepareMapForOptions();
		
		String[] command  = line.split(" ");
		
		for (int i = 0; i < command.length; i++) {
			// validate each option from command to see if parsable
			validateOption(command[i]);
			
			String validCommand = command[i];
			
			// options values
			int numberOfResults = 0;
			String language = "";
			String repos = "";
			String sortOrder = "";
			
			// if -n is specified it has to be followed by number (of results requested)
			if (validCommand.equals(GHToolComandsParser.NUMBER_OF_RESULTS)) {
				try	{
					numberOfResults = Integer.parseInt(command[++i]);
				} catch (Exception nfe) {
					System.err.println("If option -n for number of results to return is specifide please specifie a number!");
					throw new Exception(nfe);
				}
				if (numberOfResults == 0) {
					System.err.println("If option -n for number of results to return is specifide please use number greater than 0!");
					throw new Exception();
				}
			}
			
			// if -language is specified it has to be followed by language as criteria for search
			if (validCommand.equals(GHToolComandsParser.LANGUAGE_SEARCH)) {
				try	{
					language = command[++i];
				} catch (Exception nfe) {
					System.err.println("If option -language is specifide please specifie a language you wish to conduct search for!");
					throw new Exception(nfe);
				}
				if (language.equals("")) {
					System.err.println("If option -language is specifide please provide language you wish to conduct search for (eg. -ghtool list -language java)!");
					throw new Exception();
				}
			}
			
			// check if user requested instructions or search
			if (validCommand.equals(GHToolComandsParser.DESC)) {
				if (++i < command.length) {
					repos = command[i];
					repos = prepareRepos(repos);
				}
			}
			
			// Sorting
			if (validCommand.equals(GHToolComandsParser.SORT_BY_DATE_UPDATED)) {
				if (++i < command.length) {
					sortOrder = command[i];
				}
				if (!sortOrder.equals("asc") && !sortOrder.equals("desc")) {
					System.err.println("Allowed sorting directions are asc or desc!");
					throw new Exception();
				}
			}
			
			setOptions(validCommand, numberOfResults, language, repos, sortOrder);
		}
	}
	
	public static void validateFirsLevelCommand(String line) throws Exception{
		if (!line.equals(GHTOOL) && !line.startsWith(GHTOOL + " " + LIST_REPOSITORIES) && !line.startsWith(GHTOOL + " " + DESC)) {
			throw new Exception();
		}
	}

	private static void validateOption(String command) throws Exception{
		if (command.equals(LIST_REPOSITORIES)) {
			if (currentCommandsMap.containsKey(DESC)) {
				throw new Exception();
			}
		}
		if (command.equals(DESC)) {
			if (currentCommandsMap.containsKey(LIST_REPOSITORIES)) {
				throw new Exception();
			}
		}
		if(!command.equals(GHTOOL) && !command.equals(LIST_REPOSITORIES) && !command.equals(NUMBER_OF_RESULTS) && !command.equals(LANGUAGE_SEARCH) && !command.equals(DESC) && !command.equals(SORT_BY_DATE_UPDATED)) {
			throw new Exception();
		}
	}
	
	/**
	 * @param repos
	 * @return
	 * @throws Exception
	 * 
	 * This method returns formated string for search by specified repositories
	 */
	private static String prepareRepos(String repos) throws Exception {
		String reposFormated = null;
		
		if (!repos.startsWith("[") || !repos.endsWith("]")) {
			throw new Exception();
		}
		
		reposFormated = "repo%3A" + repos.substring(1, repos.length()-1).replace(",", "+repo%3A").replace("/", "%2F");
		
		return reposFormated;
	}

	private static void setOptions(String command, int numberOfResults, String language, String repos, String sortOrder) {
		if (command.equals(GHTOOL) || command.equals(LIST_REPOSITORIES) || command.equals(DESC)) {
			if (command.equals(DESC)) {
				currentCommandsMap.put(command, repos);
			} else {
				currentCommandsMap.put(command, "");
			}
		}
		if (command.equals(NUMBER_OF_RESULTS)) {
			currentCommandsMap.put(command, numberOfResults+"");
		}
		if (command.equals(LANGUAGE_SEARCH)) {
			currentCommandsMap.put(command, language);
		}
		if (command.equals(SORT_BY_DATE_UPDATED)) {
			currentCommandsMap.put(command, sortOrder);
		}
	}
	
	/**
	 * INSTRUCTIONS FOR GHTOOL
	 */
	public static void showGHToolInstructions() {
		System.out.println(
				"============================================================"
				+" INSTRUCTIONS: "
    			+ "\n" + GHTOOL + " -> for user instructions"
    			+ "\n------------------------------------------------------------"
    			+ "\n" + GHTOOL + " " + LIST_REPOSITORIES + " -> for listing last N public repositories (default value for number (N) of repositories returned is 10)"
    			+ "\n\t" + GHTOOL + " " + LIST_REPOSITORIES + " " + NUMBER_OF_RESULTS + " <int> -> for listing last <int> public repositories"
    			+ "\n\t" + GHTOOL + " " + LIST_REPOSITORIES + " " + SORT_BY_DATE_UPDATED + " <direction> -> for sorting results by date they were updated (<direction> is asc or dsc, defoult is dsc)"
				+ "\n------------------------------------------------------------"
    			+ "\n" + GHTOOL + " " + LIST_REPOSITORIES + " " + LANGUAGE_SEARCH + "  <String> -> for listing last N public repositories for a specific language (default value for number (N) of repositories returned is 10)"
				+ "\n\t" + GHTOOL + " " + LIST_REPOSITORIES + " " + LANGUAGE_SEARCH + "  <String> " + NUMBER_OF_RESULTS + " <int> -> for listing last <int> public repositories for specific language <String>"
    			+ "\n------------------------------------------------------------"
				+ "\n" + GHTOOL + " " + DESC + " -> for user instructions on how to get description for repositories"
				+ "\n------------------------------------------------------------"
				+ "\nTo EXIT type in " + GHToolMain.EXIT
				+ "\n============================================================");
	}
	
	/**
	 * INSTRUCTIONS FOR GHTOOL DESC
	 */
	public static void showGHToolDescInstructions() {
		System.out.println(
				"============================================================"
				+" DESC INSTRUCTIONS: "
				+ "\nTo get description for repositories please specifie their full names in a list"
				+ "\nYou should specifie a list in [] brekets delimited by ,"
				+ "\n(eg. -ghtool desc [<fullName1>,<fullName2>,<fullName3>])"
				+ "\nNote: maximum number of repositoris to get description for is 10!"
				+ "\n============================================================"
				+ "\nTo EXIT type in " + GHToolMain.EXIT
				+ "\n============================================================");
	}

	public static Map<String, String> getCurrentCommandsMap() {
		return currentCommandsMap;
	}
}
