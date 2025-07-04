-- ===================================================================
-- V2 - 全功能测试数据脚本
-- 说明:
-- 1. 本脚本用于填充覆盖多种测试场景的基础数据。
-- 2. 执行前请确保已执行最新的 V1__initial_schema.sql。
-- 3. 所有密码的明文统一为 '123456'。
-- ===================================================================

-- ==================== 用户数据 (Users) ======================
-- 清空旧数据
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE `users`;
SET FOREIGN_KEY_CHECKS = 1;

-- 插入用户，覆盖不同角色和状态
INSERT INTO `users`
(`id`, `student_id`, `username`, `password`, `real_name`, `email`, `phone_number`, `avatar_url`, `role`, `status`, `total_service_hours`)
VALUES
    -- 1. 超级管理员, 也是组织1的负责人
    (1, 'G00001', 'superadmin', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '王校长', 'super@cupk.edu.cn', '18800000001', 'https://i.pravatar.cc/150?u=superadmin', 'super_admin', 1, 0),
    -- 2. 普通管理员
    (2, 'G00002', 'admin', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '李老师', 'admin@cupk.edu.cn', '18800000002', 'https://i.pravatar.cc/150?u=admin', 'admin', 1, 0),
    -- 3. 普通志愿者, 已加入组织1和2, 且是组织2的负责人
    (3, '202301001', 'zhangsan', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '张三', 'zhangsan@test.com', '18800000003', 'https://i.pravatar.cc/150?u=zhangsan', 'volunteer', 1, 12.5),
    -- 4. 普通志愿者, 已加入组织1
    (4, '202301002', 'lisi', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '李四', 'lisi@test.com', '18800000004', 'https://i.pravatar.cc/150?u=lisi', 'volunteer', 1, 8.0),
    -- 5. 普通志愿者, 已申请加入组织1, 状态为待审批 (pending)
    (5, '202301003', 'wangwu', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '王五', 'wangwu@test.com', '18800000005', 'https://i.pravatar.cc/150?u=wangwu', 'volunteer', 1, 0),
    -- 6. 普通志愿者, 未加入任何组织
    (6, '202301004', 'zhaoliu', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '赵六', 'zhaoliu@test.com', '18800000006', 'https://i.pravatar.cc/150?u=zhaoliu', 'volunteer', 1, 0),
    -- 7. 被禁用的志愿者
    (7, '202301005', 'disableduser', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '禁用账户', 'disabled@test.com', '18800000007', 'https://i.pravatar.cc/150?u=disabled', 'volunteer', 0, 2.0);

-- ================== 组织部门数据 (Organizations) ====================
-- 清空旧数据
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE `organizations`;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO `organizations` (`id`, `name`, `description`, `leader_id`)
VALUES
    (1, '校青年志愿者协会', '中国石油大学（北京）克拉玛依校区官方最大的志愿者组织，统筹全校志愿服务活动。', 1),
    (2, '信息工程学院志愿者分队', '专注于信息技术与科普相关的志愿服务，开展编程支教、电脑义诊等特色活动。', 3),
    (3, '石榴籽”少数民族志愿服务队', '致力于民族文化交流与传播，服务社区，促进民族团结。', NULL),
    (4, '环保先锋社', '一个专注于环保理念宣传与实践的学生社团，目前暂无成员。', NULL);

-- ================ 组织成员关系数据 (Organization Members) =================
-- 清空旧数据
TRUNCATE TABLE `organization_members`;

INSERT INTO `organization_members` (`user_id`, `organization_id`, `member_role`, `status`)
VALUES
    -- 组织1: 校青协
    (3, 1, 'officer', 'approved'),  -- 张三是干部
    (4, 1, 'member', 'approved'),   -- 李四是普通成员
    (5, 1, 'member', 'pending'),    -- 王五待审批
    -- 组织2: 信工分队
    (3, 2, 'member', 'approved'),   -- 张三也是信工分队的普通成员
    -- 组织3: 石榴籽服务队
    (4, 3, 'member', 'approved');   -- 李四也加入了石榴籽

-- ================== 志愿活动数据 (Activities) ===================
-- 清空旧数据
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE `activities`;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO `activities` (`id`, `title`, `description`, `cover_image_url`, `category`, `location`, `start_time`, `end_time`, `organizer_id`, `recruitment_quota`, `current_enrollment`, `status`, `created_by`)
VALUES
    (1, '迎新日志愿服务', '<p>在开学季帮助新生搬运行李，引导报到流程，展现我校学子的风采。</p>', 'https://images.unsplash.com/photo-1599059813005-72827a055398?q=80&w=2070', '校内服务', '全校各区域', '2025-09-01 08:00:00', '2025-09-01 18:00:00', 1, 50, 2, 'recruiting', 1),
    (2, '图书馆整理图书活动', '<p>协助图书馆老师整理新到书籍，并进行分类上架。</p>', 'https://images.unsplash.com/photo-1521587760476-6c12a4b040da?q=80&w=2070', '校内服务', '学校图书馆三楼', '2025-07-15 09:00:00', '2025-07-15 17:00:00', 2, 20, 1, 'recruiting', 2),
    (3, '社区电脑义诊', '<p>为社区居民提供免费的电脑系统安装、软件问题咨询、硬件清灰等服务。</p>', 'https://images.unsplash.com/photo-1555421689-491a97ff2040?q=80&w=2070', '社区服务', '金龙镇街道活动中心', '2024-05-20 10:00:00', '2024-05-20 16:00:00', 2, 10, 1, 'finished', 2),
    (4, '植树节环保活动', '<p>前往九公里进行植树，为克拉玛依的春天增添一抹绿色。</p>', 'https://images.unsplash.com/photo-1512428209355-e51c243a4e5c?q=80&w=2070', '环保', '九公里', '2024-03-12 09:00:00', '2024-03-12 17:00:00', 1, 100, 0, 'finished', 1),
    (5, '暑期夏令营（已取消）', '<p>原计划的夏令营因故取消。</p>', 'https://images.unsplash.com/photo-1576495199011-bde07d3b5336?q=80&w=2070', '教育', '校内', '2025-08-01 09:00:00', '2025-08-10 17:00:00', 1, 30, 0, 'canceled', 1);

-- ================= 活动报名数据 (Enrollments) ===================
-- 清空旧数据
TRUNCATE TABLE `enrollments`;

INSERT INTO `enrollments` (`user_id`, `activity_id`)
VALUES
    (3, 1), -- 张三报名迎新活动
    (4, 1), -- 李四报名迎新活动
    (3, 2), -- 张三报名图书馆活动
    (4, 3); -- 李四报名了已结束的电脑义诊

-- ================= 志愿服务记录 (Service Records) ====================
-- 清空旧数据
TRUNCATE TABLE `service_records`;

INSERT INTO `service_records` (`user_id`, `activity_id`, `service_hours`, `recorded_by`, `remarks`)
VALUES
    (3, 3, 4.5, 2, '张三同学技术娴熟，解决了多个棘手问题。'),
    (4, 3, 8.0, 2, '李四同学全天参与，服务热情。'),
    (3, 4, 8.0, 1, '参与植树节活动，表现优异。'),
    (7, 4, 2.0, 1, '参与了2小时。'); -- 假设禁用账户之前也有时长

-- ================== 新闻资讯 (News) =====================
-- 清空旧数据
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE `news`;
SET FOREIGN_KEY_CHECKS = 1;

INSERT INTO `news` (`id`, `title`, `summary`, `content`, `author_id`, `likes_count`)
VALUES
    (1, '我校举办首届"代码之光"编程马拉松大赛', '近日，为了激发学生的编程热情，我校信息工程学院在图书馆报告厅成功举办了首届"代码之光"编程马拉松大赛...', '<p>近日，为了激发学生的编程热情，我校信息工程学院在图书馆报告厅成功举办了首届"代码之光"编程马拉松大赛。比赛吸引了来自全校各个学院的百余名编程爱好者参加。</p><p>比赛持续24小时，考验了选手们的耐力、技术和团队协作能力...</p>', 1, 108),
    (2, '青年志愿者协会换届大会圆满成功', '新一届青协主席团选举产生，他们将带领我校志愿服务走向新的辉煌。', '<p>在热烈的掌声中，新一届主席团成员从老成员手中接过了旗帜，象征着责任与使命的传承...</p>', 2, 76);

-- ================== 脚本结束 =====================