import { createRouter, createWebHistory } from 'vue-router'
import store from "@/store";
import {message} from "ant-design-vue";

const routes = [
  {
    path: '/login',
    name: 'login',
    component: () => import('../views/Login.vue'),
    meta: {
      title: '用户登录'
    }
  },
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
        path: 'passenger',
        name: 'passenger',
        component: () => import('../views/main/Passenger.vue'),
        meta: {
          title: '乘客管理'
        }
      },
      {
        path: 'train-ticket',
        name: 'train-ticket',
        component: () => import('../views/main/TrainTicket.vue'),
        meta: {
          title: '余票查询'
        }
      },
      {
        path: 'train-ticket-reserve',
        name: 'train-ticket-reserve',
        component: () => import('../views/main/TrainTicketReserve.vue'),
        meta: {
          title: '余票预定'
        }
      },
      {
        path: 'member-ticket',
        name: 'member-ticket',
        component: () => import('../views/main/MemberTicket.vue'),
        meta: {
          title: '购票记录'
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

// 全局前置守卫,用于权限验证、登录状态检查等
router.beforeEach((to, from, next) => {
  if (to.matched.some((item) => { return item.meta.requireLogin })) {
    const _member = store.state.member;
    if (!_member.token) {
      message.error("用户未登录或Token无效");
      next('/login');
    } else {
      next();
    }
  } else {
    next();
  }
})

// 全局后置守卫,用于执行一些后续操作,如页面标题更新、发送分析数据
router.afterEach((to) => {
  if (to.meta.title) {
    document.title = to.meta.title
  } else {
    document.title = '' //此处写默认的title
  }
})

export default router
