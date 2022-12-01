<template>
<div>

  <el-row :gutter="10">
    <el-col :span="8">
      <el-card shadow="hover" style="background-color: #f3f4f6" body-style="margin-left: 20p">
        <div id="main" style="width: 400px;height: 370px"></div>
      </el-card>
    </el-col>

    <el-col :span="8">
      <el-card shadow="hover" style="background-color: #f3f4f6;">
        <div id="pie" style="width: 400px;height: 370px;text-align: center"></div>
      </el-card>
    </el-col>

    <el-col :span="8">
      <el-card shadow="hover" style="background-color: #f3f4f6;">
        <div id="coxcomb" style="width: 370px;height: 370px;text-align: center"></div>
      </el-card>
    </el-col>

  </el-row>

</div>
</template>

<script>
import * as echarts from 'echarts';
import {serverIp} from "../../public/config";
export default {

  name: "Home",
  data(){
    return{
      serverIp: serverIp,
      series:[]
    }
  },



  mounted() {
    var option = {
      title: {
        text: '各季度会员统计',
        subtext: '趋势图',
        left: 'center'
      },
      xAxis: {
        type: 'category',
        data: ["第一季度", "第二季度", "第三季度", "第四季度"]
      },
      yAxis: {
        type: 'value'
      },
      series: [
        {
          data: [],
          type: 'line'
        },
        {
          data: [],
          // type:'bar' //柱状图
        }
      ]
    };


    //饼图
    var pieOption = {
      title: {
        text: '各季度读书图例',
        left: 'center'
      },
      tooltip: {
        trigger: 'item'
      },
      legend: {
        top: '5%',
        left: 'center'
      },
      series: [
        {
          name: '读者数',
          type: 'pie',
          radius: ['40%', '70%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 10,
            borderColor: '#fff',
            borderWidth: 2
          },
          label: {
            show: false,
            position: 'center'
          },
          emphasis: {
            label: {
              show: true,
              fontSize: '40',
              fontWeight: 'bold'
            }
          },
          labelLine: {
            show: false
          },
          data: [] //填空
        }
      ]
    };

    //南丁格尔玫瑰图
    var coxcombOption = {
      legend: {
        top: 'bottom'
      },
      toolbox: {
        show: true,
        feature: {
          mark: {show: true},
          dataView: {show: true, readOnly: false},
          restore: {show: true},
          saveAsImage: {show: true}
        }
      },
      series: [
        {
          name: 'Nightingale Chart',
          type: 'pie',
          radius: [20, 100],
          center: ['50%', '50%'],
          roseType: 'area',
          itemStyle: {
            borderRadius: 8
          },
          data: []
        }
      ]
    };


    var chartDom = document.getElementById('main');
    var myChart = echarts.init(chartDom);

    var pieDom = document.getElementById('pie');
    var pieChart = echarts.init(pieDom);

    var coxcombDom = document.getElementById('coxcomb');
    var coxcombChart = echarts.init(coxcombDom);

    this.request.get("/echarts/members").then(res => {

      option.series[0].data = res.data
      option.series[1].data = res.data
      myChart.setOption(option);

      pieOption.series[0].data = [
        {name: "第一季度", value: res.data[0]},
        {name: "第二季度", value: res.data[1]},
        {name: "第三季度", value: res.data[2]},
        {name: "第四季度", value: res.data[3]}
      ]
      pieChart.setOption(pieOption);


      coxcombOption.series[0].data = [
        {name: "第一季度", value: res.data[0]},
        {name: "第二季度", value: res.data[1]},
        {name: "第三季度", value: res.data[2]},
        {name: "第四季度", value: res.data[3]}
      ]
      coxcombChart.setOption(coxcombOption);

    })

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

<style scoped>
.el-card{
  border-radius: 25px;
}
.i{
  padding: 20px;
}
</style>
