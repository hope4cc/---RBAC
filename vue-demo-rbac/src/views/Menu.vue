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
      <el-button class="button-io" @click="handleAdd(null)">新增
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
        <el-button class="button-io" slot="reference">批量删除</el-button>
      </el-popconfirm>


    </div>

    <el-table :data="tableData" border stripe :header-cell-class-name="'headerBg'"
              class="mr-top-20"
              row-key="id" default-expand-all @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55"></el-table-column>
      <el-table-column prop="id" label="ID" width="80"></el-table-column>
      <el-table-column prop="name" label="名称"></el-table-column>
      <el-table-column prop="path" label="路径"></el-table-column>
      <el-table-column prop="pagePath" label="页面路径"></el-table-column>
      <el-table-column label="图标" class-name="fontSize18" align="center" label-class-name="fontSize12">
        <template slot-scope="scope">
          <span style="font-size: 20px" :class="scope.row.icon" />
        </template>
      </el-table-column>
      <el-table-column prop="description" label="描述"></el-table-column>
      <el-table-column prop="sortNum" label="顺序"></el-table-column>

      <el-table-column label="操作" width="300px">

        <template slot-scope="scope">
          <button class="button-search"
                  @click="handleAdd(scope.row.id)"
                  v-if="!scope.row.pid&&!scope.row.path"
                  style="background: #67c23a;border: 1px solid #67c23a;">新增子菜单
          </button>
          <button class="button-search" @click="handleEdit(scope.row)"
                  style="background: #67c23a;border: 1px solid #67c23a;">编辑
          </button>
          <el-popconfirm
              confirm-button-text='删除'
              cancel-button-text='我在想想'
              icon="el-icon-info"
              icon-color="red"
              title="你确定要删除吗？"
              @confirm="del(scope.row.id)"
          >
            <button class="button-search" slot="reference" style="background: #f66c6c;border: 1px solid #f66c6c;">
              删除
            </button>
          </el-popconfirm>
        </template>
      </el-table-column>

    </el-table>

    <el-dialog title="菜单信息" :visible.sync="dialogFormVisible" width="30%" >
      <el-form label-width="80px" size="small">
        <el-form-item label="名称">
          <el-input v-model="form.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="路径">
          <el-input v-model="form.path" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="页面路径">
          <el-input v-model="form.pagePath" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="图标">
          <el-select clearable v-model="form.icon" placeholder="请选择" style="width: 100%">
            <el-option v-for="item in options" :key="item.name" :label="item.name" :value="item.value">
              <i :class="item.value" /> {{ item.name }}
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="顺序">
          <el-input v-model="form.sortNum" autocomplete="off"></el-input>
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


  </div>
</template>

<script>
import {serverIp} from "../../public/config";
export default {
  name: "Menu",
  data() {
    return {
      serverIp: serverIp,
      tableData: [],
      total: 0,
      pageNum: 1,
      pageSize: 10,
      name: "",
      form: {},
      dialogFormVisible: false,
      multipleSelection: [],
      options: []
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      this.request.get("/menu", {
        params: {
          name: this.name,
        }
      }).then(res => {
        this.tableData = res.data
      })

      // 请求图标的数据
      this.request.get("/menu/icons").then(res => {
        this.options = res.data
      })
    },
    save() {
      this.request.post("/menu", this.form).then(res => {
        if (res.code === '200') {
          this.$message.success("保存成功")
          this.dialogFormVisible = false
          this.load()
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    handleAdd(pid) {
      this.dialogFormVisible = true
      this.form = {}
      if (pid) {
        this.form.pid = pid
      }
    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    del(id) {
      this.request.delete("/menu/" + id).then(res => {
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
      this.request.post("/menu/del/batch", ids).then(res => {
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
    // exp() {
    //   window.open(`http://${serverIp}:8080/role/export`)
    // },
    // handleExcelImportSuccess() {
    //   this.$message.success("导入成功")
    //   this.load()
    // }
  }
}
</script>

<style scoped>
.fontSize18{
  font-size: 40px;
}
.fontSize12{
  font-size: 12px;
}
</style>
