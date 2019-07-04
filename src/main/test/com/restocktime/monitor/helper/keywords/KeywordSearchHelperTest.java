package com.restocktime.monitor.helper.keywords;

import com.restocktime.monitor.helper.keywords.transformer.StringToSearchTransformer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest(StringToSearchTransformer.class)
public class KeywordSearchHelperTest {
    private List<Map<String, Boolean>> keywords;
    KeywordSearchHelper keywordSearchHelper;

    @Before
    public void setup(){
        Map<String, Boolean> keywordMap = new HashMap<>();
        keywordMap.put("converse", true);
        keywordMap.put("kith", true);
        keywordMap.put("nkaa3831", true);

        keywordMap.put("star", false);
        keywords = new ArrayList<>();
        keywords.add(keywordMap);
        PowerMockito.mockStatic(StringToSearchTransformer.class);
        PowerMockito.when(StringToSearchTransformer.stringToKeywordModel("input")).thenReturn(keywords);
        keywordSearchHelper = new KeywordSearchHelper("input");
    }

    @Test
    public void testPositiveMatch(){
        assertTrue(keywordSearchHelper.search("kith"));
    }

    @Test
    public void testSanitizeSpaces(){
        assertTrue(keywordSearchHelper.search("\n \tkith\t\n converse\t"));
    }

    @Test
    public void testSanitizeCaps(){
        assertTrue(keywordSearchHelper.search("KiTh CoNVeRSE"));
    }

    @Test
    public void testEmptyInput(){
        assertFalse(keywordSearchHelper.search(""));
    }

    @Test
    public void testNoneFound(){
        assertFalse(keywordSearchHelper.search("hello"));
    }

    @Test
    public void testNegativeKeyword(){
        assertFalse(keywordSearchHelper.search("kith all star"));
    }

    @Test
    public void testPositiveMatchRandomChars(){
        assertTrue(keywordSearchHelper.search("<p>nkaa3831-001</p>"));
    }
}
