create database if not exists posts;
use posts;
create table if not exists `t_post` (
  `id` int(11) not null auto_increment comment '主键',
  `user_id` int(11) not null comment '用户ID',
  `title` varchar(36) not null comment '标题',
  `content` longtext comment '帖子内容',
  `type` tinyint(1) comment '帖子布局（0 无图文章，1 单图文章，3 多图文章）',
  `channel_id` int(11) comment '帖子频道ID',
  `labels` varchar(20) comment '标签',
  `created_time` datetime comment '创建时间',
  `submited_time` datetime comment '提交时间',
  `status` tinyint(2) comment '当前状态（0 草稿，1 提交待审核，2 审核失败，3 人工审核，4 人工审核通过，8 审核通过待发布，9 已发布）',
  `publish_time` datetime comment '定时发布时间，不定时则为空',
  `reason` varchar(50) comment '拒绝理由',
  `post_id` bigint(20) comment '帖子ID',
  `images` varchar(255) comment '图片素材用逗号分隔',
  `enable` tinyint(1) comment '是否启用',
  primary key (`id`)
) engine=innodb default charset=utf8mb4;

create table if not exists `t_channel` (
  `id` int(11) not null auto_increment comment '主键',
  `name` varchar(10) not null comment '频道名称',
  `description` varchar(255) comment '频道描述',
  `is_default` tinyint(1) not null default '0' comment '是否默认频道（1：默认, 0：非默认）',
  `status` tinyint(1) not null default '0' comment '是否启用（1：启用, 0：禁用）',
  `ord` int(11) not null comment '默认排序',
  `created_time` datetime not null comment '创建时间',
  primary key (`id`)
) engine=innodb default charset=utf8mb4;
