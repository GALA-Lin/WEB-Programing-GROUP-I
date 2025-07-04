-- ===================================================================
-- V3 - 大批量、高质量模拟数据脚本 (已修正)
-- 说明:
-- 1. 本脚本旨在为平台填充大量逼真的模拟数据，用于全面测试和前端展示。
-- 2. 所有图片URL均来自 Unsplash，保证真实可用。
-- 3. 所有密码的明文统一为 '123456'，对应的密文是相同的。
-- ===================================================================

-- 准备工作：禁用外键检查，清空所有相关表
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE `users`;
TRUNCATE TABLE `organizations`;
TRUNCATE TABLE `organization_members`;
TRUNCATE TABLE `activities`;
TRUNCATE TABLE `enrollments`;
TRUNCATE TABLE `service_records`;
TRUNCATE TABLE `news`;
SET FOREIGN_KEY_CHECKS = 1;


-- ==================== 用户数据 (Users) - 20条 ======================
INSERT INTO `users` (`id`, `student_id`, `username`, `password`, `real_name`, `email`, `phone_number`, `avatar_url`, `role`, `status`, `total_service_hours`)
VALUES
    (1, 'G00001', 'superadmin', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '王校长', 'super@cupk.edu.cn', '18800000001', 'https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?q=80&w=200', 'super_admin', 1, 0),
    (2, 'G00002', 'admin_teacher', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '李老师', 'admin@cupk.edu.cn', '18800000002', 'https://images.unsplash.com/photo-1580894742597-87bc8789db3d?q=80&w=200', 'admin', 1, 0),
    (3, '202301001', 'zhangwei', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '张伟', 'zhangwei@test.com', '18800000003', 'https://images.unsplash.com/photo-1557862921-37829c790f19?q=80&w=200', 'volunteer', 1, 28.5),
    (4, '202301002', 'liuna', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '刘娜', 'liuna@test.com', '18800000004', 'https://images.unsplash.com/photo-1494790108377-be9c29b29330?q=80&w=200', 'volunteer', 1, 15.0),
    (5, '202301003', 'chenhao', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '陈浩', 'chenhao@test.com', '18800000005', 'https://images.unsplash.com/photo-1500648767791-00dcc994a43e?q=80&w=200', 'volunteer', 1, 8.0),
    (6, '202301004', 'yangjing', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '杨静', 'yangjing@test.com', '18800000006', 'https://images.unsplash.com/photo-1599566150163-29194dcaad36?q=80&w=200', 'volunteer', 1, 4.5),
    (7, '202301005', 'sunyu', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '孙宇', 'sunyu@test.com', '18800000007', 'https://images.unsplash.com/photo-1568602471122-7832951cc4c5?q=80&w=200', 'volunteer', 1, 0),
    (8, '202301006', 'wangli', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '王丽', 'wangli@test.com', '18800000008', 'https://images.unsplash.com/photo-1438761681033-6461ffad8d80?q=80&w=200', 'volunteer', 1, 12.0),
    (9, '202301007', 'liming', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '李明', 'liming@test.com', '18800000009', 'https://images.unsplash.com/photo-1544723795-3fb6469f5b39?q=80&w=200', 'volunteer', 1, 3.0),
    (10, '202301008', 'zhoufang', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '周芳', 'zhoufang@test.com', '18800000010', 'https://images.unsplash.com/photo-1542103749-8ef59b94f475?q=80&w=200', 'volunteer', 1, 0),
    (11, '202301009', 'wujie', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '吴杰', 'wujie@test.com', '18800000011', 'https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?q=80&w=200', 'volunteer', 0, 5.0),
    (12, '202301010', 'zhaoxiu', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '赵秀', 'zhaoxiu@test.com', '18800000012', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?q=80&w=200', 'volunteer', 1, 0),
    (13, '202301011', 'qiankun', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '钱坤', 'qiankun@test.com', '18800000013', 'https://images.unsplash.com/photo-1564564321837-a57b7070ac4f?q=80&w=200', 'volunteer', 1, 18.0),
    (14, '202301012', 'mahui', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '马慧', 'mahui@test.com', '18800000014', 'https://images.unsplash.com/photo-1580489944761-15a19d654956?q=80&w=200', 'volunteer', 1, 22.0),
    (15, '202301013', 'liyong', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '李勇', 'liyong@test.com', '18800000015', 'https://images.unsplash.com/photo-1552058544-f2b08422138a?q=80&w=200', 'volunteer', 1, 0),
    (16, '202301014', 'heping', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '何平', 'heping@test.com', '18800000016', 'https://images.unsplash.com/photo-1548142813-c348350df52b?q=80&w=200', 'volunteer', 1, 30.0),
    (17, '202301015', 'jiangli', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '蒋莉', 'jiangli@test.com', '18800000017', 'https://images.unsplash.com/photo-1554151228-14d9def656e4?q=80&w=200', 'volunteer', 1, 0),
    (18, '202301016', 'caofeng', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '曹峰', 'caofeng@test.com', '18800000018', 'https://images.unsplash.com/photo-1552374196-c4e7ffc6e126?q=80&w=200', 'volunteer', 1, 0),
    (19, '202301017', 'yuanmei', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '袁梅', 'yuanmei@test.com', '18800000019', 'https://images.unsplash.com/photo-1491349174775-aaafddd81942?q=80&w=200', 'volunteer', 1, 0),
    (20, '202301018', 'jinrui', '$2a$10$E9p9a7/7dZk2i.cmJ9t71uY8JzT9j.o5r.Jy5a3H.B40H.w2.uG/S', '金瑞', 'jinrui@test.com', '18800000020', 'https://images.unsplash.com/photo-1542909168-82c3e7fdca5c?q=80&w=200', 'volunteer', 1, 0);


-- ================== 组织部门数据 (Organizations) - 6条 ====================
INSERT INTO `organizations` (`id`, `name`, `description`, `leader_id`)
VALUES
    (1, '校青年志愿者协会', '中国石油大学（北京）克拉玛依校区官方最大的志愿者组织，统筹全校志愿服务活动。', 1),
    (2, '信息工程学院志愿者分队', '专注于信息技术与科普相关的志愿服务，开展编程支教、电脑义诊等特色活动。', 3),
    (3, '“石榴籽”少数民族志愿服务队', '致力于民族文化交流与传播，服务社区，促进民族团结。', 4),
    (4, '环保先锋社', '一个专注于环保理念宣传与实践的学生社团。', 13),
    (5, '爱宠之家服务队', '关爱流浪动物，定期前往救助站进行志愿服务。', 14),
    (6, '图书馆志愿服务岗', '协助图书馆进行图书整理、活动支持等常规工作。', 2);


-- ================ 组织成员关系数据 (Organization Members) - 30条 (已修正) =================
INSERT INTO `organization_members` (`user_id`, `organization_id`, `member_role`, `status`)
VALUES
    -- 组织1: 校青协 (成员6人)
    (3, 1, 'officer', 'approved'),
    (4, 1, 'member', 'approved'),
    (5, 1, 'member', 'pending'), -- 修正：补上 member_role
    (8, 1, 'member', 'approved'),
    (9, 1, 'member', 'approved'),
    (13, 1, 'member', 'approved'),
    -- 组织2: 信工分队 (成员5人)
    (3, 2, 'member', 'approved'),
    (5, 2, 'member', 'approved'),
    (6, 2, 'member', 'pending'), -- 修正：补上 member_role
    (7, 2, 'member', 'approved'),
    (18, 2, 'member', 'approved'),
    -- 组织3: 石榴籽 (成员4人)
    (4, 3, 'member', 'approved'),
    (8, 3, 'member', 'approved'),
    (12, 3, 'member', 'approved'),
    (19, 3, 'member', 'approved'),
    -- 组织4: 环保社 (成员4人)
    (13, 4, 'officer', 'approved'),
    (6, 4, 'member', 'approved'),
    (16, 4, 'member', 'approved'),
    (20, 4, 'member', 'approved'),
    -- 组织5: 爱宠之家 (成员4人)
    (14, 5, 'officer', 'approved'),
    (4, 5, 'member', 'approved'),
    (10, 5, 'member', 'pending'), -- 修正：补上 member_role
    (17, 5, 'member', 'approved'),
    -- 组织6: 图书馆 (成员4人)
    (15, 6, 'member', 'approved'),
    (16, 6, 'member', 'approved'),
    (19, 6, 'member', 'approved'),
    (3, 6, 'member', 'approved');

-- ================== 志愿活动数据 (Activities) - 15条 ===================
INSERT INTO `activities` (`id`, `title`, `description`, `cover_image_url`, `category`, `location`, `start_time`, `end_time`, `organizer_id`, `recruitment_quota`, `current_enrollment`, `status`, `created_by`)
VALUES
    (1, '克拉玛依河环保健步走', '沿克拉玛依河进行健步走，同时清理沿途垃圾，宣传环保理念。', 'https://images.unsplash.com/photo-1593336962359-d484c31c437d?q=80&w=800', '环保', '克拉玛依河沿岸', '2025-07-10 09:00:00', '2025-07-10 12:00:00', 4, 50, 4, 'recruiting', 2),
    (2, '老年人智能手机使用教学', '在社区活动中心，一对一教老年人使用智能手机，解决“数字鸿沟”问题。', 'https://images.unsplash.com/photo-1585250141639-51a4855f434b?q=80&w=800', '社区服务', '金龙镇街道活动中心', '2025-07-12 14:00:00', '2025-07-12 17:00:00', 1, 20, 2, 'recruiting', 1),
    (3, '“编程一小时”少儿科普活动', '为小学生带来有趣的图形化编程体验，激发他们对科学技术的兴趣。', 'https://images.unsplash.com/photo-1542831371-29b0f74f9713?q=80&w=800', '教育', '克拉玛依市第一小学', '2025-07-19 10:00:00', '2025-07-19 15:00:00', 2, 15, 1, 'recruiting', 2),
    (4, '流浪动物救助站大扫除', '前往合作的流浪动物救助站，帮助清理犬舍猫舍，为小动物们提供一个干净的环境。', 'https://images.unsplash.com/photo-1598822945233-8b7f5252e125?q=80&w=800', '动物保护', '克拉玛依流浪动物救助站', '2025-07-20 09:30:00', '2025-07-20 16:30:00', 5, 25, 1, 'recruiting', 14),
    (5, '“书香满校园”旧书回收整理', '在校园内设立回收点，回收同学们的旧教材和课外书，整理后捐赠给有需要的地区。', 'https://images.unsplash.com/photo-1532012197267-da84d127e765?q=80&w=800', '校内服务', '学校一食堂门口', '2025-07-22 11:00:00', '2025-07-23 18:00:00', 6, 30, 0, 'recruiting', 2),
    (6, '维吾尔族传统手工艺文化展演', '协助举办文化展，向市民和游客展示维吾尔族传统手工艺的魅力。', 'https://images.unsplash.com/photo-1599493343460-a4a4b52515b2?q=80&w=800', '文化传播', '克拉玛依市文化馆', '2025-08-05 10:00:00', '2025-08-05 17:00:00', 3, 10, 0, 'recruiting', 4),
    (7, '城市马拉松赛事志愿者', '在马拉松比赛中担任站点服务人员，为选手提供饮水、补给和加油助威。', 'https://images.unsplash.com/photo-1571008782918-b27b9f193400?q=80&w=800', '体育赛事', '世纪公园', '2025-08-10 07:00:00', '2025-08-10 13:00:00', 1, 100, 0, 'recruiting', 1),
    (8, '“地球一小时”熄灯仪式宣传', '在校园内宣传“地球一小时”活动，鼓励大家参与全球性的环保行动。', 'https://images.unsplash.com/photo-1614901328247-f3fb40592945?q=80&w=800', '环保', '全校范围', '2024-03-20 18:00:00', '2024-03-23 20:00:00', 4, 20, 2, 'finished', 13),
    (9, '迎新日志愿服务（往期）', '在开学季帮助新生搬运行李，引导报到流程，展现我校学子的风采。', 'https://images.unsplash.com/photo-1599059813005-72827a055398?q=80&w=800', '校内服务', '全校各区域', '2024-09-01 08:00:00', '2024-09-01 18:00:00', 1, 50, 4, 'finished', 1),
    (10, '图书馆整理图书活动（往期）', '协助图书馆老师整理新到书籍，并进行分类上架。', 'https://images.unsplash.com/photo-1521587760476-6c12a4b040da?q=80&w=800', '校内服务', '学校图书馆三楼', '2024-07-15 09:00:00', '2024-07-15 17:00:00', 6, 20, 2, 'finished', 2),
    (11, '社区电脑义诊（往期）', '为社区居民提供免费的电脑系统安装、软件问题咨询、硬件清灰等服务。', 'https://images.unsplash.com/photo-1555421689-491a97ff2040?q=80&w=800', '社区服务', '金龙镇街道活动中心', '2024-05-20 10:00:00', '2024-05-20 16:00:00', 2, 10, 1, 'finished', 2),
    (12, '“净滩行动”海滩清洁（已结束）', '组织前往海边，清理沙滩上的塑料垃圾和其他废弃物。', 'https://images.unsplash.com/photo-1617153635292-624123515b2?q=80&w=800', '环保', '附近海滩', '2024-06-08 09:00:00', '2024-06-08 12:00:00', 4, 30, 2, 'finished', 13),
    (13, '暑期夏令营（已取消）', '原计划的夏令营因故取消。', 'https://images.unsplash.com/photo-1576495199011-bde07d3b5336?q=80&w=800', '教育', '校内', '2024-08-01 09:00:00', '2024-08-10 17:00:00', 1, 30, 0, 'canceled', 1),
    (14, '进行中的音乐节后勤保障', '协助音乐节主办方进行场地布置、秩序维护和物资分发。', 'https://images.unsplash.com/photo-1514525253161-7a46d19cd819?q=80&w=800', '文化传播', '市体育中心', '2025-07-04 14:00:00', '2025-07-06 22:00:00', 1, 40, 2, 'ongoing', 1),
    (15, '进行中的防灾减灾社区演练', '配合社区进行消防和地震应急演练，向居民普及防灾知识。', 'https://images.unsplash.com/photo-1561411837-34f3d252f84c?q=80&w=800', '社区服务', '世纪花园社区', '2025-07-05 09:00:00', '2025-07-05 12:00:00', 1, 15, 2, 'ongoing', 2);


-- ================= 活动报名数据 (Enrollments) - 20条 ===================
INSERT INTO `enrollments` (`user_id`, `activity_id`, `status`)
VALUES
    (3, 1, 'enrolled'), (4, 1, 'enrolled'), (8, 1, 'enrolled'), (13, 1, 'enrolled'),
    (5, 2, 'enrolled'), (9, 2, 'enrolled'),
    (3, 3, 'enrolled'),
    (4, 4, 'enrolled'),
    (3, 8, 'enrolled'), (6, 8, 'enrolled'),
    (3, 9, 'enrolled'), (4, 9, 'enrolled'), (8, 9, 'enrolled'), (14, 9, 'enrolled'),
    (13, 10, 'enrolled'), (16, 10, 'enrolled'),
    (5, 11, 'enrolled'),
    (14, 12, 'enrolled'), (16, 12, 'enrolled'),
    (14, 14, 'enrolled'),(16, 14, 'enrolled'),
    (3, 15, 'enrolled'),(4, 15, 'enrolled');

-- ================= 志愿服务记录 (Service Records) - 10条 ====================
INSERT INTO `service_records` (`user_id`, `activity_id`, `service_hours`, `recorded_by`, `remarks`)
VALUES
    (3, 8, 4.5, 13, '张伟同学积极参与宣传工作。'),
    (6, 8, 4.5, 13, '杨静同学全程参与，认真负责。'),
    (3, 9, 10.0, 1, '张伟同学表现优异，获得一致好评。'),
    (4, 9, 10.0, 1, '刘娜同学服务热情，耐心引导。'),
    (8, 9, 8.0, 1, '王丽同学参与了大部分时段。'),
    (14, 9, 5.0, 1, '马慧同学参与了半天。'),
    (13, 10, 8.0, 2, '钱坤同学整理图书效率高。'),
    (16, 10, 8.0, 2, '何平同学工作细致。'),
    (5, 11, 6.0, 2, '陈浩同学解决了多个电脑技术问题。'),
    (14, 12, 3.0, 13, '马慧同学积极参与海滩清洁。'),
    (16, 12, 3.0, 13, '何平同学完成了任务。');

-- ================== 新闻资讯 (News) - 5条 =====================
INSERT INTO `news` (`id`, `title`, `summary`, `content`, `author_id`, `likes_count`)
VALUES
    (1, '我校举办首届"代码之光"编程马拉松大赛', '近日，为了激发学生的编程热情，我校信息工程学院在图书馆报告厅成功举办了首届"代码之光"编程马拉松大赛...', '### 比赛盛况\n\n近日，为了激发学生的编程热情，我校信息工程学院在图书馆报告厅成功举办了首届"代码之光"编程马拉松大赛。比赛吸引了来自全校各个学院的百余名编程爱好者参加。\n\n> 比赛持续24小时，考验了选手们的耐力、技术和团队协作能力。\n\n### 硕果累累\n\n经过激烈的角逐，最终由软工21-1班的“码到成功”队摘得桂冠。他们的项目“校园智能导航小程序”以其创新性和实用性获得了评委们的一致好评。', 1, 108),
    (2, '青年志愿者协会换届大会圆满成功', '新一届青协主席团选举产生，他们将带领我校志愿服务走向新的辉煌。', '在热烈的掌声中，新一届主席团成员从老成员手中接过了旗帜，这不仅是权力的交接，更是责任与使命的传承。新任主席张伟同学表示，将不负众望，带领青协开展更多有意义的活动。', 2, 76),
    (3, '我校志愿者在市马拉松比赛中展风采', '上周日，我市举办的国际马拉松比赛中，来自我校的百余名志愿者成为了一道亮丽的风景线。', '他们分布在赛道的各个服务站点，为选手们提供饮水、补给、医疗救助等服务。他们热情的微笑和专业的服务，获得了参赛选手和组委会的高度赞扬。', 1, 230),
    (4, '环保社成功举办“变废为宝”创意手工大赛', '为迎接世界地球日，环保先锋社举办了“变废为宝”创意手工大赛，鼓励同学们利用废旧物品进行艺术创作。', '本次大赛共收到作品五十余件，涵盖了从塑料瓶艺术装置到旧衣改造等多个类别。活动不仅锻炼了同学们的动手能力，更在校园中传播了绿色环保的理念。', 13, 152),
    (5, '爱宠之家服务队首次社区领养日活动回顾', '爱宠之家服务队于上周末在世纪公园成功举办了首次社区流浪动物领养日活动。', '活动现场，共有5只可爱的猫咪和3只狗狗找到了它们温暖的新家。服务队表示，未来将定期举办此类活动，希望能为更多的流浪动物带来希望。', 14, 312);


-- ================== 脚本结束 =====================