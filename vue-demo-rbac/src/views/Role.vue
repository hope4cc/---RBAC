<template>
  <div>


    <div class="mr-top-10">
      <!--          搜索框-->
      <input placeholder="根据用户名查询" style="width: 200px;margin-right: 10px" type="text" v-model="name"
             class="inputSearch">

      <!--          搜索-->
      <button class="button-search" style="color: #090909" @click="load">搜索</button>
      <button class="button-search" style="color: #090909" @click="reset">重置</button>
    </div>

    <div class="mr-top-20">
      <el-button class="button-io" @click="handleAdd">新增
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





    </div>

    <el-table :data="tableData" border stripe class="mr-top-20"
              @selection-change="handleSelectionChange"
              :header-cell-style="{background:'#f4f3f9',color:'#606266'}">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="名称"></el-table-column>
      <el-table-column prop="flag" label="唯一标识"></el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column label="操作"  width="280" align="center">
        <template slot-scope="scope">
          <button class="button-search"
                  @click="selectMenu(scope.row)"
                  style="background: #abadad;border: 1px solid #abadad;">分配菜单
          </button>
          <button class="button-search"
                  @click="handleEdit(scope.row)"
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
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2, 5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>



    <el-dialog title="角色信息" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="80px" size="small">
        <el-form-item label="名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="唯一标识">
          <el-input v-model="form.flag" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" autocomplete="off"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>


    <el-dialog title="菜单分配" :visible.sync="menuDialogVis" width="30%">
      <el-tree
          :props="props"
          :data="menuData"
          show-checkbox
          node-key="id"
          ref="tree"
          :default-expanded-keys="expends"
          :default-checked-keys="checks">
         <span class="custom-tree-node" slot-scope="{ node, data }">
            <span><i :class="data.icon"></i> {{ data.name }}</span>
         </span>
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click="menuDialogVis = false">取 消</el-button>
        <el-button type="primary" @click="saveRoleMenu">确 定</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import {serverIp} from "../../public/config";

export default {
  name: "User",
  data(){
    return {
      serverIp: serverIp,
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      dialogFormVisible: false,
      menuDialogVis: false,
      multipleSelection: [],
      menuData: [],
      props: {
        label: 'name',
      },
      expends: [],
      checks: [],
      roleId: 0,
      roleFlag: '',
      ids: []
    }
  },
  //vue生命周期 页面创建时加载
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/role/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data.records
        this.total = res.data.total
      })

      this.request.get("/menu/ids").then(r => {
        this.ids = r.data
      })

    },
    save() {
      this.request.post("/role", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    saveRoleMenu() {
      this.request.post("/role/roleMenu/" + this.roleId, this.$refs.tree.getCheckedKeys()).then(res => {
        if (res.code === '200') {
          this.$message.success("绑定成功")
          this.menuDialogVis = false

          // 操作管理员角色后需要重新登录
          if (this.roleFlag === 'ROLE_ADMIN') {
            this.$store.commit("logout")
          }

        } else {
          this.$message.error(res.msg)
        }
      })
    },
    handleAdd() {
      this.dialogFormVisible = true
      this.form = {}
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/role/" + id).then(res => {
        if (res.code === '200') {
          this.$message.success("删除成功")
          this.load()
        } else {
          this.$message.error("删除失败")
        }
      })
    },
    handleSelectionChange(val) {
      console.log(val)
      this.multipleSelection = val
    },
    delBatch() {
      let ids = this.multipleSelection.map(v => v.id)  // [{}, {}, {}] => [1,2,3]
      this.request.post("/role/del/batch", ids).then(res => {
        if (res.code === '200') {
          this.$message.success("批量删除成功")
          this.load()
        } else {
          this.$message.error("批量删除失败")
        }
      })
    },
    reset() {
      this.name = ""
      this.load()
    },
    handleSizeChange(pageSize) {
      console.log(pageSize)
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      console.log(pageNum)
      this.pageNum = pageNum
      this.load()
    },
    async selectMenu(role) {
      this.roleId = role.id
      this.roleFlag = role.flag

      // 请求菜单数据
      this.request.get("/menu").then(res => {
        this.menuData = res.data

        // 把后台返回的菜单数据处理成 id数组
        this.expends = this.menuData.map(v => v.id)
      })

      this.request.get("/role/roleMenu/" + this.roleId).then(res => {
        this.checks = res.data
        this.ids.forEach(id => {
          if (!this.checks.includes(id)) {
            // 可能会报错：Uncaught (in promise) TypeError: Cannot read properties of undefined (reading 'setChecked')
            this.$nextTick(() => {
              this.$refs.tree.setChecked(id, false)
            })
          }
        })
        this.menuDialogVis = true
      })
    },
  }
}
</script>


<style scoped>

</style>