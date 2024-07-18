package cn.chad.post.controller;

import cn.chad.post.domain.dto.ChannelDTO;
import cn.chad.post.domain.po.Channel;
import cn.chad.post.domain.vo.Result;
import cn.chad.post.service.ChannelService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@Api("频道相关接口")
@RestController
@RequestMapping("/channel")
public class ChannelController {

    @Resource
    private ChannelService channelService;
    @GetMapping("/findAll")
    @ApiOperation("查询全部的频道")
    public Result findAll(){
        return channelService.findAll();
    }

    @PostMapping("/add")
    @ApiOperation("添加新的频道")
    public Result saveChannel(@RequestBody ChannelDTO channelDTO){
        return channelService.saveChannel(channelDTO);
    }

    @DeleteMapping("/del/{id}")
    @ApiOperation("删除已有的频道")
    public Result delChannel(@PathVariable Integer id){
        return channelService.deleteById(id);
    }

    @PutMapping("/update")
    @ApiOperation("更新已有的频道")
    public Result updateChannel(@RequestBody Channel channel){
        return channelService.updateChannel(channel);
    }
}
