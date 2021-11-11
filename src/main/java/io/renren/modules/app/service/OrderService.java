package io.renren.modules.app.service;

import com.baomidou.mybatisplus.extension.service.IService;
import io.renren.modules.app.entity.OrderEntity;

import java.util.ArrayList;
import java.util.HashMap;

public interface OrderService extends IService<OrderEntity> {
    public ArrayList<OrderEntity> searchUserOrderList(HashMap map);
}
