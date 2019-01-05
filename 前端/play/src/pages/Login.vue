<template>
  <div>
    <el-container >
      <el-main>
        <!-- Main content -->
        <el-row type="flex" justify="center">
          <el-col :span="6">
            <el-card class="login-card">
              <div slot="header">
                <span>Play电竞</span>
              </div>
              <!-- card body -->
              <!-- 登录 -->
              <el-form v-show="login_status" :model="loginform" ref="loginform" label-width="80px">
                <el-form-item label="用户名">
                  <el-input v-model="loginform.username"></el-input>
                </el-form-item>
                <el-form-item label="密码">
                  <el-input type="password" v-model="loginform.password"></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="login()" style="margin-right: 40px">登陆</el-button>
                  <el-button type="primary" @click="ChangeToSignUp()" style="margin-right: 50px">注册</el-button>
                </el-form-item>
              </el-form>
              <!-- 注册 -->
              <el-form  v-show="signup_status" :model="SignUpform" :rules="rules" ref="SignUpform" label-width="80px">
                <el-form-item label="昵称" prop="nickname">
                  <el-input v-model="SignUpform.nickname"></el-input>
                </el-form-item>
                <el-form-item label="用户名" prop="username">
                  <el-input v-model="SignUpform.username"></el-input>
                </el-form-item>
                <el-form-item label="密码" prop="password">
                  <el-input type="password" v-model="SignUpform.password"></el-input>
                </el-form-item>
                <el-form-item label="电话号" prop="telephone">
                  <el-input v-model="SignUpform.telephone"></el-input>
                </el-form-item>
                <el-form-item label="关注游戏">
                  <el-switch
                    v-model="SignUpform.interest.LOL"
                    active-text="英雄联盟">
                  </el-switch>
                  <el-switch
                    v-model="SignUpform.interest.ROYAL"
                    active-text="王者荣耀">
                  </el-switch>
                  <el-switch
                    v-model="SignUpform.interest.DOTA2"
                    active-text="DOTA2">
                  </el-switch>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="signup()" style="margin-right: 40px">注册</el-button>
                  <el-button type="primary" @click="ChangeToLogin()" style="margin-right: 50px">取消</el-button>
                </el-form-item>
              </el-form>
            </el-card>
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

  .login-card {
    width: 480px
  }
</style>
<script>
import md5 from 'js-md5'
// 手机验证
function isvalidPhone (str) {
  const reg = /^1[3|4|5|7|8][0-9]\d{8}$/
  return reg.test(str)
}
// 用户名验证
function usernameCheck (str) {
  const reg = /^[a-zA-Z]([-_a-zA-Z0-9]{5,19})+$/
  return reg.test(str)
}

var validPhone = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入电话号码'))
  } else if (!isvalidPhone(value)) {
    callback(new Error('请输入正确的11位手机号码'))
  } else {
    callback()
  }
}
var validUsername = (rule, value, callback) => {
  if (!value) {
    callback(new Error('请输入用户名'))
  } else if (!usernameCheck(value)) {
    callback(new Error('用户名为字母开头长度不少于6的字符串'))
  } else {
    callback()
  }
}
export default{
  data () {
    return {
      // status
      login_status: true,
      signup_status: false,
      hasErrors: false,
      errorList: {},
      // 注册
      SignUpform: {
        nickname: '',
        username: '',
        password: '',
        telephone: '',
        interest: {
          LOL: true,
          ROYAL: false,
          DOTA2: false
        }
      },
      rules: {
        nickname: [
          {required: true, message: '请输入昵称', trigger: 'blur'},
          {min: 2, max: 8, message: '昵称长度在2-8之间', trigger: 'blur'}
        ],
        username: [
          {required: true, trigger: 'blur', validator: validUsername}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 6, max: 20, message: '密码长度在6-20之间', trigger: 'blur'}
        ],
        telephone: [
          {required: true, trigger: 'blur', validator: validPhone}
        ]
      },
      // 登录
      loginform: {
        username: '',
        password: ''
      }
    }
  },
  methods: {
    // 切换
    ChangeToSignUp () {
      this.login_status = false
      this.signup_status = true
    },
    ChangeToLogin () {
      this.signup_status = false
      this.login_status = true
    },
    get_interest () {
      var str = ''
      var t = this
      if (t.SignUpform.interest.LOL) {
        str = str + '1;'
      }
      if (t.SignUpform.interest.ROYAL) {
        str = str + '2;'
      }
      if (t.SignUpform.interest.DOTA2) {
        str = str + '3;'
      }
      return str
    },
    // 注册
    signup () {
      var t = this
      if (this.SignUpform) {
        if (this.SignUpform.username !== '' && this.SignUpform.password !== '' && this.SignUpform.telephone !== '') {
          this.$axios({
            method: 'post',
            url: this.$url + '/register',
            data: {
              nickname: this.SignUpform.nickname,
              username: this.SignUpform.username,
              password: md5(this.SignUpform.password),
              telephone: this.SignUpform.telephone,
              interest: this.get_interest()
            }
          }).then(function (response) {
            console.log(response)
            if (response.data === 'success') {
              t._showErrors('注册成功')
            } else if (response.data === '用户名已存在') {
              t._showErrors('用户名已存在')
            }
          })
        } else {
          this._showErrors('信息不全，请补全信息！')
        }
      }
    },
    // 登录
    login () {
      if (this.loginform) {
        if (this.loginform.username !== '' && this.loginform.password !== '') {
          var params = new URLSearchParams()
          console.log(this.loginform.password)
          params.append('grant_type', 'password')
          params.append('username', this.loginform.username)
          params.append('password', md5(this.loginform.password))

          this.$axios({
            method: 'post',
            url: this.$url + '/oauth/token',
            auth: {username: 'test', password: '123456'},
            headers: {'Content-type': 'application/x-www-form-urlencoded; charset=utf-8'},
            data: params
          }).then(function (response) {
            if (response.data && response.data.access_token) {
              this.$setCookie('play_access_token', response.data.access_token)
              this.$store.commit('userSignIn')
              this.$router.push({ name: 'Main', params: { page: 'main' } })
            }
          }.bind(this)).catch(function (error) {
            if (error.response.data.error === 'invalid_grant' || error.response.data.error === 'unauthorized') {
              console.log(error.response.data.error)
              this._showErrors('用户名或密码错误')
            }
          }.bind(this))
        } else {
          this._showErrors('信息不全，请补全信息！')
        }
      }
    },
    // 显示错误提示
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
