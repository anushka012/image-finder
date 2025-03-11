package com.eulerity.hackathon.imagefinder;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.Map;


@WebServlet(name = "ImageFinder", urlPatterns = {"/main"})
public class ImageFinder extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected static final Gson GSON = new GsonBuilder().create();
	private static final int IMAGES_PER_PAGE = 10;

	@Override
	protected final void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		resp.setContentType("application/json");

		String url = req.getParameter("url");
		String pageStr = req.getParameter("page");

		// Validate URL
		if (url == null || url.isEmpty()) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", "No URL provided.");
			resp.getWriter().print(GSON.toJson(errorResponse));
			return;
		}

		// Validate Page Number
		int page = 0;
		try {
			page = Integer.parseInt(pageStr);
			if (page < 0) throw new NumberFormatException(); // Prevent negative pages
		} catch (NumberFormatException e) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", "Invalid page number.");
			resp.getWriter().print(GSON.toJson(errorResponse));
			return;
		}

		System.out.println("Crawling URL: " + url);
		WebCrawler crawler = new WebCrawler(url);
		List<String> imageUrls = crawler.startCrawling();

		if (imageUrls.isEmpty()) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", "No images found on this page.");
			resp.getWriter().print(GSON.toJson(errorResponse));
			return;
		}

		// Store crawled images in session
		req.getSession().setAttribute("crawledImages", imageUrls);

		// Pagination Logic
		int totalImages = imageUrls.size();
		int totalPages = (int) Math.ceil((double) totalImages / IMAGES_PER_PAGE);
		int start = page * IMAGES_PER_PAGE;
		int end = Math.min(start + IMAGES_PER_PAGE, totalImages);

		if (start >= totalImages) {
			Map<String, String> errorResponse = new HashMap<>();
			errorResponse.put("error", "No more images available.");
			resp.getWriter().print(GSON.toJson(errorResponse));
			return;
		}

		List<String> paginatedImages = imageUrls.subList(start, end);

		Map<String, Object> responseData = new HashMap<>();
		responseData.put("images", paginatedImages);
		responseData.put("totalPages", totalPages);
		responseData.put("currentPage", page);

		resp.getWriter().print(GSON.toJson(responseData));
		System.out.println("Sending Paginated Images: Page " + page);
	}

}
