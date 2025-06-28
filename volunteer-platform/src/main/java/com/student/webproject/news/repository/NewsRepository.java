// 文件路径: src/main/java/com/student/webproject/news/NewsRepository.java
/*
Repository 是你与数据库直接对话的接口，它能帮你执行 SQL 查询
extends JpaRepository: 继承 JpaRepository 后，你就自动拥有了大量的常用数据库操作方法，比如 findAll() (查询所有), findById() (按ID查询), save() (保存/更新) 等，无需自己编写 SQL。
 */
package com.student.webproject.news.repository;

import com.student.webproject.news.entity.News;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsRepository extends JpaRepository<News, Long> {
    // JpaRepository 已经提供了我们需要的所有基本方法！
    // 暂时不需要添加任何自定义方法。
}