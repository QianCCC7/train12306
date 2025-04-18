import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    redirect: '/welcome'
  },
  {
    path: '/',
    name: 'main',
    component: () => import('../views/Main.vue'),
    children: [
      {
        path: 'welcome',
        name: 'welcome',
        component: () => import('../views/main/Welcome.vue'),
        meta: {
          title: '欢迎访问'
        }
      },
      {
        path: 'about',
        name: 'about',
        component: () => import('../views/main/About.vue'),
        meta: {
          title: '关于'
        }
      },
    ],
    meta: {
      title: '首页',
      requireLogin: true
    }
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})


export default router
