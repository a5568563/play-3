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
                                  <template slot="title"><img class="HeadImage" :src="User.headimage" alt=""/></template>
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
                <el-col :span="12">
                  <el-card :body-style="{ padding: '20px' }">
                    <!-- card body -->
                    <el-tabs v-model="activename" @tab-click="TabChange">
                      <el-tab-pane label="用户信息" name="first">
                        <p style="float: left;">查看、修改你的账户基本资料</p>
                        <div style="clear: both;"></div>
                        <p style="float: left;">当前头像</p>
                        <!--
                          style="margin-left:20px; float: left;"
                        -->
                        <el-upload
                          class="avatar-uploader"
                          action="http://localhost:8081/user/setHeadImage"
                          :show-file-list="false"
                          style="margin:20px; float: left;"
                          :http-request="myUpload"
                          >
                          <img :src="User.headimage" width="120px" height="120px" class="avatar">
                        </el-upload>
                        <div style="clear: both;"></div>
                        <hr style="height:1px;border:none;border-top:1px dashed black;" />

                        <el-col :span=8>
                          <p style="float: left;">昵称</p>
                          <el-input v-model="User.nickname"  :disabled="true"></el-input>
                        </el-col>
                        <div style=" clear: both;"></div>
                        <hr style="height:1px;border:none;border-top:1px dashed black;" />

                        <p style="float: left;">关注游戏</p>
                        <el-col>
                          <el-switch
                            v-model="User.interest.LOL"
                            active-text="英雄联盟">
                          </el-switch>
                          <el-switch
                            v-model="User.interest.DOTA2"
                            active-text="DOTA2">
                          </el-switch>
                          <el-switch
                            v-model="User.interest.ROYAL"
                            active-text="王者荣耀">
                          </el-switch>
                        </el-col>
                        <div style=" clear: both;"></div>
                        <hr style="height:1px;border:none;border-top:1px dashed black;" />

                        <p style="float: left;">个性签名</p>
                        <el-input
                          type="textarea"
                          :rows="2"
                          placeholder="请输入内容"
                          v-model="User.detail">
                        </el-input>
                        <div style=" clear: both;"></div>

                        <el-button style="margin-top:25px;" type="info" @click="change_userinfo">保存</el-button>
                      </el-tab-pane>

                      <el-tab-pane label="更改密码" name="second">
                        <p style="float: left;">修改你的登录密码。</p>
                        <div style=" clear: both;"></div>

                        <el-form :rules="rule2" :model="password" ref="password" label-width="35%">
                          <el-col :span="13">
                            <el-form-item label="当前密码" prop="old_pwd">
                              <el-input type="password" v-model="password.old_pwd"></el-input>
                            </el-form-item>
                          </el-col>

                          <el-col :span="13">
                            <el-form-item label="请输入新密码" prop="new_pwd">
                              <el-input type="password" v-model="password.new_pwd"></el-input>
                            </el-form-item>
                          </el-col>

                          <el-col :span="13">
                            <el-form-item label="请重新输入一次新密码" prop="check_pwd">
                              <el-input type="password" v-model="password.check_pwd"></el-input>
                            </el-form-item>
                          </el-col>
                          <div style=" clear: both;"></div>
                          <el-col :span="18">
                            <el-form-item>
                              <el-button  type="info" @click="change_pwd('password')">保存</el-button>
                            </el-form-item>
                          </el-col>
                        </el-form>
                      </el-tab-pane>
                    </el-tabs>
                    <el-dialog
                      title="提示"
                      :visible.sync="hasErrors"
                      width="20%">
                      <span>{{errorList.name}}</span>
                      <span slot="footer">
                        <el-button type="primary" @click="hasErrors = false">确 定</el-button>
                        <el-button @click="hasErrors = false">取 消</el-button>
                      </span>
                    </el-dialog>
                  </el-card>
                </el-col>
              </el-row>
            </el-main>
        </el-container>
    </div>
</template>
<style>
  body{
    background-color: rgb(236, 238, 241);
  }
  .user{
    margin: 30px;
    border: 1px;
  }

  .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .HeadImage{
    height: 48px;
    width: 48px;
  }
</style>
<script>
import md5 from 'js-md5'
export default {
  mounted () {
    var t = this
    if (t.$route.query.activeIndex) {
      t.activeIndex = t.$route.query.activeIndex
    }
    t.$axios({
      method: 'post',
      url: this.$url + '/user/getInfo',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + t.$getCookie('play_access_token')
      }
    }).then(function (response) {
      console.log(response)
      t.User.nickname = response.data.data.info.nickname
      t.User.detail = response.data.data.info.detail
      if (response.data.data.info.headimage !== null) {
        t.User.headimage = t.$image_url + response.data.data.info.headimage
      }
      if (response.data.data.info.interest !== null) {
        t.set_interest(response.data.data.info.interest)
      }
    })
  },
  data () {
    var samepwd = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请再次输入新密码'))
      } else if (!this.isSamePwd(value)) {
        callback(new Error('两次输入密码不一样'))
      } else if (this.oldnewsame(value)) {
        callback(new Error('新密码不能和旧密码一样'))
      } else {
        callback()
      }
    }
    var checkoldpwd = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入旧密码'))
      } else if (!this.Checkpwd(value)) {
        callback(new Error('旧密码输入错误'))
      } else {
        callback()
      }
    }
    return {
      ready: false,
      hasErrors: false,
      errorList: {},
      activeIndex: '1',
      activename: 'first',
      User: {
        nickname: '昵称',
        detail: '我的个性签名',
        headimage: this.$image_url + 'default.png',
        interest: {
          LOL: true,
          DOTA2: false,
          ROYAL: false
        }
      },
      password: {
        old_pwd: '',
        new_pwd: '',
        check_pwd: ''
      },
      rule2: {
        old_pwd: [
          {required: true, trigger: 'blur', validator: checkoldpwd}
        ],
        new_pwd: [
          {required: true, message: '请输入新密码', trigger: 'blur'},
          {min: 6, max: 20, message: '密码长度在6-20之间', trigger: 'blur'}
        ],
        check_pwd: [
          {required: true, trigger: 'blur', validator: samepwd}
        ]
      }
    }
  },
  methods: {
    TabChange (tab, event) {
      console.log(tab, event)
    },
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
    set_interest (val) {
      var s = val.split(';')
      var t = this
      var length = s.length
      for (var i = 0; i < length; i++) {
        if (s[i] === '1') {
          t.User.interest.LOL = true
        }
        if (s[i] === '2') {
          t.User.interest.ROYAL = true
        }
        if (s[i] === '3') {
          t.User.interest.DOTA2 = true
        }
      }
    },
    myUpload (content) {
      var t = this
      var form = new FormData()
      form.append('file', content.file)
      t.$axios({
        method: 'post',
        url: content.action,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + t.$getCookie('play_access_token')
        },
        data: form
      }).then(function (response) {
        t.User.headimage = t.$image_url + response.data.data.filename
        t.$setCookie('user_headimage', t.User.headimage)
      })
    },
    isSamePwd (pwd) {
      if (pwd === this.password.new_pwd) {
        return true
      } else {
        return false
      }
    },
    oldnewsame (pwd) {
      if (pwd === this.password.old_pwd) {
        return true
      } else {
        return false
      }
    },
    Checkpwd (pwd) {
      var t = this
      t.$axios({
        method: 'get',
        url: this.$url + '/user/valid_psw',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + t.$getCookie('play_access_token')
        },
        params: {
          password: md5(pwd)
        }
      }).then(function (response) {
        console.log(response)
        if (response.data === 'success') {
          t.ready = true
        } else {
          t.ready = false
        }
      })
      return t.ready
    },
    change_pwd (formName) {
      console.log(this.$refs[formName])
      var t = this
      var pwd = this.$refs[formName].model.new_pwd
      this.$refs[formName].validate((valid) => {
        if (valid) {
          t.$axios({
            method: 'get',
            url: this.$url + '/user/change_psw',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': 'Bearer ' + t.$getCookie('play_access_token')
            },
            params: {
              password: md5(pwd)
            }
          }).then(function (response) {
            if (response.data === 'success') {
              t._showErrors('修改成功')
            }
          })
        } else {
          this._showErrors('填写完整')
          return false
        }
      })
    },
    /* User: {
        nickname: '昵称',
        detail: '我的个性签名',
        headimage: this.$image_url + 'default.png',
        interest: {
          LOL: true,
          DOTA2: false,
          ROYAL: false
        }
      }, */
    change_userinfo () {
      var t = this
      t.$axios({
        method: 'get',
        url: this.$url + '/user/change_userinfo',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': 'Bearer ' + t.$getCookie('play_access_token')
        },
        params: {
          nickname: t.User.nickname,
          detail: t.User.detail,
          headimage: t.get_headimage(),
          interest: t.get_interest()
        }
      }).then(function (response) {
        if (response.data === 'success') {
          t._showErrors('保存成功')
        }
      })
    },
    get_interest () {
      var t = this
      var str = ''
      if (t.User.interest.LOL === true) {
        str = str + '1;'
      }
      if (t.User.interest.ROYAL === true) {
        str = str + '2;'
      }
      if (t.User.interest.DOTA2 === true) {
        str = str + '3;'
      }
      return str
    },
    get_headimage () {
      var t = this
      var str = t.User.headimage.replace(t.$image_url, '')
      return str
    },
    _showErrors (errorname) {
      if (errorname !== '') {
        this.hasErrors = true
        this.errorList = {}
        this.errorList.name = errorname
      } else {
        this.hasErrors = false
        this.errorList = {}
      }
    }
  }
}
</script>
