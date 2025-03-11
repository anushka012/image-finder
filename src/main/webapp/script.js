let currentPage = 0;
let totalPages = 1;

document.querySelector("#submit-btn").addEventListener("click", function(event) {
    event.preventDefault();
    currentPage = 0;
    fetchImages(currentPage);
});

document.querySelector("#next-page").addEventListener("click", function() {
    if (currentPage < totalPages - 1) {
        currentPage++;
        fetchImages(currentPage);
        updatePaginationUI();
    }
});

document.querySelector("#prev-page").addEventListener("click", function() {
    if (currentPage > 0) {
        currentPage--;
        fetchImages(currentPage);
        updatePaginationUI();
    }
});

function fetchImages(page) {
    let urlInput = document.querySelector("#url-input").value.trim();
    let resultList = document.querySelector(".results");
    let loadingText = document.querySelector("#loading-spinner");
    let downloadBtn = document.querySelector("#download-btn");

    if (!urlInput) {
        alert("Please enter a valid URL!");
        return;
    }

    loadingText.style.display = "block";
    resultList.innerHTML = '';
    downloadBtn.style.display = "none";

    fetch(`/main?url=${encodeURIComponent(urlInput)}&page=${page}`, { method: 'POST' })
        .then(response => response.json())
        .then(data => {
            loadingText.style.display = "none";

            if (data.error) {
                alert(data.error);
                return;
            }

            if (!data.images || data.images.length === 0) {
                alert("No more images available.");
                if (currentPage > 0) {
                    currentPage--;
                }
                return;
            }

            if (data.totalPages !== undefined) {
                totalPages = data.totalPages;
            }

            resultList.innerHTML = '';
            data.images.forEach(imgUrl => {
                let img = document.createElement("img");
                img.src = imgUrl;
                img.alt = "Image Result";
                resultList.appendChild(img);
            });

            if (data.images.length > 0) {
                downloadBtn.style.display = "block";
            }

            updatePaginationUI();
        })
        .catch(error => {
            loadingText.style.display = "none";
            console.error("Error fetching images:", error);
            alert("Unexpected error fetching images. Please check console for details.");
        });
}

function updatePaginationUI() {
    const currentPageElement = document.querySelector("#page-number");

    if (currentPageElement) {
        currentPageElement.textContent = `Page ${currentPage + 1} of ${totalPages}`;
    } else {
        console.log("ERROR: `#page-number` element NOT FOUND!");
    }

    let prevButton = document.querySelector("#prev-page");
    let nextButton = document.querySelector("#next-page");

    if (prevButton && nextButton) {
        prevButton.disabled = currentPage === 0;
        nextButton.disabled = currentPage >= totalPages - 1;
        console.log("Updated button states:", {
            prevDisabled: prevButton.disabled,
            nextDisabled: nextButton.disabled
        });
    } else {
        console.log("ERROR: Pagination buttons not found!");
    }

}

document.querySelector("#download-btn").addEventListener("click", function() {
    fetch('/download-zip', { method: 'GET' })
        .then(response => {
            if (response.ok) {
                return response.blob();
            }
            throw new Error("Failed to generate ZIP file");
        })
        .then(blob => {
            let link = document.createElement("a");
            link.href = window.URL.createObjectURL(blob);
            link.download = "images.zip";
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
        })
        .catch(error => {
            alert("Error downloading ZIP file!");
            console.error(error);
        });
});

document.addEventListener("DOMContentLoaded", function () {
    const submitBtn = document.querySelector("#submit-btn");
    const urlInput = document.querySelector("#url-input");
    const historyDropdown = document.querySelector("#history-dropdown");

    function loadSearchHistory() {
        const searches = JSON.parse(localStorage.getItem("searchHistory")) || [];
        historyDropdown.innerHTML = '<option value="">Recent Searches</option>';
        searches.forEach(url => {
            let option = document.createElement("option");
            option.value = url;
            option.textContent = url;
            historyDropdown.appendChild(option);
        });
    }

    function saveSearch(url) {
        let searches = JSON.parse(localStorage.getItem("searchHistory")) || [];
        if (!searches.includes(url)) {
            searches.unshift(url);
            if (searches.length > 5) searches.pop();
            localStorage.setItem("searchHistory", JSON.stringify(searches));
        }
    }

    historyDropdown.addEventListener("change", function () {
        if (this.value) {
            urlInput.value = this.value;
        }
    });

    submitBtn.addEventListener("click", function () {
        const url = urlInput.value.trim();
        if (url) {
            saveSearch(url);
            loadSearchHistory();
        }
    });

    loadSearchHistory();
});

document.addEventListener("DOMContentLoaded", function () {
    const darkModeToggle = document.getElementById("dark-mode-toggle");
    const body = document.body;

    if (localStorage.getItem("dark-mode") === "enabled") {
        body.classList.add("dark-mode");
        darkModeToggle.textContent = "☀️ Light Mode";
    }

    darkModeToggle.addEventListener("click", function () {
        body.classList.toggle("dark-mode");

        if (body.classList.contains("dark-mode")) {
            localStorage.setItem("dark-mode", "enabled");
            darkModeToggle.textContent = "☀️ Light Mode";
        } else {
            localStorage.setItem("dark-mode", "disabled");
            darkModeToggle.textContent = "🌙 Dark Mode";
        }
    });
});