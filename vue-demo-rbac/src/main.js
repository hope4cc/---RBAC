import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
//引入element ui
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
//引入全局样式
import './assets/global.css'
import request from "@/utils/request";
//响应式布局
import 'element-ui/lib/theme-chalk/display.css';
//按钮 io网的样式
import './assets/home.css'
import './icon/iconfont'
//封装axios
Vue.prototype.request=request

Vue.config.productionTip = false

//element ui 控件大小 mimi小
Vue.use(ElementUI,{size:"mini"});

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
