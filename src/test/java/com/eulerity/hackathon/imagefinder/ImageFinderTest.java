package com.eulerity.hackathon.imagefinder;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.io.StringWriter;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImageFinderTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private StringWriter responseWriter;
    private ImageFinder servlet;

    @Before
    public void setUp() throws Exception {
        responseWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(responseWriter);
        when(response.getWriter()).thenReturn(writer);
        servlet = new ImageFinder();
    }

    @Test
    public void testDoPost_NoUrlProvided() throws Exception {
        when(request.getParameter("url")).thenReturn(null);
        when(request.getParameter("page")).thenReturn("0");

        servlet.doPost(request, response);
        String result = responseWriter.toString();

        assertTrue(result.contains("\"error\":\"No URL provided.\""));
    }
}