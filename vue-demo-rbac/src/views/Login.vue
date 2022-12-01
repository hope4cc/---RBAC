<template>
  <div class="wrapper">
    <div
        style=" box-shadow: 0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1); position:absolute; top: 0;left: 0;right: 0; botton: 0; margin:200px auto; webkit-transform:rotate(6deg); background-color: #5fa5fa; width: 400px; height: 500px; padding: 20px; border-radius: 25px"></div>

    <div
        style=" box-shadow: 10px 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1); position:absolute; top: 0;left: 0;right: 0; botton: 0; margin:200px auto; webkit-transform:rotate(-6deg); background-color: #f87171; width: 400px; height: 500px; padding: 20px; border-radius: 25px"></div>

    <div
        style=" box-shadow: 10px 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1); position:absolute; top: 0;left: 0;right: 0; botton: 0; margin:200px auto; shadow-size: 1px; background-color: #f3f4f6; width: 400px; height: 500px; padding: 20px; border-radius: 25px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>登 录</b></div>
      <el-form :model="user" ref="userForm" :rules="rules">
        <el-form-item prop="username">
          <el-input size="medium" style="margin: 10px 0" v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" style="margin: 10px 0" show-password
                    v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item>
<!--          v-model="checked"-->
          <el-checkbox >记住我</el-checkbox>
          <a href=" /register" style="text-decoration:none; color:#000;float:right;"  >没有账号？去注册</a>

          <!--          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>-->
          <el-button class="button-login" autocomplete="off" @click="login">登 录</el-button>
          <hr class="hr">

          <button class="button-others_login">QQ</button>

          <button class="button-others_login" style="position:absolute; right: 5px;background: #28c444">微信</button>


        </el-form-item>

      </el-form>
    </div>
  </div>

</template>

<script>
import {setRoutes} from "@/router";
import {serverIp} from "../../public/config";

export default {
  name: "Login",
  data() {
    return {
      user: {},
      rules: {
        serverIp: serverIp,
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在 3 到 5 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    login() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          this.request.post("/user/login", this.user).then(res => {
            if (res.code === '200') {
              localStorage.setItem("user", JSON.stringify(res.data))  // 存储用户信息到浏览器
              localStorage.setItem("menus", JSON.stringify(res.data.menus))  // 存储菜单信息到浏览器
             // 动态设置当前用户的路由
              setRoutes()
              this.$message.success("登录成功")
              this.$router.push("/home")
              if (res.data.role === 'ROLE_STUDENT') {
                this.$router.push("/home")
              }
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    }
  }
}
</script>
<!--lang="scss" scoped-->
<style>

.wrapper {
  height: 100vh;
  /*background-image: linear-gradient(to bottom right, #FC466B, #3F5EFB);*/
  overflow: hidden;
  background: url("../assets/img/background.webp") no-repeat;
  /*background-repeat: no-repeat;*/
  background-size: cover;
}

.icon {
  width: 2em;
  height: 2em;
  vertical-align: -0.15em;
  fill: currentColor;
  overflow: hidden;
}

</style>
