package io.renren.modules.app.controller;

import io.renren.common.utils.R;
import io.renren.common.validator.ValidatorUtils;
import io.renren.modules.app.annotation.Login;
import io.renren.modules.app.entity.OrderEntity;
import io.renren.modules.app.form.UserOrderForm;
import io.renren.modules.app.service.OrderService;
import io.renren.modules.app.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;

@RestController
@RequestMapping("/app/order")
@Api("订单业务接口")
public class OrderController {
    /**
     * 订单业务接口
     *
     * @author ze1sure99
     */
     @Autowired
     private OrderService orderService;
     @Autowired
     private JwtUtils jwtUtils;
     //登陆之后才能调用
     @Login
     @PostMapping("/searchUserOrderList")
     @ApiOperation("查询用户订单")
     //接收token 令牌字符串,反向计算得出用户id
     public R searchUserOrderList(@RequestBody UserOrderForm form , @RequestHeader HashMap header){
         ValidatorUtils.validateEntity(form);
         String token = header.get("token").toString();
         int userId = Integer.parseInt(jwtUtils.getClaimByToken(token).getSubject());
         int page = form.getPage();
         int length = form.getLength();
         int start= (page-1)*length;
         HashMap map = new HashMap();
         map.put("userId",userId);
         map.put("start",start);
         map.put("length",length);
         ArrayList<OrderEntity> list = orderService.searchUserOrderList(map);
         return R.ok().put("list",list);
     }

}

