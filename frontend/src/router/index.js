import { createRouter, createWebHistory } from "vue-router";
import { useAuthStore } from "../stores/auth";

const routes = [
  { path: "/", redirect: "/home" },
  {
    path: "/auth/login",
    component: () => import("../modules/auth/views/LoginView.vue"),
    meta: { guestOnly: true },
  },
  {
    path: "/auth/signup",
    component: () => import("../modules/auth/views/SignupView.vue"),
    meta: { guestOnly: true },
  },
  {
    path: "/auth/forgot-password",
    component: () => import("../modules/auth/views/ForgotPasswordView.vue"),
    meta: { guestOnly: true },
  },
  {
    path: "/home",
    component: () => import("../modules/home/views/HomeView.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/search",
    component: () => import("../modules/home/views/SearchView.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/player/:id",
    component: () => import("../modules/player/views/PlayerView.vue"),
    meta: { requiresAuth: true },
  },
  {
    path: "/admin",
    component: () => import("../modules/admin/views/AdminDashboard.vue"),
    meta: { requiresAuth: true, requiresAdmin: true },
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to) => {
  const auth = useAuthStore();
  if (to.meta.requiresAuth && !auth.isAuthenticated) {
    return "/auth/login";
  }
  if (to.meta.guestOnly && auth.isAuthenticated) {
    return "/home";
  }
  if (to.meta.requiresAdmin && !auth.isAdmin) {
    return "/home";
  }
  return true;
});

export default router;
