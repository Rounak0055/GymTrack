import axios from "axios";

// ✅ Use environment variable or default to localhost
const API_BASE_URL = process.env.REACT_APP_API_URL || "http://localhost:8080";

const API = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

// ✅ Request interceptor (optional - for future auth tokens)
API.interceptors.request.use(
  (config) => {
    // Add auth token if needed in future
    return config;
  },
  (error) => Promise.reject(error)
);

// ✅ Response interceptor for error handling
API.interceptors.response.use(
  (response) => response,
  (error) => {
    if (!error.response) {
      console.error("Network error or server not responding");
    }
    return Promise.reject(error);
  }
);

export default API;
