package cn.chad.post.service;

import cn.chad.post.domain.dto.ChannelDTO;
import cn.chad.post.domain.po.Channel;
import cn.chad.post.domain.vo.Result;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ChannelService extends IService<Channel> {
    Result findAll();

    Result deleteById(Integer id);

    Result saveChannel(ChannelDTO channel);

    Result updateChannel(Channel channel);
}
