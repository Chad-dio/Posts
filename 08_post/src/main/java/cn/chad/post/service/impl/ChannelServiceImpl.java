package cn.chad.post.service.impl;

import cn.chad.post.domain.dto.ChannelDTO;
import cn.chad.post.domain.po.Channel;
import cn.chad.post.domain.vo.Result;
import cn.chad.post.mapper.ChannelMapper;
import cn.chad.post.service.ChannelService;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class ChannelServiceImpl extends ServiceImpl<ChannelMapper, Channel> implements ChannelService {
    @Override
    public Result findAll() {
        LambdaQueryWrapper<Channel> wrapper = new LambdaQueryWrapper<>();
        //只查询启用的频道
        wrapper.eq(Channel::getStatus, 1);
        return Result.success(list(wrapper));
    }

    @Override
    public Result deleteById(Integer id) {
        LambdaUpdateWrapper<Channel> wrapper = new LambdaUpdateWrapper<>();
        wrapper.set(Channel::getStatus, 0)
                .eq(Channel::getId, id);
        boolean update = update(wrapper);
        if(!update){
            return Result.error("删除失败");
        }
        return Result.success("删除成功");
    }

    @Override
    public Result saveChannel(ChannelDTO channelDTO) {
        LambdaQueryWrapper<Channel> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Channel::getName, channelDTO.getName());
        Channel c = getOne(wrapper);
        if(BeanUtil.isNotEmpty(c)){
            return Result.error("存在相同名字的频道");
        }
        Channel channel = BeanUtil.copyProperties(channelDTO, Channel.class);
        channel.setCreatedTime(new Date());
        boolean f = save(channel);
        if(!f){
            return Result.error("添加失败");
        }
        return Result.success("添加成功");
    }
}
