import Vue from 'vue'
import VueRouter from 'vue-router'
import store from "@/store";
import home from "@/views/Home";

import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css'
import getPageTitle from '@/utils/get-page-title'


NProgress.configure({showSpinner: false}) // NProgress Configuration
Vue.use(VueRouter)

const routes = [
  //
  // {
  //     path: '/about',
  //     name: 'About',
  //     // component: () => import('../views/About.vue')
  // },

  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/404',
    name: '404',
    component: () => import('../views/404.vue')
  },
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

// 提供一个重置路由的方法
export const resetRouter = () => {
  router.matcher = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
  })
}

// 注意：刷新页面会导致页面路由重置
export const setRoutes = () => {
  const storeMenus = localStorage.getItem("menus");
  if (storeMenus) {

    // 获取当前的路由对象名称数组
    const currentRouteNames = router.getRoutes().map(v => v.name)
    if (!currentRouteNames.includes('Manage')) {
      // 拼装动态路由
      const manageRoute = { path: '/', name: 'Manage', component: () => import('../views/Manage.vue'), redirect: "/home", children: [
          { path: 'person', name: '个人信息', component: () => import('../views/Person.vue')},
          { path: 'password', name: '修改密码', component: () => import('../views/Password.vue')}
        ] }
      const menus = JSON.parse(storeMenus)
      menus.forEach(item => {
        if (item.path) {  // 当且仅当path不为空的时候才去设置路由
          let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue')}
          manageRoute.children.push(itemMenu)
        } else if(item.children.length) {
          item.children.forEach(item => {
            if (item.path) {
              NProgress.done()
              let itemMenu = { path: item.path.replace("/", ""), name: item.name, component: () => import('../views/' + item.pagePath + '.vue')}
              manageRoute.children.push(itemMenu)
            }
          })
        }
      })
      // 动态添加到现在的路由对象中去
      router.addRoute(manageRoute)
    }

  }
}

// 重置我就再set一次路由
setRoutes()
const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location, onResolve, onReject) {
  if (onResolve || onReject)
    return originalPush.call(this, location, onResolve, onReject);
  return originalPush.call(this, location).catch((err) => err);
};
/**
 * 设置路由守卫
 * to: 下一个页面/即将要进入的目标
 * form: 当前页面
 */
router.beforeEach((to, from, next) => {
  NProgress.start()
  // localStorage.getItem():获取指定本地存储的值
  localStorage.setItem("currentPathName", to.name)  // 设置当前的路由名称
  // commit：同步操作，数据提交至 mutations ，可用于登录成功后读取用户信息写到缓存里
  store.commit("setPath")// 触发store的数据更新

  // 未找到路由的情况
  if (!to.matched.length) {
    const storeMenus = localStorage.getItem("menus")
    if (storeMenus) {
      next("/404")
    } else {
      // 跳回登录页面
      next("/login")
    }
  }
  // 其他的情况都放行（  放行路由）
  next()

})
router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})

export default router
