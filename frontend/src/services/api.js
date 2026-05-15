import axios from "axios";

export const USE_MOCK_API = false;

const API_BASE_URL =
  import.meta.env.VITE_API_BASE_URL || "http://localhost:8080/api";

const api = axios.create({
  baseURL: API_BASE_URL,
});

const mockState = {
  users: [
    {
      id: 1,
      email: "demo@movieflix.com",
      password: "password123",
      roles: ["ROLE_USER"],
    },
  ],
  profiles: [
    { id: 1, name: "Main", avatar: "A" },
    { id: 2, name: "Kids", avatar: "K" },
  ],
  videos: [
    { id: 1, title: "Skyline", genre: "Sci-Fi", thumbnailUrl: "" },
    { id: 2, title: "Lost City", genre: "Adventure", thumbnailUrl: "" },
    { id: 3, title: "Neon Night", genre: "Action", thumbnailUrl: "" },
    { id: 4, title: "Code Runner", genre: "Thriller", thumbnailUrl: "" },
    { id: 5, title: "Blue Ocean", genre: "Drama", thumbnailUrl: "" },
    { id: 6, title: "Final Stand", genre: "Action", thumbnailUrl: "" },
    { id: 7, title: "Orbit", genre: "Sci-Fi", thumbnailUrl: "" },
    { id: 8, title: "Silent Road", genre: "Mystery", thumbnailUrl: "" },
    { id: 9, title: "Sunrise", genre: "Romance", thumbnailUrl: "" },
    { id: 10, title: "Night Shift", genre: "Crime", thumbnailUrl: "" },
    { id: 11, title: "Zero Day", genre: "Thriller", thumbnailUrl: "" },
    { id: 12, title: "Wild West", genre: "Western", thumbnailUrl: "" },
  ],
  watchHistory: [],
};

const wait = (ms = 250) => new Promise((resolve) => setTimeout(resolve, ms));

const parseQueryFromUrl = (url = "") => {
  const fullUrl = url.startsWith("http")
    ? url
    : `http://localhost${url.startsWith("/") ? "" : "/"}${url}`;
  return new URL(fullUrl).searchParams;
};

const buildPage = (items, page = 0, size = 12) => {
  const safePage = Number(page) || 0;
  const safeSize = Number(size) || 12;
  const start = safePage * safeSize;
  const content = items.slice(start, start + safeSize);
  return {
    content,
    number: safePage,
    size: safeSize,
    totalElements: items.length,
  };
};

const createMockError = (status, message, config) => {
  const error = new Error(message);
  error.config = config;
  error.response = {
    status,
    data: { message },
  };
  return error;
};

const getRolesForEmail = (email) => {
  if ((email || "").toLowerCase().includes("admin")) {
    return ["ROLE_ADMIN", "ROLE_USER"];
  }
  return ["ROLE_USER"];
};

const createAuthTokens = () => ({
  accessToken: `mock-access-${Date.now()}`,
  refreshToken: `mock-refresh-${Date.now()}`,
});

const mockRequest = async (config) => {
  await wait();

  const method = (config.method || "get").toLowerCase();
  const url = config.url || "";
  const pathOnly = url.split("?")[0];
  const query = parseQueryFromUrl(url);
  const body =
    typeof config.data === "string"
      ? JSON.parse(config.data || "{}")
      : config.data || {};

  if (method === "post" && pathOnly === "/auth/login") {
    const user = mockState.users.find((item) => item.email === body.email);
    if (!user) {
      throw createMockError(401, "Invalid credentials", config);
    }
    return {
      data: createAuthTokens(),
      status: 200,
      statusText: "OK",
      headers: {},
      config,
    };
  }

  if (method === "post" && pathOnly === "/auth/login/otp") {
    return {
      data: createAuthTokens(),
      status: 200,
      statusText: "OK",
      headers: {},
      config,
    };
  }

  if (method === "post" && pathOnly === "/auth/otp") {
    return {
      data: { message: "OTP sent" },
      status: 200,
      statusText: "OK",
      headers: {},
      config,
    };
  }

  if (method === "post" && pathOnly === "/auth/signup") {
    const exists = mockState.users.some((item) => item.email === body.email);
    if (exists) {
      throw createMockError(400, "Email already exists", config);
    }
    mockState.users.push({
      id: mockState.users.length + 1,
      email: body.email,
      password: body.password,
      roles: getRolesForEmail(body.email),
    });
    return {
      data: { message: "Signup successful" },
      status: 200,
      statusText: "OK",
      headers: {},
      config,
    };
  }

  if (method === "post" && pathOnly === "/auth/forgot-password/reset") {
    return {
      data: { message: "Password reset successful" },
      status: 200,
      statusText: "OK",
      headers: {},
      config,
    };
  }

  if (method === "post" && pathOnly === "/auth/refresh") {
    return {
      data: createAuthTokens(),
      status: 200,
      statusText: "OK",
      headers: {},
      config,
    };
  }

  if (method === "get" && pathOnly === "/profiles") {
    return {
      data: [...mockState.profiles],
      status: 200,
      statusText: "OK",
      headers: {},
      config,
    };
  }

  if (method === "post" && pathOnly === "/profiles") {
    const created = {
      id: mockState.profiles.length
        ? Math.max(...mockState.profiles.map((item) => item.id)) + 1
        : 1,
      name: body.name || `Profile ${mockState.profiles.length + 1}`,
      avatar: body.avatar || "P",
    };
    mockState.profiles.push(created);
    return {
      data: created,
      status: 200,
      statusText: "OK",
      headers: {},
      config,
    };
  }

  if (method === "get" && pathOnly === "/videos") {
    const q = (query.get("q") || "").trim().toLowerCase();
    const page = query.get("page") || 0;
    const size = query.get("size") || 12;
    const filtered = q
      ? mockState.videos.filter((video) =>
          `${video.title} ${video.genre}`.toLowerCase().includes(q),
        )
      : mockState.videos;
    return {
      data: buildPage(filtered, page, size),
      status: 200,
      statusText: "OK",
      headers: {},
      config,
    };
  }

  if (method === "post" && pathOnly === "/videos/upload") {
    const created = {
      id: mockState.videos.length + 1,
      title: "Uploaded Video",
      genre: "General",
      thumbnailUrl: "",
    };
    mockState.videos.unshift(created);
    return {
      data: created,
      status: 200,
      statusText: "OK",
      headers: {},
      config,
    };
  }

  if (method === "get" && pathOnly === "/admin/dashboard") {
    return {
      data: {
        users: mockState.users.length,
        videos: mockState.videos.length,
      },
      status: 200,
      statusText: "OK",
      headers: {},
      config,
    };
  }

  if (method === "post" && pathOnly === "/watch-history") {
    const entryIndex = mockState.watchHistory.findIndex(
      (item) =>
        item.profileId === body.profileId && item.videoId === body.videoId,
    );
    const entry = {
      profileId: body.profileId,
      videoId: body.videoId,
      lastWatchedPosition: body.lastWatchedPosition || 0,
      updatedAt: new Date().toISOString(),
    };
    if (entryIndex === -1) {
      mockState.watchHistory.push(entry);
    } else {
      mockState.watchHistory.splice(entryIndex, 1, entry);
    }
    return { data: entry, status: 200, statusText: "OK", headers: {}, config };
  }

  if (method === "get" && pathOnly === "/watch-history") {
    const profileId = Number(query.get("profileId") || 0);
    const page = query.get("page") || 0;
    const size = query.get("size") || 20;
    const filtered = mockState.watchHistory
      .filter((item) => item.profileId === profileId)
      .sort((a, b) => new Date(b.updatedAt) - new Date(a.updatedAt));
    return {
      data: buildPage(filtered, page, size),
      status: 200,
      statusText: "OK",
      headers: {},
      config,
    };
  }

  throw createMockError(
    404,
    `Mock route not found: ${method.toUpperCase()} ${pathOnly}`,
    config,
  );
};

if (USE_MOCK_API) {
  api.defaults.adapter = mockRequest;
} else {
  api.interceptors.request.use((config) => {
    const accessToken = localStorage.getItem("accessToken");
    if (accessToken) {
      config.headers.Authorization = `Bearer ${accessToken}`;
    }
    return config;
  });

  api.interceptors.response.use(
    (response) => response,
    async (error) => {
      const refreshToken = localStorage.getItem("refreshToken");
      const original = error.config;
      if (error.response?.status === 401 && refreshToken && !original._retry) {
        original._retry = true;
        const { data } = await axios.post(`${API_BASE_URL}/auth/refresh`, {
          refreshToken,
        });
        localStorage.setItem("accessToken", data.accessToken);
        original.headers.Authorization = `Bearer ${data.accessToken}`;
        return api(original);
      }
      return Promise.reject(error);
    },
  );
}

export default api;
