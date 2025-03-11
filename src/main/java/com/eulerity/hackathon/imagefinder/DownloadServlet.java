package com.eulerity.hackathon.imagefinder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/download-zip")
public class DownloadServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<String> imageUrls = (List<String>) req.getSession().getAttribute("crawledImages");

        if (imageUrls == null || imageUrls.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No images found to download.");
            return;
        }

        resp.setContentType("application/zip");
        resp.setHeader("Content-Disposition", "attachment; filename=images.zip");

        try (ZipOutputStream zipOut = new ZipOutputStream(resp.getOutputStream())) {
            for (String imageUrl : imageUrls) {
                try (InputStream in = new URL(imageUrl).openStream()) {
                    ZipEntry entry = new ZipEntry(imageUrl.substring(imageUrl.lastIndexOf("/") + 1));
                    zipOut.putNextEntry(entry);
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) > 0) {
                        zipOut.write(buffer, 0, len);
                    }
                    zipOut.closeEntry();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
