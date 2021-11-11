package io.renren.modules.app.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.entity.UserEntity;
import io.renren.modules.app.form.PayOrderForm;
import io.renren.modules.app.form.WxLoginForm;
import io.renren.modules.app.service.UserService;
import io.renren.modules.app.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/app/wx")
@Api("微信业务接口")
public class WxController {
    /**
     * APP登录授权
     *
     * @author ze1sure99
     */
       @Value("${application.app-id}")
       private String appId;
       @Value("${application.app-secret}")
       private String appSecret;
       @Autowired
       private UserService userService;
       @Autowired
       private JwtUtils jwtUtils;
        /**
         * 登录
         */
        @PostMapping("login")
        @ApiOperation("登录")
        public R login(@RequestBody WxLoginForm form){
            //表单校验
            ValidatorUtils.validateEntity(form);
            String url = "https://api.weixin.qq.com/sns/jscode2session";
            HashMap map = new HashMap();
            map.put("appid",appId);
            map.put("secret",appSecret);
            //form.getCode获取前端传过来的临时登陆凭证
            map.put("js_code",form.getCode());
            map.put("grant_type","authorization_code");

            //请求微信接口后微信平台返回的数据
            String response=HttpUtil.post(url,map);
            //响应数据
            JSONObject json=JSONUtil.parseObj(response);
            System.out.println(response);
            String openId=json.getStr("openid");
            if(openId==null||openId.length()==0){
                return R.error("临时登陆凭证错误");
            }
            UserEntity user = new UserEntity() ;
            user.setOpenId(openId);
            QueryWrapper wrapper = new QueryWrapper(user);
            //如果openID不存在,一般不存在,openid唯一
            int count = userService.count(wrapper);
            if(count==0){
                user.setNickname(form.getNickname());
                user.setPhoto(form.getPhoto());
                user.setType(2);
                user.setCreateTime(new Date());
                userService.save(user);
            }
            user = new UserEntity();
            user.setOpenId(openId);
            wrapper=new QueryWrapper(user);
            user=userService.getOne(wrapper);
            long id=user.getUserId();
            //生成token
            String token = jwtUtils.generateToken(id);
            //封装进hashMap
            Map<String,Object> result = new HashMap<>();
            result.put("token",token);
            result.put("expire",jwtUtils.getExpire());
            return R.ok(result);
        }

        @Login
        @PostMapping("/microAppPayOrder")
        @ApiOperation("小程序付款")
        //参数 请求头，请求表单json数据
        public R microAppPayOrder(@RequestBody PayOrderForm form, @RequestHeader HashMap header){
            ValidatorUtils.validateEntity(form);

            return  null;
        }

}

