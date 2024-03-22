package com.hmdp.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hmdp.dto.Result;
import com.hmdp.entity.ShopType;
import com.hmdp.mapper.ShopTypeMapper;
import com.hmdp.service.IShopTypeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements IShopTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public Result queryTypeList() {
        //1.从redis中查询菜单类型缓存
        String shopTypeJson = stringRedisTemplate.opsForList().index("cache:type-list", 0);
        if (StrUtil.isNotBlank(shopTypeJson)) {
            //3.存在，直接返回
            List<ShopType> list = JSONUtil.toList(shopTypeJson, ShopType.class);
            return Result.ok(list);
        }

        //4.不存在，查询数据库
        List<ShopType> list = query().orderByAsc("sort").list();
        //5.不存在，返回错误
        if (list == null) {
            return Result.fail("类型不存在！");
        }

        //6.存在，写入redis
        stringRedisTemplate.opsForList().rightPush("cache:type-list", JSONUtil.toJsonStr(list));
        //7.返回
        return Result.ok(list);
    }
}
