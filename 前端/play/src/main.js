// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import Vuex from 'vuex'
import App from './App'
import router from './router'
import cookieOpe from './utils/cookieOp'
import store from './store/index.js'
import ElementUI from 'element-ui'
import '../node_modules/element-ui/lib/theme-chalk/index.css'
import axios from 'axios'
import VueQuillEditor from 'vue-quill-editor'
import 'quill/dist/quill.snow.css'
import 'url-search-params-polyfill'

Vue.use(VueQuillEditor)
Vue.use(Vuex)
Vue.use(ElementUI)
Vue.use(cookieOpe)

Vue.prototype.$url = 'http://localhost:8081'
Vue.prototype.$axios = axios
Vue.config.productionTip = false
Vue.prototype.$image_url = 'http://localhost:8081/image/HeadImage/'

// http request 请求拦截器
axios.interceptors.request.use(
  config => {
    var token = Vue.prototype.$getCookie('play_access_token')
    if (token === '') {
      store.commit('loginOut')
      router.replace('/login')
    }
    return config
  },
  err => {
    return Promise.reject(err)
  }
)

// http response 拦截器
axios.interceptors.response.use(
  response => {
    return response
  },
  error => {
    console.log(error)
    if (error.response) {
      switch (error.response.status) {
        case 401:
        // case 500:
        // 返回 401 清除token信息并跳转到登录页面
          store.commit('loginOut')
          router.replace('/login')
      }
    }
    // 返回接口返回的错误信息
    return Promise.reject(error)
  }
)

/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  store,
  components: { App },
  template: '<App/>'
})
