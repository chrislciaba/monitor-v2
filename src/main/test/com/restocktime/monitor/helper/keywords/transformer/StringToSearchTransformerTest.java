package com.restocktime.monitor.helper.keywords.transformer;

import org.junit.Test;

import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class StringToSearchTransformerTest {

    @Test
    public void testSingleInputCorrect(){
        String keywordString = "+kith,-thing";

        List<Map<String, Boolean>> result = StringToSearchTransformer.stringToKeywordModel(keywordString);
        assertEquals(1, result.size());
        Map<String, Boolean> singleMap = result.get(0);
        assertTrue(singleMap.containsKey("kith"));
        assertTrue(singleMap.get("kith"));
        assertTrue(singleMap.containsKey("thing"));
        assertFalse(singleMap.get("thing"));
    }

    @Test
    public void testMultipleInputCorrect(){
        String keywordString = "+kith,-thing;+yeezy,-700;+john,-cena";

        List<Map<String, Boolean>> result = StringToSearchTransformer.stringToKeywordModel(keywordString);
        assertEquals(3, result.size());
        Map<String, Boolean> singleMap = result.get(0);
        assertTrue(singleMap.containsKey("kith"));
        assertTrue(singleMap.get("kith"));
        assertTrue(singleMap.containsKey("thing"));
        assertFalse(singleMap.get("thing"));

        singleMap = result.get(1);
        assertTrue(singleMap.containsKey("yeezy"));
        assertTrue(singleMap.get("yeezy"));
        assertTrue(singleMap.containsKey("700"));
        assertFalse(singleMap.get("700"));

        singleMap = result.get(2);
        assertTrue(singleMap.containsKey("john"));
        assertTrue(singleMap.get("john"));
        assertTrue(singleMap.containsKey("cena"));
        assertFalse(singleMap.get("cena"));
    }
}
