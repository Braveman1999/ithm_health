package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.common.RedisConst;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ：seanyang
 * @date ：Created in 2019/6/10
 * @description ：
 * @version: 1.0
 */
@Service
public class SetmealServiceImpl implements SetmealService {
	@Autowired
	private JedisPool jedisPool;

	@Autowired
	private SetmealDao setmealDao;

	@Transactional
	@Override
	public void add(Setmeal setmeal, Integer[] checkgroupIds) {
		// 保存套餐数据
		setmealDao.add(setmeal);
		// 保存套餐与检查组对应关系
		for (Integer checkGoupId:checkgroupIds){
			Map<String,Integer> map = new HashMap<>();
			map.put("setmeal_id",setmeal.getId());
			map.put("checkgroup_id",checkGoupId);
			setmealDao.addSetmealAndCheckGroup(map);
		}
		jedisPool.getResource().sadd(RedisConst.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
	}

	@Override
	public PageResult pageQuery(Integer currentPage, Integer pageSize, String queryString) {
		PageHelper.startPage(currentPage,pageSize);
		Page<Setmeal> pageSetmeal = setmealDao.selectByCondition(queryString);
		return new PageResult(pageSetmeal.getTotal(),pageSetmeal.getResult());
	}

	@Override
	public List<Setmeal> findAll() {
		return setmealDao.findAll();
	}

	@Override
	public Setmeal findById(Integer id) {
		return setmealDao.findById(id);
	}



}
