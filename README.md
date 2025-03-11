# 📸 Image Finder App

## 🏆 Eulerity Hackathon Challenge Submission

### 👩‍💻 Author: Anushka Sarath

## 🚀 Project Overview
The **Image Finder App** is a Java-based web application designed to crawl a given URL, extract images, and display them in a paginated format. The app also provides an option to download all found images as a ZIP file.

## ✨ Features
- 🕸 **Web Crawler**: Finds all images on the web page(s) that it crawls.
- 🔗 **Sub-page Crawling**: Crawls sub-pages to discover more images within the same domain.
- ⚡ **Multi-threading Support**: Enables concurrent crawling of multiple sub-pages.
- 🌐 **Domain Restriction**: Ensures that the crawl stays within the same domain as the input URL.
- 🔄 **Duplicate Avoidance**: Prevents re-crawling pages that have already been visited.
- 📄 **Pagination Support**: Images are displayed in a paginated format (10 images per page).
- 💾 **Session Storage**: Stores crawled images in the session for easy access.
- 📥 **Download ZIP Functionality**: Allows users to download all extracted images as a ZIP file.
- 🌙 **Dark Mode Toggle**: Users can switch between light and dark mode.
- 🕵️‍♂️ **Recent Search History**: Saves and displays recent URLs searched by the user.
- 🐳 **Docker Support**: Easily deploy the application using the provided Dockerfile.
- 🧪 **Unit Testing**: Includes JUnit and Mockito-based test cases to ensure reliability.

---

## 🛠 Installation and Setup

### **📌 Prerequisites**
Ensure you have the following installed:
- ☕ Java 8 (exact version, NOT Java 9+)
- 🛠 Maven 3.5 or higher
- 🌐 Apache Tomcat (if running manually)
- 🐳 Docker (optional, for containerized deployment)

### **💻 Running the Project Locally**
1. Clone the repository:
   ```sh
   git clone https://github.com/your-repo/imagefinder.git
   cd imagefinder
   ```
2. Build the project using Maven:
   ```sh
   mvn package
   ```
3. Run the project using Jetty:
   ```sh
   mvn clean test package jetty:run
   ```
4. Open the browser and go to:
   ```
   http://localhost:8080
   ```

### **🐳 Docker Deployment**
To build and run the project using Docker:
1. Build the Docker image:
   ```sh
   docker build -t imagefinder .
   ```
2. Run the container:
   ```sh
   docker run -p 8080:8080 imagefinder
   ```
3. Access the application at:
   ```
   http://localhost:8080
   ```

---

## 🎯 Usage
1. **Enter a URL** in the input field and click **Submit**.
2. The web crawler will fetch images and display them in a paginated format.
3. **Navigate through pages** using the ⬅️ **Previous** and ➡️ **Next** buttons.
4. Click 📥 **Download ZIP** to save all images as a ZIP file.
5. Use the 🌙 **Dark Mode Toggle** for a better viewing experience.
6. The 📌 **Recent Searches Dropdown** allows easy access to previously searched URLs.

---

## 🏗 Project Structure
```
imagefinder/
├── src/
│   ├── main/
│   │   ├── java/com.eulerity.hackathon.imagefinder/
│   │   │   ├── DownloadServlet.java
│   │   │   ├── ImageFinder.java
│   │   │   ├── WebCrawler.java
│   │   ├── webapp/
│   │       ├── index.html
│   │       ├── script.js
│   │       ├── style.css
│   │       ├── WEB-INF/web.xml
│   ├── test/java/com.eulerity.hackathon.imagefinder/
│       ├── ImageFinderTest.java
│       ├── WebCrawlerTest.java
├── pom.xml (Maven Configuration)
├── Dockerfile (For Containerization)
├── test-links.txt (Test URLs)
└── README.md (Project Documentation)
```

---

## 🌐 API Endpoints
### **1. 🔍 Image Crawling (POST)**
```
Endpoint: /main
Method: POST
Parameters:
- url (String): The website URL to crawl.
- page (int): Page number for pagination.
```
**Example Response:**
```json
{
    "images": ["https://example.com/image1.jpg", "https://example.com/image2.png"],
    "totalPages": 3,
    "currentPage": 0
}
```

### **2. 📥 Download Images as ZIP (GET)**
```
Endpoint: /download-zip
Method: GET
Response: ZIP file containing all images
```

---

## 🧪 Testing
Unit tests are written using **JUnit** and **Mockito**.
To run tests:
```sh
mvn test
```

---

## 🚀 Known Issues & Future Enhancements
- ⚠️ **Improved Error Handling**: More informative messages for invalid URLs.
- 🔄 **Performance Optimization**: Reduce duplicate URL visits.
- 🖼 **Advanced Image Recognition**: Detect and categorize images (logos, icons, etc.).

---

## 📩 Contact
For any queries, reach out at 📧 **anushkasarath01@gmail.com**.
