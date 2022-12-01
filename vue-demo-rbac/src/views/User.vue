<template>
  <div>


    <div class="mr-top-10">
      <!--          搜索框-->
      <input placeholder="根据用户名查询" style="width: 200px;margin-right: 10px" type="text" v-model="username"
             class="inputSearch">
      <input placeholder="根据邮箱查询" style="width: 200px;margin-right: 10px" type="text" v-model="email"
             class="inputSearch">
      <input placeholder="根据地址查询" style="width: 200px;margin-right: 10px" type="text" v-model="address"
             class="inputSearch">




      <!--          搜索-->
      <button class="button-search" style="color: #090909" @click="pageload">搜索</button>
      <button class="button-search" style="color: #090909" @click="reset">重置</button>
    </div>

    <div class="mr-top-20">
        <el-button class="button-io" @click="useradd">新增
      </el-button>

      <!--            批量删除-->


      <el-popconfirm
          confirm-button-text='批量删除'
          cancel-button-text='我在想想'
          icon="el-icon-info"
          icon-color="red"
          title="你确定要批量删除这些数据吗？"
          @confirm="delBatch"
      >
        <el-button class="button-io" slot="reference" >批量删除</el-button>
      </el-popconfirm>


      <el-upload :action="'http://' + serverIp + ':8088/user/import'"
                 style="display: inline-block"
                 :show-file-list="false"
                 accept=".xlsx"
                 ref='upload'
                 :on-success="ExcelImportSuccess"
      >
        <el-button class="button-io" >导入</el-button>
      </el-upload>



      <el-popconfirm
          confirm-button-text='确定导出'
          cancel-button-text='取消'
          icon="el-icon-info"
          icon-color="red"
          title="你确定要导出全部数据吗？"
          @confirm="userexp"
      >
        <el-button class="button-io" slot="reference" >导出</el-button>
      </el-popconfirm>

    </div>

    <el-table
        class="mr-top-20"
        :data="tableData" border stripe :header-cell-class-name="'headerBg'" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="username" label="用户名" width="140"></el-table-column>
      <el-table-column prop="role" label="角色">
        <template slot-scope="scope">
          <el-tag type="primary" v-if="scope.row.role === 'ROLE_ADMIN'">管理员</el-tag>
          <el-tag type="warning" v-if="scope.row.role === 'ROLE_TEACHER'">老师</el-tag>
          <el-tag type="success" v-if="scope.row.role === 'ROLE_STUDENT'">学生</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="nickname" label="昵称" width="120"></el-table-column>
      <el-table-column prop="email" label="邮箱"></el-table-column>
      <el-table-column prop="phone" label="电话"></el-table-column>
      <el-table-column prop="address" label="地址"></el-table-column>
      <el-table-column label="操作" width="200px">
        <template slot-scope="scope">
          <button class="button-search" @click="userEdit(scope.row)"
                  style="background: #67c23a;border: 1px solid #67c23a;">编辑
          </button>
          <el-popconfirm
              confirm-button-text='删除'
              cancel-button-text='我在想想'
              icon="el-icon-info"
              icon-color="red"
              title="你确定要删除吗？"
              @confirm="userdelete(scope.row.id)"
          >
            <button class="button-search" slot="reference" style="background: #f66c6c;border: 1px solid #f66c6c;">
              删除
            </button>
          </el-popconfirm>
        </template>
      </el-table-column>

    </el-table>


    <!--          分页-->
    <div style="padding-top: 20px">
      <span class="demonstration"></span>
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[5, 10, 15, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>

    </div>
    <el-dialog title="用户信息" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="80px" size="small">
        <el-form-item label="用户名">
          <el-input v-model="form.username" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="角色">
          <el-select clearable v-model="form.role" placeholder="请选择角色" style="width: 100%">
            <el-option v-for="item in roles" :key="item.name" :label="item.name" :value="item.flag"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="form.email" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="form.phone" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {serverIp} from "../../public/config";
export default {
  name: "User",
  data(){
    return{
      serverIp: serverIp,
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      username: "",
      email: "",
      address: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      roles: [],
      courses: [],
      vis: false,
      stuCourses: [],
      stuVis: false,
      input3: '',
      select: ''

    }
  },
  //vue生命周期 页面创建时加载
  created() {
    //请求分页查询数据
    this.pageload()
  },
  methods:{
    //请求分页查询数据
    pageload() {
      this.request.get("/user/page",
          {
            params: {
              pageNum: this.pageNum,
              pageSize: this.pageSize,
              username: this.username,
              email: this.email,
              address: this.address,

            }
          }).then(res => {

        this.tableData = res.data.records
        this.total = res.data.total

      })

      this.request.get("/role").then(res => {
        this.roles = res.data
      })
    },
    userexp() {
      window.open(`http://${serverIp}:8088/user/export`)
    },
    ExcelImportSuccess(){
      this.$message.success("文件导入成功")
      this.pageload()
      this.$refs["upload"].clearFiles()
    },
    save() {
      this.request.post("/user", this.form).then(res => {
        if (res.data) {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.pageload()
        } else {
          this.$message.error("保存失败")
        }
      })
    },

    reset() {
      this.username = ""
      this.nickname = ""
      //清空表单后，调用分页数据
      this.pageload()
    },
    useradd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    userEdit(row) {
      this.form = Object.assign({}, row)//解决没点击确定前的双向绑定
      this.dialogFormVisible = true
    },
    cancele() {
      this.dialogFormVisible = false
      this.pageload()
    },
    userdelete(id) {
      this.request.delete("user/" + id).then(res => {
        if (res.data) {
          this.$message.success("删除成功")
          this.pageload()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/user/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.pageload()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    userDeleteBatch() {
      let ids = this.multipleSelection.map(v => v.id)// 对象数组变成纯id数组 [{},{},{}] => [1,2,3]
      this.request.post("user/delete/batch", ids).then(res => {
        if (res.data) {
          this.$message.success("批量删除成功")
          this.pageload()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },

    //分页数据数量
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.pageload()
    },

    //分类数据索引
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.pageload()
    }
  },





}
</script>

<style>
.el-select .el-input {
  width: 130px;
}
.input-with-select .el-input-group__prepend {
  background-color: #fff;
}
</style>
