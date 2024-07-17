package cn.chad.post.service.impl;

import cn.chad.post.domain.po.Channel;
import cn.chad.post.mapper.ChannelMapper;
import cn.chad.post.service.ChannelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ChannelServiceImpl extends ServiceImpl<ChannelMapper, Channel> implements ChannelService {
}
