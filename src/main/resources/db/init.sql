create table menu
(
    id          bigint auto_increment
        primary key,
    menu_name   varchar(30)                        null comment '菜单名称',
    pid         bigint   default 0                 null comment '父菜单id 0为根菜单',
    `desc`      varchar(50)                        null comment '描述',
    url         varchar(100)                       null comment '菜单url',
    is_leaf     int(1)   default 0                 null comment '是否叶子节点',
    create_time datetime default CURRENT_TIMESTAMP null comment '创建时间',
    modify_time datetime default CURRENT_TIMESTAMP null comment '修改时间',
    data_status int(1)   default 1                 null comment '状态（0:删除 1：有效）',
    icon        varchar(50)                        null comment '图标样式名'
)
    charset = utf8;

INSERT INTO menu (id, menu_name, pid, `desc`, url, is_leaf, create_time, modify_time, data_status, icon) VALUES (1, '系统管理', 0, '系统管理', '', 0, '2018-11-30 10:27:34', '1970-01-01 00:00:00', 1, 'am-icon-file');
INSERT INTO menu (id, menu_name, pid, `desc`, url, is_leaf, create_time, modify_time, data_status, icon) VALUES (2, '账号管理', 1, '账号管理', '/user/manage', 1, '2018-11-30 11:56:34', '1970-01-01 00:00:00', 1, 'am-icon-check');
INSERT INTO menu (id, menu_name, pid, `desc`, url, is_leaf, create_time, modify_time, data_status, icon) VALUES (3, '角色管理', 1, '角色管理', '/role/manage', 1, '2018-11-30 11:45:27', '1970-01-01 00:00:00', 1, 'am-icon-puzzle-piece');
INSERT INTO menu (id, menu_name, pid, `desc`, url, is_leaf, create_time, modify_time, data_status, icon) VALUES (4, '权限管理', 1, '权限管理', '/menu/manage', 1, '2018-11-30 15:13:38', '1970-01-01 00:00:00', 1, 'am-icon-bug');

create table role
(
    id          bigint auto_increment comment '权限角色ID'
        primary key,
    role_name   varchar(30)      not null comment '角色名称',
    `desc`      varchar(100)     null comment '描述',
    create_time datetime         null comment '创建时间',
    modify_time datetime         null comment '更新时间',
    data_status int(1) default 1 null comment '状态（0:删除 1：有效）'
)
    comment '角色表';

INSERT INTO role (id, role_name, `desc`, create_time, modify_time, data_status) VALUES (1, '系统管理员', '系统管理元12', '2019-09-18 14:18:35', '2020-03-15 16:59:31', 1);

create table role_menu
(
    id      bigint auto_increment comment '主键'
        primary key,
    role_id bigint not null comment '角色id',
    menu_id bigint not null comment '菜单id'
)
    comment '角色菜单关系表';

INSERT INTO role_menu (id, role_id, menu_id) VALUES (1, 1, 1);
INSERT INTO role_menu (id, role_id, menu_id) VALUES (2, 1, 2);
INSERT INTO role_menu (id, role_id, menu_id) VALUES (3, 1, 3);
INSERT INTO role_menu (id, role_id, menu_id) VALUES (4, 1, 4);

create table user
(
    id          bigint auto_increment comment 'ID'
        primary key,
    user_name   varchar(50)      not null comment '用户名称',
    nick_name   varchar(50)      not null comment '用户昵称',
    account     varchar(50)      not null comment '账号',
    password    varchar(250)     null comment '用户密码',
    phone       varchar(11)      not null comment '手机号',
    create_time datetime         not null comment '创建时间',
    modify_time datetime         not null comment '修改时间',
    data_status int(1) default 0 not null comment '状态（0：无效；1：有效）',
    `desc`      varchar(100)     null comment '描述'
)
    comment '用户表';

INSERT INTO user (id, user_name, nick_name, account, password, phone, create_time, modify_time, data_status, `desc`) VALUES (1, 'maple', 'maple', 'maple', '94a4936833302ddf6fe192eda7053a65', '18011111111', '2019-09-07 02:57:54', '2019-09-20 21:26:35', 1, null);

create table user_role
(
    id      bigint auto_increment comment '主键'
        primary key,
    user_id bigint not null comment '用户id',
    role_id bigint not null comment '角色id'
)
    comment '用户角色关系表';

INSERT INTO user_role (id, user_id, role_id) VALUES (1, 1, 1);