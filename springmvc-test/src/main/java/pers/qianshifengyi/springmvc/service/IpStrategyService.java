package pers.qianshifengyi.springmvc.service;

import pers.qianshifengyi.springmvc.model.IpStrategy;

import java.util.List;

/**
 * Created by zhangshan on 17/4/20.
 */
public interface IpStrategyService {

    int deleteByPrimaryKey(String id);

    int insert(IpStrategy record);

    int insertSelective(IpStrategy record);

    IpStrategy selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(IpStrategy record);

    int updateByPrimaryKey(IpStrategy record);

    List<String> getAllDisabledIps();

}
