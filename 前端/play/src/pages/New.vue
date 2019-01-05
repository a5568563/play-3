<template>
    <div>
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
                                  <template slot="title"><img class="HeadImage" :src="headimage" alt=""/></template>
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
                <!--新闻内容-->
                <el-row type="flex" justify="center" class="new_content">
                    <el-col :span="16">
                        <el-card>
                            <template>
                              <div slot="header">
                                  <h2>{{news.title}}</h2>
                              </div>
                              <div v-html="news.article"></div>
                            </template>
                        </el-card>
                    </el-col>
                </el-row>

                <!--用户评论-->
                <template v-for="item in comment.slice((currentPage-1)*pagesize,currentPage*pagesize)">
                  <el-row type="flex" justify="center" v-bind:key="item.num" class="new_comment">
                    <el-col :span="14">
                      <el-card>
                        <template>
                          <el-row>
                            <el-col :span = "2">
                              <el-card>
                                <div class="hp">
                                  <img :src="item.headimage" width="100%" height="100%"/>
                                </div>
                                <div style="word-break: break-all;">
                                  {{item.nickname}}
                                </div>
                              </el-card>
                            </el-col>

                            <el-col :offset="1" :span = "20">
                              <el-card>
                                <div v-html="item.content"></div>
                              </el-card>
                            </el-col>
                          </el-row>
                        </template>
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

                <el-row type="flex" justify="center">
                    <el-col :span="18">
                        <el-card :body-style="{ padding: '0px' }">
                            <div slot="header">
                                <span>评论</span>
                            </div>
                            <!-- card body -->
                            <quill-editor ref="myTextEditor"
                                        v-model="content"
                                        :config="editorOption"
                                        @blur="onEditorBlur($event)"
                                        @focus="onEditorFocus($event)"
                                        @ready="onEditorReady($event)">
                            </quill-editor>
                            <el-button type="primary" @click="add_comment()" plain>评论</el-button>
                        </el-card>
                    </el-col>
                </el-row>
                <el-dialog
                  title="提示"
                  :visible.sync="comment_success"
                  width="30%"
                  @close="comment_refresh">
                  <el-row type="flex" justify="center">
                    <el-col :span="18">
                      <span>评论成功</span>
                    </el-col>
                  </el-row>
                  <span slot="footer">
                    <el-button @click="comment_success=false,comment_refresh()">取 消</el-button>
                    <el-button type="primary" @click="comment_success=false,comment_refresh()">确 定</el-button>
                  </span>
                </el-dialog>
            </el-main>
        </el-container>
    </div>
</template>
<style>
  body{
      background-color: rgb(236, 238, 241);
  }

  .new_content{
    margin-bottom: 20px;
  }

  .new_comment{
    margin-bottom: 20px;
  }
  .HeadImage{
    height: 48px;
    width: 48px;
  }
</style>
<script>
import { quillEditor } from 'vue-quill-editor'
export default {
  data () {
    return {
      headimage: '',
      activeIndex: '1',
      nid: 0,
      news: {
        title: '',
        article: '',
        ctr: 0,
        createtime: ''
      },
      // 评论
      comment: [],
      currentPage: 1,
      pagesize: 10,
      total: 0,

      content: '', // 评论内容(编辑器)
      comment_success: false,

      editorOption: {
        // something config
        modules: {
          toolbar: [
            ['bold', 'italic', 'underline', 'strike'], // toggled buttons
            ['blockquote', 'code-block']
          ]
        }
      }
    }
  },
  mounted () {
    var t = this
    if (t.$route.query.activeIndex) {
      t.activeIndex = t.$route.query.activeIndex
    }
    t.nid = t.$route.query.nid
    // 增加CTR
    this.$axios({
      method: 'post',
      url: this.$url + '/add/new_CTR',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + t.$getCookie('play_access_token')
      },
      data: {
        nid: t.nid
      }
    })
    // 载入头像
    t.headimage = t.$getCookie('user_headimage')
    // 载入新闻
    this.$axios({
      method: 'post',
      url: this.$url + '/get/new',
      data: {
        nid: t.nid
      }
    }).then(function (response) {
      console.log(response)
      t.news.title = response.data.data.data.title
      t.news.article = response.data.data.data.content
      t.news.ctr = response.data.data.data.ctr
      t.news.createtime = response.data.data.data.createtime
      t.comment_refresh()
    })
  },
  methods: {
    handleSelect (key, keyPath) {
      console.log(key, keyPath)
      var t = this
      if (key !== 0 && key !== '6-1' && key !== '6-2') {
        t.activeIndex = keyPath
        this.$router.push({
          path: '/main',
          query: {
            sid: key,
            activeIndex: key
          }
        })
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
      }
    },
    onEditorBlur (editor) {
      console.log('editor blur!', editor)
    },
    onEditorFocus (editor) {
      console.log('editor focus!', editor)
    },
    onEditorReady (editor) {
      console.log('editor ready!', editor)
    },
    onEditorChange ({ editor, html, text }) {
      // console.log('editor change!', editor, html, text)
      this.content = html
    },
    add_comment () {
      var t = this
      t.$axios({
        method: 'post',
        url: this.$url + '/addcomment',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + t.$getCookie('play_access_token')
        },
        data: {
          nid: t.nid,
          content: t.content
        }
      }).then(function (response) {
        t.comment_success = true
      })
    },
    comment_refresh () {
      var t = this
      t.$axios({
        method: 'post',
        url: t.$url + '/get/comment',
        data: {
          nid: t.nid
        }
      }).then(function (response) {
        console.log(response)
        var length = response.data.totalRows
        t.comment = []
        for (let index = 0; index < length; index++) {
          if (response.data.list[index].user_info.headimage === null) {
            response.data.list[index].user_info.headimage = 'default.png'
          }
          t.comment.push({
            content: response.data.list[index].content,
            createtime: response.data.list[index].createtime,
            headimage: t.$image_url + response.data.list[index].user_info.headimage,
            nickname: response.data.list[index].user_info.nickname
          })
        }
        t.total = length
      })
    },
    // 分页部分
    handleSizeChange (val) {
      this.pagesize = val
    },
    handleCurrentChange (val) {
      this.currentPage = val
    }
  },
  components: {
    quillEditor
  }
}
</script>
