<template>
  <div class="wrapper">
    <div
        style=" box-shadow: 0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1); position:absolute; top: 0;left: 0;right: 0; botton: 0; margin:200px auto; webkit-transform:rotate(6deg); background-color: #5fa5fa; width: 400px; height: 500px; padding: 20px; border-radius: 25px"></div>

    <div
        style=" box-shadow: 10px 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1); position:absolute; top: 0;left: 0;right: 0; botton: 0; margin:200px auto; webkit-transform:rotate(-6deg); background-color: #f87171; width: 400px; height: 500px; padding: 20px; border-radius: 25px"></div>

    <div
        style=" box-shadow: 10px 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1); position:absolute; top: 0;left: 0;right: 0; botton: 0; margin:200px auto; shadow-size: 1px; background-color: #f3f4f6; width: 400px; height: 500px; padding: 20px; border-radius: 25px">
      <div style="margin: 20px 0; text-align: center; font-size: 24px"><b>注 册</b></div>
      <el-form :model="user" ref="userForm" :rules="rules">

        <el-form-item prop="username">
          <el-input placeholder="请输入账号" size="medium" style="margin: 10px 0" v-model="user.username"></el-input>
        </el-form-item>

        <el-form-item prop="password">
          <el-input placeholder="请输入密码" size="medium" style="margin: 10px 0" show-password
                    v-model="user.password"></el-input>
        </el-form-item>

        <el-form-item prop="confirmPassword">
          <el-input placeholder="请确认密码" size="medium" style="margin: 10px 0" show-password
                    v-model="user.confirmPassword"></el-input>
        </el-form-item>

        <el-form-item>
          <a href="/login" style="text-decoration:none; color:#000;float:right;">已有账号？去登录</a>
          <!--          <el-button type="warning" size="small" autocomplete="off" @click="$router.push('/register')">注册</el-button>-->
          <el-button class="button-login" autocomplete="off" @click="register">注 册</el-button>

        </el-form-item>

      </el-form>
    </div>
  </div>

</template>

<script>

export default {
  name: "Login",
  data() {
    return {
      checked: true,
      user: {},

      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 5, max: 20, message: '长度在 5 到 20 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 5, max: 20, message: '长度在 5 到 20 个字符', trigger: 'blur'}
        ],
        confirmPassword: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 5, max: 20, message: '长度在 5 到 20 个字符', trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    register() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          if (this.user.password !== this.user.confirmPassword) {
            this.$message.error("两次输入的密码不一致")
            return false
          }
          this.request.post("/user/register", this.user).then(res => {
            if(res.code === '200') {
              this.$message.success("注册成功")
            } else {
              this.$message.error(res.msg)
            }
          })
        }
      });
    }
  },
  watch: {
    // 方法1 //监听路由是否变化
    '$route' (to, from) {
      if(to.query.id !== from.query.id){
        this.id = to.query.id;
        this.init();//重新加载数据
      }
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
  background: url("../assets/img/background.webp");
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
