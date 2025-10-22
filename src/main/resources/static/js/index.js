// Use relative URLs since we're serving from the same origin
const baseUrl = window.location.origin;

console.log("🌐 API Base URL:", baseUrl);

// DOM Elements
let urlInput,
  expiryInput,
  shortenBtn,
  resultSection,
  shortUrlResult,
  errorMessage;
let analyticsInput, analyticsResult, analyticsContent, analyticsError;

// Initialize when DOM is loaded
document.addEventListener("DOMContentLoaded", function () {
  initializeElements();
  setupEventListeners();
  checkApiConnection();
});

function initializeElements() {
  urlInput = document.getElementById("urlInput");
  expiryInput = document.getElementById("expiryInput");
  shortenBtn = document.querySelector(".shorten-btn");
  resultSection = document.getElementById("resultSection");
  shortUrlResult = document.getElementById("shortUrlResult");
  errorMessage = document.getElementById("errorMessage");

  analyticsInput = document.getElementById("analyticsInput");
  analyticsResult = document.getElementById("analyticsResult");
  analyticsContent = document.getElementById("analyticsContent");
  analyticsError = document.getElementById("analyticsError");
}

function setupEventListeners() {
  // Enter key support for URL input
  urlInput.addEventListener("keypress", function (e) {
    if (e.key === "Enter") {
      shortenUrl();
    }
  });

  // Enter key support for analytics input
  analyticsInput.addEventListener("keypress", function (e) {
    if (e.key === "Enter") {
      fetchAnalytics();
    }
  });

  // Smooth scrolling for navigation
  document.querySelectorAll(".nav-link").forEach((link) => {
    link.addEventListener("click", function (e) {
      e.preventDefault();
      const targetId = this.getAttribute("href").substring(1);
      const targetElement = document.getElementById(targetId);
      if (targetElement) {
        targetElement.scrollIntoView({ behavior: "smooth" });

        // Update active nav link
        document
          .querySelectorAll(".nav-link")
          .forEach((l) => l.classList.remove("active"));
        this.classList.add("active");
      }
    });
  });
}

async function checkApiConnection() {
  try {
    const response = await fetch(`${baseUrl}/actuator/health`, {
      method: "GET",
      mode: "cors",
    });

    if (response.ok) {
      console.log("✅ API connection successful");
    } else {
      throw new Error(`HTTP ${response.status}`);
    }
  } catch (error) {
    console.warn("⚠️ API connection failed:", error);
    showError(
      "Warning: Cannot connect to API server. Please ensure the backend is running."
    );
  }
}

function toggleAdvanced() {
  const panel = document.getElementById("advancedPanel");
  const isVisible = panel.style.display !== "none";
  panel.style.display = isVisible ? "none" : "block";

  // Animate the toggle
  if (!isVisible) {
    panel.style.opacity = "0";
    panel.style.transform = "translateY(-10px)";
    setTimeout(() => {
      panel.style.opacity = "1";
      panel.style.transform = "translateY(0)";
    }, 10);
  }
}

async function shortenUrl() {
  const url = urlInput.value.trim();
  const expiry = expiryInput.value;

  // Hide previous results and errors
  hideError();
  hideResult();

  // Validation
  if (!url) {
    showError("Please enter a URL to shorten");
    return;
  }

  if (!isValidUrl(url)) {
    showError("Please enter a valid URL (must start with http:// or https://)");
    return;
  }

  // Show loading state
  showLoading();

  try {
    const requestBody = {
      url: url,
      expiryDate: expiry || null,
    };

    const response = await fetch(`${baseUrl}/api/url`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      mode: "cors",
      body: JSON.stringify(requestBody),
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.errorMessage || `HTTP ${response.status}`);
    }

    const data = await response.json();
    showResult(data);
  } catch (error) {
    console.error("Error shortening URL:", error);
    showError(error.message || "Failed to shorten URL. Please try again.");
  } finally {
    hideLoading();
  }
}

function showLoading() {
  shortenBtn.disabled = true;
  shortenBtn.querySelector(".btn-text").style.display = "none";
  shortenBtn.querySelector(".btn-loading").style.display = "flex";
}

function hideLoading() {
  shortenBtn.disabled = false;
  shortenBtn.querySelector(".btn-text").style.display = "inline";
  shortenBtn.querySelector(".btn-loading").style.display = "none";
}

function showResult(data) {
  const shortUrl = `${baseUrl}/s/${data.shortUrlCode}`;
  shortUrlResult.value = shortUrl;
  resultSection.style.display = "block";

  // Scroll to result
  resultSection.scrollIntoView({ behavior: "smooth", block: "nearest" });

  // Store data for analytics
  window.currentUrlData = data;
}

function hideResult() {
  resultSection.style.display = "none";
}

function showError(message) {
  errorMessage.textContent = message;
  errorMessage.style.display = "block";
}

function hideError() {
  errorMessage.style.display = "none";
}

function isValidUrl(string) {
  try {
    new URL(string);
    return string.startsWith("http://") || string.startsWith("https://");
  } catch (_) {
    return false;
  }
}

async function copyToClipboard() {
  const copyBtn = document.querySelector(".copy-btn");
  const copyText = copyBtn.querySelector(".copy-text");
  const copyIcon = copyBtn.querySelector(".copy-icon");

  try {
    await navigator.clipboard.writeText(shortUrlResult.value);

    // Visual feedback
    copyBtn.classList.add("copied");
    copyText.textContent = "Copied!";
    copyIcon.textContent = "✅";

    setTimeout(() => {
      copyBtn.classList.remove("copied");
      copyText.textContent = "Copy";
      copyIcon.textContent = "📋";
    }, 2000);
  } catch (err) {
    // Fallback for older browsers
    shortUrlResult.select();
    document.execCommand("copy");

    copyText.textContent = "Copied!";
    setTimeout(() => {
      copyText.textContent = "Copy";
    }, 2000);
  }
}

function testUrl() {
  if (shortUrlResult.value) {
    window.open(shortUrlResult.value, "_blank");
  }
}

function getAnalytics() {
  if (window.currentUrlData) {
    // Scroll to analytics section and populate
    document.getElementById("analytics").scrollIntoView({ behavior: "smooth" });
    analyticsInput.value = shortUrlResult.value;
    setTimeout(() => fetchAnalytics(), 500);
  }
}

function resetForm() {
  urlInput.value = "";
  expiryInput.value = "";
  hideResult();
  hideError();
  urlInput.focus();
}

async function fetchAnalytics() {
  const shortUrl = analyticsInput.value.trim();

  hideAnalyticsError();
  hideAnalyticsResult();

  if (!shortUrl) {
    showAnalyticsError("Please enter a short URL");
    return;
  }

  // Extract short code from URL
  const shortCode = extractShortCode(shortUrl);
  console.log("🔍 Analytics Debug:", {
    inputUrl: shortUrl,
    extractedCode: shortCode,
    apiUrl: `${baseUrl}/api/details/${shortCode}`,
  });

  if (!shortCode) {
    showAnalyticsError("Invalid short URL format");
    return;
  }

  try {
    const response = await fetch(`${baseUrl}/api/details/${shortCode}`, {
      method: "GET",
      mode: "cors",
    });

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}));
      throw new Error(errorData.errorMessage || "URL not found");
    }

    const data = await response.json();
    showAnalyticsResult(data);
  } catch (error) {
    console.error("Error fetching analytics:", error);
    showAnalyticsError(error.message || "Failed to fetch URL details");
  }
}

function extractShortCode(url) {
  try {
    const urlObj = new URL(url);
    const pathname = urlObj.pathname;

    // Handle /s/{shortCode} format
    if (pathname.startsWith("/s/")) {
      return pathname.substring(3); // Remove '/s/'
    }

    // Fallback: remove leading slash
    return pathname.substring(1);
  } catch {
    // If not a valid URL, assume it's just the short code
    const lastSlashIndex = url.lastIndexOf("/");
    return lastSlashIndex !== -1 ? url.substring(lastSlashIndex + 1) : url;
  }
}

function showAnalyticsResult(data) {
  const createdDate = data.createdAt
    ? new Date(data.createdAt).toLocaleString()
    : "Not available";
  const expiryDate = new Date(data.expiryDate).toLocaleString();
  const lastClickedDate = data.lastClickedAt
    ? new Date(data.lastClickedAt).toLocaleString()
    : "Never";
  const shortUrl = `${baseUrl}/s/${data.shortUrlCode}`;

  analyticsContent.innerHTML = `
    <div class="analytics-item">
      <strong>Short URL:</strong> 
      <a href="${shortUrl}" target="_blank">${shortUrl}</a>
    </div>
    <div class="analytics-item">
      <strong>Original URL:</strong> 
      <a href="${data.originalUrl}" target="_blank">${data.originalUrl}</a>
    </div>
    <div class="analytics-item">
      <strong>Created:</strong> ${createdDate}
    </div>
    <div class="analytics-item">
      <strong>Expires:</strong> ${expiryDate}
    </div>
    <div class="analytics-item">
      <strong>Status:</strong> 
      <span class="status-badge ${
        new Date(data.expiryDate) > new Date() ? "active" : "expired"
      }">
        ${new Date(data.expiryDate) > new Date() ? "Active" : "Expired"}
      </span>
    </div>
    
    <!-- Analytics Section -->
    <div class="analytics-section">
      <h4>📊 Click Analytics</h4>
      <div class="analytics-stats">
        <div class="stat-card">
          <div class="stat-number">${data.totalClicks || 0}</div>
          <div class="stat-label">Total Clicks</div>
        </div>
        <div class="stat-card">
          <div class="stat-number">${data.uniqueClicks || 0}</div>
          <div class="stat-label">Unique Users</div>
        </div>
        <div class="stat-card">
          <div class="stat-number">${
            data.totalClicks > 0
              ? Math.round((data.uniqueClicks / data.totalClicks) * 100)
              : 0
          }%</div>
          <div class="stat-label">Unique Rate</div>
        </div>
      </div>
      <div class="analytics-item">
        <strong>Last Clicked:</strong> ${lastClickedDate}
      </div>
      ${
        data.totalClicks > 0
          ? `
        <button class="detailed-analytics-btn" id="detailedAnalyticsBtn" onclick="toggleDetailedAnalytics('${data.shortUrlCode}')">
          📈 View Detailed Analytics
        </button>
      `
          : ""
      }
    </div>
  `;

  analyticsResult.style.display = "block";
}

function hideAnalyticsResult() {
  analyticsResult.style.display = "none";
}

function showAnalyticsError(message) {
  analyticsError.textContent = message;
  analyticsError.style.display = "block";
}

function hideAnalyticsError() {
  analyticsError.style.display = "none";
}

async function toggleDetailedAnalytics(shortCode) {
  const existingDetailed = document.querySelector(".detailed-analytics");
  const btn = document.getElementById("detailedAnalyticsBtn");

  if (existingDetailed) {
    // Hide detailed analytics
    hideDetailedAnalytics();
    if (btn) btn.textContent = "📈 View Detailed Analytics";
  } else {
    // Show detailed analytics
    if (btn) btn.textContent = "⏳ Loading...";
    await showDetailedAnalytics(shortCode);
    if (btn) btn.textContent = "📊 Hide Detailed Analytics";
  }
}

async function showDetailedAnalytics(shortCode) {
  try {
    const response = await fetch(`${baseUrl}/api/analytics/${shortCode}`, {
      method: "GET",
      mode: "cors",
    });

    if (!response.ok) {
      throw new Error(`HTTP error! Status: ${response.status}`);
    }

    const analytics = await response.json();
    displayDetailedAnalytics(analytics);
  } catch (error) {
    console.error("Error fetching detailed analytics:", error);
    showAnalyticsError("Failed to load detailed analytics. Please try again.");
    // Reset button text on error
    const btn = document.getElementById("detailedAnalyticsBtn");
    if (btn) btn.textContent = "📈 View Detailed Analytics";
  }
}

function displayDetailedAnalytics(analytics) {
  // Check if detailed analytics already exists and remove it
  const existingDetailed = document.querySelector(".detailed-analytics");
  if (existingDetailed) {
    existingDetailed.remove();
  }

  const countryData = Object.entries(analytics.clicksByCountry || {})
    .sort(([, a], [, b]) => b - a)
    .slice(0, 5);

  const recentClicks = analytics.recentClicks || [];

  const detailedHtml = `
    <div class="detailed-analytics">
      <h4>🌍 Geographical Distribution</h4>
      <div class="country-stats">
        ${
          countryData.length > 0
            ? countryData
                .map(
                  ([country, count]) => `
            <div class="country-item">
              <span class="country-name">${country}</span>
              <span class="country-count">${count} clicks</span>
            </div>
          `
                )
                .join("")
            : '<p class="no-data">No geographical data available</p>'
        }
      </div>
      
      <h4>🕒 Recent Activity</h4>
      <div class="recent-activity">
        ${
          recentClicks.length > 0
            ? recentClicks
                .slice(0, 5)
                .map(
                  (click) => `
            <div class="activity-item">
              <div class="activity-location">${click.city || "Unknown"}, ${
                    click.country || "Unknown"
                  }</div>
              <div class="activity-time">${new Date(
                click.clickedAt
              ).toLocaleString()}</div>
              ${
                click.referrer
                  ? `<div class="activity-referrer">From: ${click.referrer}</div>`
                  : ""
              }
            </div>
          `
                )
                .join("")
            : '<p class="no-data">No recent activity</p>'
        }
      </div>
      
      <button class="hide-analytics-btn" onclick="hideDetailedAnalytics()">
        Hide Detailed Analytics
      </button>
    </div>
  `;

  // Add detailed analytics to the analytics content (append to existing content)
  analyticsContent.insertAdjacentHTML("beforeend", detailedHtml);
}

function hideDetailedAnalytics() {
  const detailedSection = document.querySelector(".detailed-analytics");
  if (detailedSection) {
    detailedSection.remove();
  }

  // Update button text
  const btn = document.getElementById("detailedAnalyticsBtn");
  if (btn) {
    btn.textContent = "📈 View Detailed Analytics";
  }
}

// Add some additional CSS for analytics
const additionalCSS = `
.analytics-item {
  margin-bottom: 1rem;
  padding: 0.75rem;
  background: #f8f9fa;
  border-radius: 6px;
  border-left: 4px solid #007bff;
}

.analytics-item strong {
  color: #495057;
  display: inline-block;
  min-width: 120px;
}

.analytics-item a {
  color: #007bff;
  text-decoration: none;
  word-break: break-all;
}

.analytics-item a:hover {
  text-decoration: underline;
}

.status-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: 600;
  text-transform: uppercase;
}

.status-badge.active {
  background: #d4edda;
  color: #155724;
}

.status-badge.expired {
  background: #f8d7da;
  color: #721c24;
}

.analytics-section {
  margin-top: 2rem;
  padding: 1.5rem;
  background: #e9ecef;
  border-radius: 8px;
  border: 2px solid #007bff;
}

.analytics-section h4 {
  color: #007bff;
  margin-bottom: 1rem;
}

.analytics-stats {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
  flex-wrap: wrap;
}

.stat-card {
  background: white;
  padding: 1rem;
  border-radius: 6px;
  text-align: center;
  flex: 1;
  min-width: 120px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.stat-number {
  font-size: 1.5rem;
  font-weight: bold;
  color: #28a745;
  margin-bottom: 0.25rem;
}

.stat-label {
  font-size: 0.8rem;
  color: #6c757d;
  text-transform: uppercase;
}

.detailed-analytics-btn, .hide-analytics-btn {
  background: #007bff;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  margin-top: 1rem;
}

.detailed-analytics-btn:hover, .hide-analytics-btn:hover {
  background: #0056b3;
}

.detailed-analytics {
  margin-top: 2rem;
  padding: 1.5rem;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #dee2e6;
}

.detailed-analytics h4 {
  color: #495057;
  margin-bottom: 1rem;
}

.country-stats, .recent-activity {
  margin-bottom: 2rem;
}

.country-item, .activity-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0.5rem 0;
  border-bottom: 1px solid #dee2e6;
}

.country-item:last-child, .activity-item:last-child {
  border-bottom: none;
}

.country-name, .activity-location {
  font-weight: 500;
  color: #495057;
}

.country-count {
  color: #28a745;
  font-weight: 600;
}

.activity-time {
  font-size: 0.8rem;
  color: #6c757d;
}

.activity-referrer {
  font-size: 0.8rem;
  color: #007bff;
  margin-top: 0.25rem;
}

.no-data {
  color: #6c757d;
  font-style: italic;
  text-align: center;
  padding: 1rem;
}

@media (max-width: 768px) {
  .analytics-stats {
    flex-direction: column;
  }
  
  .country-item, .activity-item {
    flex-direction: column;
    align-items: flex-start;
    gap: 0.25rem;
  }
}
`;

// Inject additional CSS
const style = document.createElement("style");
style.textContent = additionalCSS;
document.head.appendChild(style);
