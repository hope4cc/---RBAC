<template>

  <el-menu :default-openeds="opens" style="min-height:100%;overflow-x:hidden;"

           text-color="#555555"
           :collapse-transition="false"
           active-text-color="#ff879e"
           :collapse="isCollapse"
           router
  >
    <!--          logo-->
    <div style="height: 60px;line-height: 60px;text-align: center;margin-bottom: 20px;">
      <img src="../assets/logo1.png" style="width: 30px; position: relative; top: 8px">
      <!--            这里绑定 isCollapse 文字跟随按钮-->
      <b style="color: #000000" v-show="!isCollapse">后台管理系统</b>
    </div>

    <div v-for="item in menus" :key="item.id">
      <div v-if="item.path">
        <el-menu-item :index="item.path">
          <i :class="item.icon"></i>
          <span slot="title">{{ item.name }}</span>
        </el-menu-item>
      </div>
      <div v-else>
        <el-submenu :index="item.id + ''">
          <template slot="title">
            <i :class="item.icon"></i>
            <span slot="title">{{ item.name }}</span>
          </template>
          <div  v-for="subItem in item.children" :key="subItem.id">
            <el-menu-item :index="subItem.path">
              <i :class="subItem.icon"></i>
              <span slot="title">{{ subItem.name }}</span>
            </el-menu-item>
          </div>
        </el-submenu>
      </div>
    </div>



  </el-menu>

</template>
<!--    <el-menu-item index="/">-->
<!--      <i class="el-icon-s-home"></i>-->
<!--      <span slot="title">首页</span>-->
<!--    </el-menu-item>-->

<!--    <el-submenu index="1">-->
<!--      <template slot="title">-->
<!--        <i class="el-icon-user-solid"></i>-->
<!--        <span id="biaoti" slot="title">用户管理</span>-->
<!--      </template>-->
<!--      <el-menu-item-group>-->
<!--        <el-menu-item  index="/admin">管理员</el-menu-item>-->
<!--        <el-menu-item index="/user">用户管理</el-menu-item>-->
<!--        <el-menu-item index="/role">角色管理</el-menu-item>-->
<!--      </el-menu-item-group>-->
<!--    </el-submenu>-->

<!--    <el-submenu index="2">-->
<!--      <template slot="title">-->
<!--        <i class="el-icon-setting"></i>-->
<!--        <span id="biaoti" slot="title">系统管理</span>-->
<!--      </template>-->
<!--      <el-menu-item-group>-->
<!--        <el-menu-item index="/menu">菜单管理</el-menu-item>-->
<!--        <el-menu-item index="/file">文件管理</el-menu-item>-->
<!--      </el-menu-item-group>-->
<!--    </el-submenu>-->

<!--    <el-submenu index="4">-->
<!--      <template slot="title"><i class="el-icon-reading"></i>-->
<!--        <span slot="title">图书管理</span>-->
<!--      </template>-->
<!--      <el-menu-item-group>-->
<!--        <el-menu-item index="/book">图书管理</el-menu-item>-->
<!--        <el-menu-item index="/category">图书类别</el-menu-item>-->
<!--      </el-menu-item-group>-->
<!--    </el-submenu>-->

<!--    <el-submenu index="5">-->
<!--      <template slot="title"><i class="el-icon-notebook-1"></i>-->
<!--        <span slot="title">借阅管理</span>-->
<!--      </template>-->
<!--      <el-menu-item-group>-->
<!--        <el-menu-item index="borrow">借阅人管理</el-menu-item>-->
<!--        <el-menu-item index="/borrowing">图书借阅管理</el-menu-item>-->
<!--      </el-menu-item-group>-->
<!--    </el-submenu>-->

<!--      <el-submenu index="6">-->
<!--        <template slot="title"><i class="el-icon-star-on"></i>-->
<!--          <span slot="title">会员系统</span>-->
<!--        </template>-->
<!--        <el-menu-item-group>-->
<!--          <el-menu-item index="bookcard">图书卡</el-menu-item>-->
<!--        </el-menu-item-group>-->
<!--    </el-submenu>-->




<script>
export default {
  name: "Aside.vue",
  props: {
    isCollapse: Boolean,
    // logoTextshow: Boolean
  },
  data() {
    return {
      menus: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")) : [],
      opens: localStorage.getItem("menus") ? JSON.parse(localStorage.getItem("menus")).map(v => v.id + '') : []
    }
  },
}
</script>
<style>

.el-submenu:hover{
  background-color: #ffeaf8 !important;
}
.el-menu-item:hover{
  background-color: #fff0f7 !important;
}
.text {
  width: 100%;
  font-size: 12px;
  font-family: "Microsoft YaHei";
  color: #909399;
}
/*由于 element-ui 的<el-menu>标签
本身希望里面嵌套的是<el-menu-item>,<el-submenu>,<el-menu-item-group>之一
但是却嵌套了<div>,而导致收折就隐藏不了文字*/

/*这里研究了一下，发现只要我把el-submenu直接放入到el-menu中是可以的，*/
/*但是实际业务中做动态路由的时候会带着菜单项也为动态的，那么基本上就会吧菜单项抽离为组件然后循环渲染，*/
/*但是由于 element-ui 的<el-menu>标签本身希望里面嵌套的是<el-menu-item>,<el-submenu>,<el-menu-item-group>之一， */
/*但是却嵌套了<div>,而导致收折就隐藏不了文字那么解决方案就来了，我们手动写样式将文字和>隐藏！*/
/*注意<style>标签中不要加scope，否则导致>还是无法隐藏*/
/*隐藏文字*/
.el-menu--collapse  .el-submenu__title span{
  display: none;
}
/*隐藏 > */
.el-menu--collapse  .el-submenu__title .el-submenu__icon-arrow{
  display: none;
}


</style>
