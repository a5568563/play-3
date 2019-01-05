import Vue from 'vue'
import Router from 'vue-router'
import Main from '@/pages/Main'
import Login from '@/pages/Login'
import New from '@/pages/New'
import UMessage from '@/pages/UMessage'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/login',
      name: 'Login',
      component: Login
    },
    {
      path: '/main',
      name: 'Main',
      component: Main
    },
    {
      path: '/new',
      name: 'New',
      component: New
    },
    {
      path: '/um',
      name: 'UMessage',
      component: UMessage
    }
  ],
  mode: 'history'
})
