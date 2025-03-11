package com.eulerity.hackathon.imagefinder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Set;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.atomic.AtomicInteger;

public class WebCrawler {
    private final String baseUrl;
    private final Set<String> visitedUrls = Collections.synchronizedSet(new HashSet<>());
    private final Set<String> imageUrls = Collections.synchronizedSet(new HashSet<>());
    private final ExecutorService executorService = Executors.newFixedThreadPool(15);
    private final CompletionService<Void> completionService = new ExecutorCompletionService<>(executorService);
    private final AtomicInteger activeTasks = new AtomicInteger(0);


    public WebCrawler(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public List<String> startCrawling() {
        crawlPage(baseUrl);

        // Wait until all crawling tasks finish before shutting down
        while (activeTasks.get() > 0) {
            try {
                completionService.take(); // Wait for tasks to complete
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        executorService.shutdown();

        return new ArrayList<>(imageUrls);
    }

    private void crawlPage(String url) {
        final String finalUrl = normalizeUrl(url); // Use helper method to clean URL

        if (visitedUrls.contains(finalUrl)) return; // Prevent duplicate visits
        visitedUrls.add(finalUrl);
        activeTasks.incrementAndGet();

        completionService.submit(() -> {
            try {
                System.out.println("Crawling: " + finalUrl);
                Document doc = Jsoup.connect(finalUrl)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/110.0.0.0 Safari/537.36")
                        .referrer("http://www.google.com")
                        .timeout(15000)
                        .ignoreHttpErrors(true)
                        .followRedirects(true)
                        .get();

                // Keep Existing Image Extraction Logic (No Changes)
                Elements images = doc.select("img[src], img[data-pin-media]");
                for (Element img : images) {
                    String imgUrl = cleanImageUrl(img.hasAttr("data-pin-media") ? img.absUrl("data-pin-media") : img.absUrl("src"));

                    if (!imgUrl.isEmpty() && isValidImage(imgUrl)) {
                        imageUrls.add(imgUrl);
                        System.out.println("Extracted Image: " + imgUrl);
                    }


                }

                // Extract Links & Crawl Valid Pages
                Elements links = doc.select("a[href]");
                for (Element link : links) {
                    String newUrl = normalizeUrl(link.absUrl("href"));

                    // Crawl Main Page Links
                    if (isValidLink(newUrl)) {
                        crawlPage(newUrl);
                    }

                    // Crawl Sub-Pages (New Logic Added)
                    if (isValidSubPage(newUrl)) {
                        completionService.submit(() -> {
                            crawlPage(newUrl);
                            return null;
                        });
                    }
                }

            } catch (Exception e) {
                System.err.println("Failed to crawl " + finalUrl + ": " + e.getMessage());
            } finally {
                activeTasks.decrementAndGet();
            }
            return null;
        });
    }

    private String cleanImageUrl(String url) {
        return url.split("\\?")[0]; //
    }


    private String normalizeUrl(String url) {
        url = url.split("#")[0];
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }


    private boolean isValidLink(String url) {
        return url.startsWith(baseUrl) &&
                !visitedUrls.contains(url) &&
                !url.contains("#") &&
                !url.contains("comment-page") &&
                !url.contains("replytocom") &&
                !url.contains("?ref=") &&
                !url.matches(".*\\?.*");
    }

    private boolean isValidSubPage(String url) {
        return url.startsWith(baseUrl) &&
                !visitedUrls.contains(url) &&
                !url.contains("#") &&
                !url.contains("?ref=") &&
                !url.contains("comment-page") &&
                !url.contains("replytocom");
    }

    private boolean isValidImage(String url) {
        return url.matches(".*(\\.jpg|\\.jpeg|\\.png|\\.gif|\\.bmp|\\.svg)$");
    }

}