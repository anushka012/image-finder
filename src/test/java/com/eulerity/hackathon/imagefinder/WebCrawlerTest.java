package com.eulerity.hackathon.imagefinder;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public class WebCrawlerTest {

    private WebCrawler crawler;

    @Before
    public void setUp() {
        crawler = new WebCrawler("http://example.com");
    }

    @Test
    public void testStartCrawling_ReturnsList() {
        List<String> images = crawler.startCrawling();
        Assert.assertNotNull(images);
    }

    @Test
    public void testIsValidSubPage() throws Exception {
        Method method = WebCrawler.class.getDeclaredMethod("isValidSubPage", String.class);
        method.setAccessible(true);

        boolean valid = (boolean) method.invoke(crawler, "http://example.com/subpage");
        Assert.assertTrue(valid);

        boolean invalidDueToQuery = (boolean) method.invoke(crawler, "http://example.com/subpage?ref=123");
        Assert.assertFalse(invalidDueToQuery);

        boolean invalidDifferentDomain = (boolean) method.invoke(crawler, "http://otherdomain.com/subpage");
        Assert.assertFalse(invalidDifferentDomain);
    }

    @Test
    public void testCrawlPage_DuplicateNotVisited() throws Exception {
        Method crawlPageMethod = WebCrawler.class.getDeclaredMethod("crawlPage", String.class);
        crawlPageMethod.setAccessible(true);

        Field visitedField = WebCrawler.class.getDeclaredField("visitedUrls");
        visitedField.setAccessible(true);
        @SuppressWarnings("unchecked")
        Set<String> visitedUrls = (Set<String>) visitedField.get(crawler);

        crawlPageMethod.invoke(crawler, "http://example.com/page");
        int sizeAfterFirstCall = visitedUrls.size();

        crawlPageMethod.invoke(crawler, "http://example.com/page");
        int sizeAfterSecondCall = visitedUrls.size();

        Assert.assertEquals(sizeAfterFirstCall, sizeAfterSecondCall);
    }

    @Test
    public void testStartCrawling_InvalidUrl_ReturnsEmptyList() {
        WebCrawler crawlerInvalid = new WebCrawler("http://example.com/pagegit ");
        List<String> images = crawlerInvalid.startCrawling();
        Assert.assertNotNull(images);
    }


}
