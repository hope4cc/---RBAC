<template>
  <div>

    <el-container style="min-height: 100vh;background-color: #fffcff">

      <el-aside :width="sizeWidth + 'px' "
                style="background-color: rgb(238, 241, 246);margin :5px;  box-shadow: 2px 0 6px rgba(0 21 41 / 35%); overflow-x:hidden;border-radius: 10px;">
        <Aside :isCollapse="isCollapse"/>
      </el-aside>

      <el-container>
        <el-header style=" border-bottom: 1px solid #ccc; margin :5px; border-radius: 10px; background-color: white;box-shadow: 2px 0 6px rgba(0 21 41 / 35%)">
          <!--       el-header 顶部导航栏 -->
          <Header :collapseBtnClass="collapseBtnClass" @asideCollapse="collapse" :user="user"/>
        </el-header>

<!--        标签页-->
        <el-main style=" border-bottom: 1px solid #ccc; margin :5px; border-radius: 10px; background-color: white;box-shadow: 2px 0 6px rgba(0 21 41 / 35%)">
          <el-tabs

              v-model="activeIndex"
              type="card"
              @tab-click="clickTab"
              @tab-remove="removeTab"
              closable
          >
            <!-- v-if可用v-show代替，也可考虑使用计算属性代替v-for与v-if功能 -->
            <el-tab-pane
                v-for="item of openTab"
                v-if="openTab.length"
                :key="item.name"
                :label="item.name"
                :name="item.route"
            >
            </el-tab-pane>
          </el-tabs>
          <!--        表示当前页面的子路由会在 <router-view /> 里面展示-->
          <router-view @refreshUser="getUser" />
        </el-main>


      </el-container>
    </el-container>

  </div>
</template>

<script>


import Aside from "@/components/Aside"
import Header from "@/components/Header";
import {serverIp} from "../../public/config";
export default {
  name: 'Home',
  components: {
    Header,
    Aside
  },
  data() {
    return {
      serverIp: serverIp,
      openTab:[],
      activeIndex:'',

      collapseBtnClass: "el-icon-s-fold",
      isCollapse: false,
      sizeWidth: 250,
      user: {}
    }
  },
  created() {
    // 从后台获取最新的User数据
    this.getUser()
  },

  methods: {
    collapse() {
      //  点击收缩按钮触发
      this.isCollapse = !this.isCollapse
      if (this.isCollapse) {
        this.sizeWidth = 64
        this.collapseBtnClass = 'el-icon-s-unfold'
      } else {
        this.sizeWidth = 250
        this.collapseBtnClass = 'el-icon-s-fold'
      }
    },
    getUser() {
      let username = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")).username : ""
      if (username) {
        // 从后台获取User数据
        this.request.get("/user/username/" + username).then(res => {
          // 重新赋值后台的最新User数据
          this.user = res.data
        })
      }
    },
    set_active_index(index){
      this.activeIndex = index
    },
    add_tabs(data){
      this.openTab.push(data)
    },
    delete_tabs(route){
      let index = 0
      for(let gohh of this.openTab){
        if(gohh.route == route){
          break
        }
        index++
      }
      this.openTab.splice(index,1)
    },
    clickTab() {
      this.$router.push({path: this.activeIndex})
      console.log(this.$route.path)
    },
    removeTab (target) {
      if(target == '/page1'){
        return
      }
      this.delete_tabs(target)
      if (this.activeIndex === target) {
        if (this.openTab && this.openTab.length >= 1) {
          console.log('=============', this.openTab[this.openTab.length - 1].route)
          this.set_active_index(this.openTab[this.openTab.length - 1].route)
          this.$router.push({path: this.activeIndex})

        }
      }
    },
  },
  watch: {
    '$route' (to, from) {
      console.log('跳转了')
      let flag = false
      for (let item of this.openTab) {
        if (item.name === to.name) {
          this.set_active_index(to.path)
          flag = true
          break
        }
      }
      if (!flag) {
        this.add_tabs({route: to.path, name: to.name})
        this.set_active_index(to.path)
      }
    },



  },
  mounted () {
    console.log('mounted')
    console.log('mounted1',this.openTab)
    if(this.openTab.length>=1){
    }else{
      if(this.$route.path !== '/home') {
        console.log('mounted222222',this.openTab)
        this.add_tabs({route: '/home' , name: '主页'})
        this.add_tabs({route: this.$route.path , name: this.$route.name })
        this.set_active_index(this.$route.path)
      }else{
        this.add_tabs({route: '/home', name: '主页'})
        this.set_active_index('/home')

      }
    }
  },

}
</script>

<style>
.el-header {
  background-color: #B3C0D1;
  color: #333;
  line-height: 60px;
}

.el-aside {
  color: #333;
}

/* 修改tab的样式一定要删除style标签后的scoped */
.el-tabs__item {
  /*标签页样式*/
}
.el-tabs__active-bar {

}

</style>