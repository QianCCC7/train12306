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
      {
        path: 'station',
        name: 'station',
        component: () => import('../views/main/Station.vue'),
        meta: {
          title: '车站管理'
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

router.afterEach((to) => {
  if (to.meta.title) {
    document.title = to.meta.title
  } else {
    document.title = '' //此处写默认的title
  }
})


export default router
