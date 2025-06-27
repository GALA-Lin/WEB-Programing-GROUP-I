-- 油炬智愿 - 数据库表结构设计 V1
-- 表结构
-- Creation Date: 2025-06-27

-- -----------------------------------------------------
-- Table `users` (用户信息表)
-- 存储所有系统用户，包括志愿者、管理员和超级管理员
-- -----------------------------------------------------
CREATE TABLE `users` (
                         `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户唯一ID (主键)',
                         `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名 (登录使用, 必须唯一)',
                         `password` VARCHAR(255) NOT NULL COMMENT '加密后的密码',
                         `real_name` VARCHAR(50) NULL COMMENT '真实姓名',
                         `email` VARCHAR(100) NULL UNIQUE COMMENT '电子邮箱 (可用于找回密码, 保持唯一)',
                         `phone_number` VARCHAR(20) NULL UNIQUE COMMENT '手机号码 (唯一)',
                         `role` VARCHAR(20) NOT NULL DEFAULT 'volunteer' COMMENT '用户角色 (volunteer, admin, super_admin)',
                         `total_service_hours` DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '累计志愿服务总时长',
                         `status` TINYINT NOT NULL DEFAULT 1 COMMENT '账户状态 (1:正常, 0:禁用)',
                         `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
                         `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- -----------------------------------------------------
-- Table `organizations` (组织部门表)
-- 存储学校内的志愿组织或部门，如青年志愿者协会、学院分队等
-- -----------------------------------------------------
CREATE TABLE `organizations` (
                                 `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '组织唯一ID (主键)',
                                 `name` VARCHAR(100) NOT NULL UNIQUE COMMENT '组织名称 (必须唯一)',
                                 `description` TEXT NULL COMMENT '组织描述',
                                 `leader_id` BIGINT UNSIGNED NULL COMMENT '负责人用户ID (外键关联 users.id)',
                                 `member_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '组织成员数量',
                                 `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
                                 `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
                                 PRIMARY KEY (`id`),
                                 INDEX `idx_leader_id` (`leader_id` ASC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织部门表';

-- -----------------------------------------------------
-- Table `activities` (志愿活动表)
-- 存储所有志愿活动的详细信息
-- -----------------------------------------------------
CREATE TABLE `activities` (
                              `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '活动唯一ID (主键)',
                              `title` VARCHAR(255) NOT NULL COMMENT '活动标题',
                              `description` TEXT NULL COMMENT '活动详细描述 (可由AI生成)',
                              `location` VARCHAR(255) NULL COMMENT '活动地点',
                              `start_time` DATETIME NOT NULL COMMENT '活动开始时间',
                              `end_time` DATETIME NOT NULL COMMENT '活动结束时间',
                              `organizer_id` BIGINT UNSIGNED NOT NULL COMMENT '主办方组织ID (外键关联 organizations.id)',
                              `recruitment_quota` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '招募人数上限 (0为不限)',
                              `current_enrollment` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '当前报名人数',
                              `status` VARCHAR(20) NOT NULL DEFAULT 'recruiting' COMMENT '活动状态 (recruiting:招募中, ongoing:进行中, finished:已结束, canceled:已取消)',
                              `created_by` BIGINT UNSIGNED NOT NULL COMMENT '创建活动的管理员ID (外键关联 users.id)',
                              `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
                              `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
                              PRIMARY KEY (`id`),
                              INDEX `idx_organizer_id` (`organizer_id` ASC),
                              INDEX `idx_start_time` (`start_time` ASC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='志愿活动信息表';

-- -----------------------------------------------------
-- Table `enrollments` (报名记录表)
-- 记录用户与活动之间的多对多关系，即报名情况
-- -----------------------------------------------------
CREATE TABLE `enrollments` (
                               `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '报名记录唯一ID (主键)',
                               `user_id` BIGINT UNSIGNED NOT NULL COMMENT '报名的用户ID (外键关联 users.id)',
                               `activity_id` BIGINT UNSIGNED NOT NULL COMMENT '报名的活动ID (外键关联 activities.id)',
                               `status` VARCHAR(20) NOT NULL DEFAULT 'enrolled' COMMENT '报名状态 (enrolled:已报名, canceled:已取消)',
                               `enrolled_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
                               PRIMARY KEY (`id`),
                               UNIQUE INDEX `uq_user_activity` (`user_id`, `activity_id`),
                               INDEX `idx_user_id` (`user_id` ASC),
                               INDEX `idx_activity_id` (`activity_id` ASC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动报名记录表';

-- -----------------------------------------------------
-- Table `service_records` (服务时长记录表)
-- 记录用户在某个活动中的具体服务时长
-- -----------------------------------------------------
CREATE TABLE `service_records` (
                                   `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '时长记录唯一ID (主键)',
                                   `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID (外键关联 users.id)',
                                   `activity_id` BIGINT UNSIGNED NOT NULL COMMENT '活动ID (外键关联 activities.id)',
                                   `service_hours` DECIMAL(10, 2) NOT NULL COMMENT '本次活动记录的服务时长',
                                   `record_method` VARCHAR(20) NOT NULL DEFAULT 'manual' COMMENT '时长录入方式 (manual:手动, auto:自动签到)',
                                   `recorded_by` BIGINT UNSIGNED NOT NULL COMMENT '录入该记录的管理员ID (外键关联 users.id)',
                                   `recorded_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
                                   PRIMARY KEY (`id`),
                                   INDEX `idx_user_id` (`user_id` ASC),
                                   INDEX `idx_activity_id` (`activity_id` ASC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='志愿服务时长记录表';

-- -----------------------------------------------------
-- Table `news` (新闻资讯表)
-- 存储由管理员发布的新闻、通知等
-- -----------------------------------------------------
CREATE TABLE `news` (
                        `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '新闻唯一ID (主键)',
                        `title` VARCHAR(255) NOT NULL COMMENT '新闻标题',
                        `content` TEXT NOT NULL COMMENT '新闻正文内容',
                        `author_id` BIGINT UNSIGNED NOT NULL COMMENT '发布新闻的管理员ID (外键关联 users.id)',
                        `likes_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '点赞数',
                        `comments_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '评论数',
                        `favorites_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '收藏数',
                        `published_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
                        `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        PRIMARY KEY (`id`),
                        INDEX `idx_author_id` (`author_id` ASC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='新闻资讯表';