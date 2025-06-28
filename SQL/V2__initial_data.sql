-- =====================================================
-- 油炬智愿 - 初始数据填充脚本 V1.0
-- 创建日期: 2025-06-28
-- 最后更新: 2025-06-28
-- =====================================================

-- 说明:
-- 1. 本脚本用于填充基础测试数据
-- 2. 执行前请确保已执行 V1__initial_schema.sql
-- 3. 所有密码使用BCrypt加密(明文123456)

-- ==================== 用户数据 ======================
-- 用户类型: 1管理员 + 2志愿者
INSERT INTO `users` (
    `id`, `student_id`, `username`, `password`,
    `real_name`, `email`, `phone_number`,
    `avatar_url`, `role`, `status`
) VALUES
      (1, 'G00001', 'admin', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S',
       '超级管理员', 'admin@youchu.com', '18800000001',
       'https://i.pravatar.cc/150?u=admin', 'super_admin', 1),

      (2, '20210101', 'zhangsan', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S',
       '张三', 'zhangsan@test.com', '18800000002',
       'https://i.pravatar.cc/150?u=zhangsan', 'volunteer', 1),

      (3, '20210102', 'lisi', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S',
       '李四', 'lisi@test.com', '18800000003',
       'https://i.pravatar.cc/150?u=lisi', 'volunteer', 1);

-- ================== 组织部门数据 ====================
INSERT INTO `organizations` (
    `id`, `name`, `description`, `leader_id`
) VALUES
      (1, '校青年志愿者协会',
       '中国石油大学（北京）克拉玛依校区官方最大的志愿者组织。', 1),

      (2, '信息工程学院志愿者分队',
       '专注于信息技术与科普相关的志愿服务。', 1);

-- ================ 组织成员关系数据 =================
INSERT INTO `organization_members` (
    `user_id`, `organization_id`, `member_role`
) VALUES
      (2, 1, 'member'),  -- 张三-校青协-普通成员
      (3, 1, 'member'),  -- 李四-校青协-普通成员
      (3, 2, 'officer'); -- 李四-信工分队-干部

-- ================== 志愿活动数据 ===================
INSERT INTO `activities` (
    `id`, `title`, `description`, `cover_image_url`,
    `category`, `location`, `start_time`, `end_time`,
    `organizer_id`, `recruitment_quota`, `created_by`
) VALUES
      (1, '迎新日志愿服务',
       '<p>在开学季帮助新生搬运行李，引导报到流程，展现我校学子的风采。</p>',
       'https://images.unsplash.com/photo-1599059813005-72827a055398?q=80&w=2070',
       '校内服务', '全校各区域', '2025-09-01 08:00:00', '2025-09-01 18:00:00',
       1, 50, 1),

      (2, '图书馆整理图书活动',
       '<p>协助图书馆老师整理新到书籍，并进行分类上架。</p>',
       'https://images.unsplash.com/photo-1521587760476-6c12a4b040da?q=80&w=2070',
       '校内服务', '学校图书馆三楼', '2025-07-15 09:00:00', '2025-07-15 17:00:00',
       2, 20, 1);

-- ================= 活动报名数据 ===================
INSERT INTO `enrollments` (
    `user_id`, `activity_id`
) VALUES
      (2, 1),  -- 张三报名迎新活动
      (3, 1);  -- 李四报名迎新活动

-- 更新活动报名人数
UPDATE `activities` SET `current_enrollment` = 2 WHERE `id` = 1;

-- ================ 志愿服务记录 ====================
INSERT INTO `service_records` (
    `user_id`, `activity_id`, `service_hours`,
    `recorded_by`, `remarks`
) VALUES
    (2, 1, 8.5, 1, '该同学表现优异，额外奖励0.5小时。');

-- 更新用户总服务时长
UPDATE `users` SET `total_service_hours` = 8.50 WHERE `id` = 2;

-- ================== 新闻资讯 =====================
INSERT INTO `news` (
    `id`, `title`, `summary`, `content`, `author_id`
) VALUES
    (1, '我校举办首届"代码之光"编程马拉松大赛',
     '近日，为了激发学生的编程热情，我校信息工程学院在图书馆报告厅成功举办了首届"代码之光"编程马拉松大赛...',
     '<p>近日，为了激发学生的编程热情，我校信息工程学院在图书馆报告厅成功举办了首届"代码之光"编程马拉松大赛。比赛吸引了来自全校各个学院的百余名编程爱好者参加。</p><p>比赛持续24小时，考验了选手们的耐力、技术和团队协作能力...</p>',
     1);

-- ================== 脚本结束 =====================