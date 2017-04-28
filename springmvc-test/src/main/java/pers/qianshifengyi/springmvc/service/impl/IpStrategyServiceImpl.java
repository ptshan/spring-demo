package pers.qianshifengyi.springmvc.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pers.qianshifengyi.springmvc.mapper.IpStrategyMapper;
import pers.qianshifengyi.springmvc.model.IpStrategy;
import pers.qianshifengyi.springmvc.service.IpStrategyService;

import java.util.Collections;
import java.util.List;

/**
 * Created by zhangshan193 on 17/4/20.
 */
@Service
public class IpStrategyServiceImpl implements IpStrategyService {

    @Autowired
    private IpStrategyMapper ipStrategyMapper;

    @Override
    public int deleteByPrimaryKey(String id) {
        return 0;
    }

    @Override
    public int insert(IpStrategy record) {
        return 0;
    }

    @Override
    public int insertSelective(IpStrategy record) {
        return 0;
    }

    @Override
    public IpStrategy selectByPrimaryKey(String id) {
        return null;
    }

    @Override
    public int updateByPrimaryKeySelective(IpStrategy record) {
        return 0;
    }

    @Override
    public int updateByPrimaryKey(IpStrategy record) {
        return 0;
    }

    @Override
    public List<String> getAllDisabledIps() {
        List<String> ipList = ipStrategyMapper.getAllDisabledIps();
        return ipList == null ? Collections.emptyList() : ipList;
    }
}
