import Vue from 'vue'
import Vuex from 'vuex'
import router, {resetRouter} from "@/router";

Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    // 共享的数据
    currentPathName: '',
    children: '',

  },
  mutations: {
    setPath (state) {
      //将本地存储的菜单赋给currentPathName
      // currentPathName在header绑定
      state.currentPathName = localStorage.getItem("currentPathName")


    },
    logout() {
      // 清空缓存
      localStorage.removeItem("user")
      localStorage.removeItem("menus")
      router.push("/login")

      // 重置路由
      resetRouter()
    }
  }
})

export default store
