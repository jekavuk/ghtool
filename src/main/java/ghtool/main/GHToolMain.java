package ghtool.main;

import java.util.Scanner;

import ghtool.processor.GHToolProcessor;
import ghtool.processor.GHToolProcessorHelper;
import ghtool.util.GHToolComandsParser;
import ghtool.util.GHToolResultsBuilder;

/**
 * @author Jelena
 * 
 * This class is used as starting point
 * (run this class to use this tool) 
 *
 */
public class GHToolMain {
	
	private static Scanner scan;
	public static final String EXIT = "bye"; 

	public static void main(String[] args) {
		
		System.out.println("/***** WELCOME TO GHTOOL*****/" + "\nFor infomation on how to use this tool please issue command -ghtool");
		
		scan = new Scanner(System.in);
		
		boolean exit = false;
        while(!exit) {
	    	try {
	    		String line = scan.nextLine();
	    		
	    		// "Close" the tool
	    		if (line.equals(EXIT)) {
	    			exit = true;
	    			System.out.println("Good bye! :)");
	    			break;
	    		}
	    		
	    		if (line != null && line != "") {
	    			
	    			// Pars options into tool usable
		    		GHToolComandsParser.parseOptions(line);
		    		
		    		// GitHub allows only 100 results per load check if user requested more than that
		    		int numberOfResultsRequestedOverLimit = 0;
		    		if (GHToolComandsParser.getCurrentCommandsMap().containsKey(GHToolComandsParser.NUMBER_OF_RESULTS)) {
		    			numberOfResultsRequestedOverLimit = Integer.parseInt(GHToolComandsParser.getCurrentCommandsMap().get(GHToolComandsParser.NUMBER_OF_RESULTS));
		    		}
		    		
		    		// if more than 100 results requested prepare pagination
		    		int pages = 0;
		    		int mod = 0; // if 145 results requested mod is 45 -> get only 45 results from next page
		    		if (numberOfResultsRequestedOverLimit > 100) {
		    			pages = numberOfResultsRequestedOverLimit/100;
		    			mod = numberOfResultsRequestedOverLimit%100;
		    		}
		    		
		    		// if less than 100 results requested
		    		if (pages == 0) {
		    			String response = GHToolProcessor.processCommand();
		    			if (!response.isEmpty()) {
		    				if (GHToolComandsParser.getCurrentCommandsMap().containsKey(GHToolComandsParser.DESC)) {
		    					System.out.println(GHToolResultsBuilder.displayRepoDescription(response));
		    				} else {
		    					System.out.println(GHToolResultsBuilder.displayBasic(response));
		    				}
		    			}
		    		}
		    		
		    		// If more than 100 results requested read all pages
		    		if (pages > 0) {
		    			for (int i = 1; i <= pages; i++) {    				
		    				GHToolProcessorHelper.setPageNum(i);
		    				String response = GHToolProcessor.processCommand();
				    		System.out.println(GHToolResultsBuilder.displayBasic(response));
						}
		    		}
		    		if (mod > 0) {
	    				GHToolProcessorHelper.setPageNum(pages+1);
	    				GHToolProcessorHelper.setPerPage(mod);
	    				String response = GHToolProcessor.processCommand();
			    		System.out.println(GHToolResultsBuilder.displayBasic(response));
		    		}
		    		
		    		// reset count of results
		    		// when more than 100 result requested after each 100s load to continue from previous page count
		    		GHToolResultsBuilder.count = 0;
	    		}
	    		
			} catch (Exception e) {
				//e.printStackTrace();
				System.err.println("Command you entered was not valid! For more information please enter -ghtool");
			}
        }
	}
}
