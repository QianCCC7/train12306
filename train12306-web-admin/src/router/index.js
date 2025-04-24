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
        path: 'base/',
        name: 'base',
        children: [
          {
            path: 'station',
            name: 'station',
            component: () => import('../views/main/base/Station.vue'),
            meta: {
              title: '车站管理'
            }
          },
          {
            path: 'train',
            name: 'train',
            component: () => import('../views/main/base/Train.vue'),
            meta: {
              title: '车次管理'
            }
          },
          {
            path: 'train-station',
            name: 'train-station',
            component: () => import('../views/main/base/TrainStation.vue'),
            meta: {
              title: '车次历经车站管理'
            }
          },
          {
            path: 'train-carriage',
            name: 'train-carriage',
            component: () => import('../views/main/base/TrainCarriage.vue'),
            meta: {
              title: '车厢管理'
            }
          },
          {
            path: 'train-seat',
            name: 'train-seat',
            component: () => import('../views/main/base/TrainSeat.vue'),
            meta: {
              title: '车座管理'
            }
          },
        ]
      },
      {
        path: 'batch/',
        name: 'batch',
        children: [
          {
            path: 'batch-job',
            name: 'batch-job',
            component: () => import('../views/main/batch/Job.vue'),
            meta: {
              title: '任务管理'
            }
          },
        ]
      },
      {
        path: 'business/',
        name: 'business',
        children: [
          {
            path: 'daily-train',
            name: 'daily-train',
            component: () => import('../views/main/business/DailyTrain.vue'),
            meta: {
              title: '每日车次管理'
            }
          },
        ]
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
