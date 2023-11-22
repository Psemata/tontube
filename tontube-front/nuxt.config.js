export default {
  // Disable server-side rendering: https://go.nuxtjs.dev/ssr-mode
  ssr: false,

  // Global page headers: https://go.nuxtjs.dev/config-head
  head: {
    title: "TonTube",
    htmlAttrs: {
      lang: "fr",
    },
    meta: [
      { charset: "utf-8" },
      { name: "viewport", content: "width=device-width, initial-scale=1" },
      { hid: "description", name: "description", content: "" },
      { name: "format-detection", content: "telephone=no" },
    ],
    link: [{ rel: "icon", type: "image/x-icon", href: "/favicon.ico" }],
  },

  // Global CSS: https://go.nuxtjs.dev/config-css
  css: ["@/assets/css/main.css"],

  // Plugins to run before rendering page: https://go.nuxtjs.dev/config-plugins
  plugins: [],

  // Auto import components: https://go.nuxtjs.dev/config-components
  components: true,

  // Modules for dev and build (recommended): https://go.nuxtjs.dev/config-modules
  buildModules: [
    // https://go.nuxtjs.dev/tailwindcss
    "@nuxtjs/tailwindcss",
    "@nuxtjs/moment",
  ],

  // Modules: https://go.nuxtjs.dev/config-modules
  modules: [
    // https://go.nuxtjs.dev/axios
    "@nuxtjs/axios",
    "@nuxtjs/auth-next",
    "@nuxtjs/toast",
  ],

  // Axios module configuration: https://go.nuxtjs.dev/config-axios
  axios: {
    // Workaround to avoid enforcing hard-coded localhost:3000: https://github.com/nuxt-community/axios-module/issues/308
    baseURL: "http://localhost:9991/api",
  },

  auth: {
    strategies: {
      local: {
        token: {
          property: "token",
          global: true,
          maxAge: 2629800, //One month*/
        },
        user: {
          property: "user",
        },
        endpoints: {
          login: {
            url: "/auth/login",
            method: "post",
            propertyName: "token",
          },
          logout: false,
          user: {
            url: "/auth/user",
            method: "get",
            propertyName: "user",
          },
        },
      },
    },
    redirect: {
      login: "/login",
      logout: "/",
      callback: "/login",
      home: "/",
      user: "/",
    },
  },
  toast: {
    position: "top-center",
    iconPack: "fontawesome",
    duration: 3000,
    register: [
      {
        name: "defaultSuccess",
        message: (payload) => (!payload.msg ? "Register" : payload.msg),
        options: {
          type: "success",
          icon: "check",
        },
      },
      {
        name: "defaultError",
        message: (payload) => (!payload.msg ? "Error" : payload.msg),
        options: {
          type: "error",
          icon: "times",
        },
      },
    ],
  },

  // Build Configuration: https://go.nuxtjs.dev/config-build
  build: {},
};
