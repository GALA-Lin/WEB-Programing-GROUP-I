-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS web_project_db
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

-- 选择要使用的数据库
USE web_project_db;

-- -----------------------------------------------------
-- Table `users` (用户信息表)
-- -----------------------------------------------------
CREATE TABLE `users` (
                         `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '用户唯一ID (主键)',
                         `student_id` VARCHAR(50) NULL UNIQUE COMMENT '学号 (唯一, 可为空)',
                         `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名 (登录使用, 必须唯一)',
                         `password` VARCHAR(255) NOT NULL COMMENT '加密后的密码',
                         `real_name` VARCHAR(50) NULL COMMENT '真实姓名',
                         `email` VARCHAR(100) NULL UNIQUE COMMENT '电子邮箱 (可用于找回密码, 保持唯一)',
                         `phone_number` VARCHAR(20) NULL UNIQUE COMMENT '手机号码 (唯一)',
                         `avatar_url` VARCHAR(255) NULL COMMENT '用户头像图片URL',
                         `role` VARCHAR(20) NOT NULL DEFAULT 'volunteer' COMMENT '用户角色 (volunteer, admin, super_admin)',
                         `total_service_hours` DECIMAL(10, 2) NOT NULL DEFAULT 0.00 COMMENT '累计志愿服务总时长',
                         `status` TINYINT NOT NULL DEFAULT 1 COMMENT '账户状态 (1:正常, 0:禁用)',
                         `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
                         `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';

-- -----------------------------------------------------
-- Table `organizations` (组织部门表)
-- -----------------------------------------------------
CREATE TABLE `organizations` (
                                 `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '组织唯一ID (主键)',
                                 `name` VARCHAR(100) NOT NULL UNIQUE COMMENT '组织名称 (必须唯一)',
                                 `description` TEXT NULL COMMENT '组织描述',
                                 `leader_id` BIGINT UNSIGNED NULL COMMENT '负责人用户ID (外键关联 users.id)',
                                 `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
                                 `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
                                 PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织部门表';

-- -----------------------------------------------------
-- Table `organization_members` (组织成员关系表)
-- -----------------------------------------------------
CREATE TABLE `organization_members` (
                                        `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
                                        `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID (外键)',
                                        `organization_id` BIGINT UNSIGNED NOT NULL COMMENT '组织ID (外键)',
                                        `join_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入或申请日期',
                                        `member_role` VARCHAR(20) NOT NULL DEFAULT 'member' COMMENT '在组织中的角色 (如: member, officer)',
                                        `status` VARCHAR(20) NOT NULL DEFAULT 'approved' COMMENT '成员状态 (pending:待审批, approved:已批准)',
                                        PRIMARY KEY (`id`),
                                        UNIQUE INDEX `uq_user_organization` (`user_id`, `organization_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='组织成员关系表';

-- -----------------------------------------------------
-- Table `activities` (志愿活动表)
-- -----------------------------------------------------
CREATE TABLE `activities` (
                              `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '活动唯一ID (主键)',
                              `title` VARCHAR(255) NOT NULL COMMENT '活动标题',
                              `description` TEXT NULL COMMENT '活动详细描述',
                              `cover_image_url` VARCHAR(255) NULL COMMENT '活动封面图片URL',
                              `category` VARCHAR(50) NULL COMMENT '活动分类',
                              `location` VARCHAR(255) NULL COMMENT '活动地点',
                              `start_time` DATETIME NOT NULL COMMENT '活动开始时间',
                              `end_time` DATETIME NOT NULL COMMENT '活动结束时间',
                              `organizer_id` BIGINT UNSIGNED NULL COMMENT '主办方组织ID (外键关联 organizations.id)',
                              `recruitment_quota` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '招募人数上限 (0为不限)',
                              `current_enrollment` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '当前报名人数',
                              `status` VARCHAR(20) NOT NULL DEFAULT 'recruiting' COMMENT '活动状态 (recruiting:招募中, ongoing:进行中, finished:已结束, canceled:已取消)',
                              `created_by` BIGINT UNSIGNED NULL COMMENT '创建活动的管理员ID (外键关联 users.id)',
                              `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录创建时间',
                              `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录更新时间',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='志愿活动信息表';

-- -----------------------------------------------------
-- Table `enrollments` (报名记录表)
-- -----------------------------------------------------
CREATE TABLE `enrollments` (
                               `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '报名记录唯一ID (主键)',
                               `user_id` BIGINT UNSIGNED NOT NULL COMMENT '报名的用户ID (外键关联 users.id)',
                               `activity_id` BIGINT UNSIGNED NOT NULL COMMENT '报名的活动ID (外键关联 activities.id)',
                               `status` VARCHAR(20) NOT NULL DEFAULT 'enrolled' COMMENT '报名状态 (enrolled:已报名, canceled:已取消)',
                               `enrolled_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '报名时间',
                               PRIMARY KEY (`id`),
                               UNIQUE INDEX `uq_user_activity` (`user_id`, `activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='活动报名记录表';

-- -----------------------------------------------------
-- Table `service_records` (服务时长记录表)
-- -----------------------------------------------------
CREATE TABLE `service_records` (
                                   `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '时长记录唯一ID (主键)',
                                   `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID (外键关联 users.id)',
                                   `activity_id` BIGINT UNSIGNED NOT NULL COMMENT '活动ID (外键关联 activities.id)',
                                   `service_hours` DECIMAL(10, 2) NOT NULL COMMENT '本次活动记录的服务时长',
                                   `record_method` VARCHAR(20) NOT NULL DEFAULT 'manual' COMMENT '时长录入方式 (manual:手动, auto:自动签到)',
                                   `recorded_by` BIGINT UNSIGNED NULL COMMENT '录入该记录的管理员ID (外键关联 users.id)',
                                   `remarks` VARCHAR(500) NULL COMMENT '备注信息',
                                   `recorded_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
                                   PRIMARY KEY (`id`),
                                   UNIQUE INDEX `uq_user_activity` (`user_id`, `activity_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='志愿服务时长记录表';

-- -----------------------------------------------------
-- Table `news` (新闻资讯表)
-- -----------------------------------------------------
CREATE TABLE `news` (
                        `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '新闻唯一ID (主键)',
                        `title` VARCHAR(255) NOT NULL COMMENT '新闻标题',
                        `summary` TEXT NULL COMMENT '新闻摘要 (可由AI生成)',
                        `content` TEXT NOT NULL COMMENT '新闻正文内容',
                        `author_id` BIGINT UNSIGNED NULL COMMENT '发布新闻的管理员ID (外键关联 users.id)',
                        `likes_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '点赞数',
                        `comments_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '评论数',
                        `favorites_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '收藏数',
                        `published_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '发布时间',
                        `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='新闻资讯表';

-- =====================================================
-- 外键约束定义 (Foreign Key Constraints)
-- =====================================================

-- 为organizations表添加外键约束：leader_id关联users.id
ALTER TABLE `organizations`
    ADD CONSTRAINT `fk_org_leader`
        FOREIGN KEY (`leader_id`) REFERENCES `users` (`id`)
            ON DELETE SET NULL ON UPDATE CASCADE;

-- 为organization_members表添加两个外键约束
ALTER TABLE `organization_members`
    ADD CONSTRAINT `fk_member_user`
        FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
            ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT `fk_member_org`
        FOREIGN KEY (`organization_id`) REFERENCES `organizations` (`id`)
            ON DELETE CASCADE ON UPDATE CASCADE;

-- 为activities表添加两个外键约束
ALTER TABLE `activities`
    ADD CONSTRAINT `fk_act_organizer`
        FOREIGN KEY (`organizer_id`) REFERENCES `organizations` (`id`)
            ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT `fk_act_creator`
        FOREIGN KEY (`created_by`) REFERENCES `users` (`id`)
            ON DELETE SET NULL ON UPDATE CASCADE;

-- 为enrollments表添加两个外键约束
ALTER TABLE `enrollments`
    ADD CONSTRAINT `fk_enroll_user`
        FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
            ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT `fk_enroll_activity`
        FOREIGN KEY (`activity_id`) REFERENCES `activities` (`id`)
            ON DELETE CASCADE ON UPDATE CASCADE;

-- 为service_records表添加三个外键约束
ALTER TABLE `service_records`
    ADD CONSTRAINT `fk_record_user`
        FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
            ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT `fk_record_activity`
        FOREIGN KEY (`activity_id`) REFERENCES `activities` (`id`)
            ON DELETE CASCADE ON UPDATE CASCADE,
    ADD CONSTRAINT `fk_record_recorder`
        FOREIGN KEY (`recorded_by`) REFERENCES `users` (`id`)
            ON DELETE SET NULL ON UPDATE CASCADE;

-- 为news表添加外键约束
ALTER TABLE `news`
    ADD CONSTRAINT `fk_news_author`
        FOREIGN KEY (`author_id`) REFERENCES `users` (`id`)
            ON DELETE SET NULL ON UPDATE CASCADE;