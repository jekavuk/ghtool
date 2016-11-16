package ghtool.processor;

import java.util.Map;

import ghtool.util.GHToolComandsParser;

public class GHToolProcessorHelper {
	
	private static final String HOST					= "api.github.com";
	private static final String SEARCH_REPOSITORIES		= "/search/repositories";
	private static final String TYPE_REPOSITORIES		= "type=Repositories";
	private static final String DEFAULT_SORT			= "sort=updated";
	private static final String DEFAULT_SORT_ORDER		= "desc";
	private static final String LANGUAGE 				= "language";
	
	private static final String PER_PAGE				= "per_page";
	private static int DEFAULT_NUMBER_OF_RESULTS		= 10;
	
	private static final String PAGE					= "page";
	
	private static int pageNum = 1;
	private static int perPage = 100;
	
	public static String buildURL() {
		
		Map<String, String> currentCommandsMap = GHToolComandsParser.getCurrentCommandsMap();
		
		StringBuilder url = new StringBuilder("https://");
		url.append(HOST);
		url.append(SEARCH_REPOSITORIES);
		url.append("?");
		if (currentCommandsMap.containsKey(GHToolComandsParser.NUMBER_OF_RESULTS)) {
			int limit = Integer.parseInt(currentCommandsMap.get(GHToolComandsParser.NUMBER_OF_RESULTS));
			if (limit < 100) {
				url.append(PER_PAGE + "=" + limit);
			} else {
				url.append(PER_PAGE + "=" + perPage);
			}
		} else {
			url.append(PER_PAGE + "=" + DEFAULT_NUMBER_OF_RESULTS);
		}
		url.append("&" + PAGE + "=" + pageNum);
		url.append("&q=created%3A>1970-01-01");
		if(currentCommandsMap.containsKey(GHToolComandsParser.DESC)) {
			url.append("+" + currentCommandsMap.get(GHToolComandsParser.DESC));
		}
		if (currentCommandsMap.containsKey(GHToolComandsParser.LANGUAGE_SEARCH)) {
			url.append("+" + LANGUAGE + "%3A" + currentCommandsMap.get(GHToolComandsParser.LANGUAGE_SEARCH));
		}
		url.append("&" + TYPE_REPOSITORIES);
		url.append("&" + DEFAULT_SORT);
		if(currentCommandsMap.containsKey(GHToolComandsParser.SORT_BY_DATE_UPDATED)) {
			if (!currentCommandsMap.get(GHToolComandsParser.SORT_BY_DATE_UPDATED).isEmpty()) {
				url.append("&order=" + currentCommandsMap.get(GHToolComandsParser.SORT_BY_DATE_UPDATED));
			} else {
				url.append("&order=" + DEFAULT_SORT_ORDER);
			}
		}
		
		return url.toString();
		//return "https://api.github.com/search/repositories?per_page=25&q=language%3Ajava&type=Repositories";
	}

	public static int getPageNum() {
		return pageNum;
	}

	public static void setPageNum(int pageNum) {
		GHToolProcessorHelper.pageNum = pageNum;
	}

	public static int getPerPage() {
		return perPage;
	}

	public static void setPerPage(int perPage) {
		GHToolProcessorHelper.perPage = perPage;
	}

}