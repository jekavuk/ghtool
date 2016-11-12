package ghtool.test.processor;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import ghtool.util.GHToolComandsParser;

public class GHToolJUTestCommandParser {
	
	/**
	 * Test if map is parsed correctly
	 */
	@Test
	public void testCommandParserMap() {
		
		// ============================== TEST MAP 1
		String inLine1 = "-ghtool";
		
		Map<String, String> mapMock1 = new HashMap<String, String>();
		mapMock1.put("-ghtool", "");
		
		try {
			GHToolComandsParser.parseOptions(inLine1);
		} catch (Exception e) {
			//e.printStackTrace();
			System.err.println("[ERROR] JUTest - GHToolJUTestCommandParser.testCommandParserMap -> map1");
		}
		
		Assert.assertEquals(mapMock1, GHToolComandsParser.getCurrentCommandsMap());
		
		// ============================== TEST MAP 2
		
		String inLine2 = "-ghtool list -n 546";
		
		Map<String, String> mapMock2 = new HashMap<String, String>();
		mapMock2.put("-ghtool", "");
		mapMock2.put("list", "");
		mapMock2.put("-n", "546");
		
		try {
			GHToolComandsParser.parseOptions(inLine2);
		} catch (Exception e) {
			//e.printStackTrace();
			System.err.println("[ERROR] JUTest - GHToolJUTestCommandParser.testCommandParserMap -> map2");
		}
		
		Assert.assertEquals(mapMock2, GHToolComandsParser.getCurrentCommandsMap());
		// ============================== TEST MAP 3
	}

}
