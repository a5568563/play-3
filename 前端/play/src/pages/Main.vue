<template>
    <div>
        <el-container>
            <el-container>
                <el-header style="position: fixed;top:0;width:100%;z-index:1;">
                    <!-- Header content -->
                    <el-row type="flex" justify="center">
                        <el-col :span="18">
                            <el-menu
                            :default-active="activeIndex"
                            class="el-menu-demo"
                            mode="horizontal"
                            @select="handleSelect"
                            background-color="#545c64"
                            text-color="#fff"
                            active-text-color="#ffd04b">
                                <el-menu-item index="0">首页</el-menu-item>
                                <el-menu-item index="1">英雄联盟</el-menu-item>
                                <el-menu-item index="2">王者荣耀</el-menu-item>
                                <el-menu-item index="3">DOTA2</el-menu-item>
                                <el-menu-item index="4">其他</el-menu-item>
                                <el-submenu index="6" style="float: right;">
                                  <template slot="title"><img class="HeadImage" :src="HeadImage" alt=""/></template>
                                    <el-menu-item index="6-1">我的信息</el-menu-item>
                                    <el-menu-item index="6-2">注销</el-menu-item>
                                </el-submenu>
                                <div style="clear: both;"></div>
                            </el-menu>
                        </el-col>
                    </el-row>
                </el-header>
                <el-main>
                    <!-- Main content -->
                    <el-row type="flex" justify="center">
                        <el-col :span="16">
                            <el-carousel :interval="4000" type="card" height="300px" >
                                <el-carousel-item v-for="item in 6" :key="item">
                                  <a :href="'/new?nid='+news[item].nid">
                                      <img :src="news[item].img" alt="" style="width:100%;height:100%;"/>
                                      <h4 style="position:absolute; left:25%; top: 75%; color: white;">{{news[item].title}}</h4>
                                  </a>
                                </el-carousel-item>
                            </el-carousel>
                        </el-col>
                    </el-row>
                    <el-row type="flex" justify="center">
                        <el-col :span="12">
                            <h2>赛事新闻</h2>
                        </el-col>
                    </el-row>
                    <template v-for="item in news.slice((currentPage-1)*pagesize,currentPage*pagesize)">
                        <el-row class="news" type="flex" justify="center" :key="item.num">
                            <el-col :span="12">
                                <el-card shadow="hover">
                                    <a :href="'/new?nid='+item.nid">
                                      <img class="news_img" :src="item.img" height="120px" width="180px" style="float: left;"/>
                                      <div style="clear: both;"></div>
                                      <span class="news_title" style="position: absolute;right:0%;top:50%;">{{item.title}}</span>
                                    </a>
                                    <span style="float: right">阅读量：{{item.ctr}}</span>
                                    <div style="clear: both;"></div>
                                </el-card>
                            </el-col>
                        </el-row>
                    </template>
                    <el-pagination
                      @size-change="handleSizeChange"
                      @current-change="handleCurrentChange"
                      :current-page="currentPage"
                      :page-sizes="[5, 10, 15, 20]"
                      :page-size="10"
                      layout="total, sizes, prev, pager, next, jumper"
                      :total="total">
                    </el-pagination>
                </el-main>
            </el-container>
        </el-container>
    </div>
</template>
<style>
  body{
      background-color: rgb(236, 238, 241);
  }

  .el-carousel__item h3 {
    color: #475669;
    font-size: 14px;
    opacity: 0.75;
    line-height: 200px;
    margin: 0;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
  }

  .el-carousel__item:nth-child(2n+1) {
    background-color: #d3dce6;
  }

  .news{
      padding-top: 25px;
  }

  .HeadImage{
    height: 48px;
    width: 48px;
  }
  a {
    font-size:16px
  }

  a:link {
    color: black;
    text-decoration: none
  }
    /* 未访问：蓝色、无下划线 */

  a:active {
    color: red
  } /* 激活：红色*/

  a:visited {
    color:black;
    text-decoration:none
  }

  a:hover {
    color: red;
    text-decoration: underline
  } /* 鼠标移近：红色、下划线 */

  .news_title{
    position: relative;
    left:10px;
    bottom: 50px;
  }
</style>
<script>
export default {
  mounted () {
    var t = this
    console.log(this.$store)
    if (t.$route.query.sid) {
      t.sid = t.$route.query.sid
    }
    if (t.$route.query.activeIndex) {
      t.activeIndex = t.$route.query.activeIndex
    }
    t.init(t.sid)
    this.$axios({
      method: 'get',
      url: this.$url + '/user/getHeadImage',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + t.$getCookie('play_access_token')
      }
    }).then(function (response) {
      console.log(response)
      t.HeadImage = t.$image_url + response.data
      t.$setCookie('user_headimage', t.HeadImage)
    })
  },
  data () {
    return {
      activeIndex: '1',
      HeadImage: '',
      news: [],
      currentPage: 1,
      pagesize: 10,
      total: 0,
      sid: 1
    }
  },
  methods: {
    handleSelect (key, keyPath) {
      console.log(key, keyPath)
      var t = this
      if (key !== '0' && key !== '6-1' && key !== '6-2') {
        t.activeIndex = keyPath
        t.init(key)
      } else if (key === '6-1') {
        t.$router.push({path: '/um',
          query: {
            activeIndex: keyPath[1]
          }})
      } else if (key === '6-2') {
        t.$clearCookie('play_access_token')
        t.$clearCookie('user_headimage')
        t.$store.commit('loginOut')
        t.$router.replace('/login')
      } else if (key === '0') {
        t.recommond()
      }
    },
    handleSizeChange (val) {
      this.pagesize = val
    },
    handleCurrentChange (val) {
      this.currentPage = val
    },
    init (val) {
      var t = this
      t.news = []
      t.currentPage = 1
      t.$axios({
        method: 'post',
        url: this.$url + '/get/titlelist',
        data: {
          sid: val,
          pagenum: 1
        }
      }).then(function (response) {
        console.log(response)
        var length = response.data.data.nid_page.totalRows
        for (let index = 0; index < length; index++) {
          t.news.push({
            title: response.data.data.title_page.list[index],
            img: response.data.data.img_page.list[index],
            nid: response.data.data.nid_page.list[index],
            ctr: response.data.data.ctr_page.list[index]
          })
        }
        t.total = length
      })
    },
    recommond () {
      var t = this
      t.news = []
      t.currentPage = 1
      t.$axios({
        method: 'post',
        url: this.$url + '/get/Recommend',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + t.$getCookie('play_access_token')
        }
      }).then(function (response) {
        console.log(response)
        var length = response.data.data.data.totalRows
        for (let index = 0; index < length; index++) {
          t.news.push({
            title: response.data.data.data.list[index].title,
            img: response.data.data.data.list[index].title_picture,
            nid: response.data.data.data.list[index].id,
            ctr: response.data.data.data.list[index].ctr
          })
        }
        t.total = length
      })
    }
  }
}
</script>
