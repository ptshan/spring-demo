package pers.qianshifengyi.springmvc.mapper;

import pers.qianshifengyi.springmvc.model.IpStrategy;
import pers.qianshifengyi.springmvc.util.MyBatisRepository;

import java.util.List;

@MyBatisRepository
public interface IpStrategyMapper {

    int deleteByPrimaryKey(String id);

    int insert(IpStrategy record);

    int insertSelective(IpStrategy record);

    IpStrategy selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(IpStrategy record);

    int updateByPrimaryKey(IpStrategy record);

    List<String> getAllDisabledIps();
}