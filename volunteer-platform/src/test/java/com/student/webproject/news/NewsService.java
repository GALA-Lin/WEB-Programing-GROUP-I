// 文件路径: src/main/java/com/student/webproject/news/NewsService.java

/*
@Service: 标记这是一个服务类。

@Autowired: 自动注入我们刚刚创建的 NewsRepository，这样 Service 就可以使用 Repository 来操作数据库了。

findAllNews(): 调用 newsRepository.findAll() 获取所有新闻。

findNewsById(): 调用 newsRepository.findById() 获取单条新闻。

likeNews(): 这是你点赞功能的业务逻辑。先找到新闻，然后把它的 likeCount 加一，最后保存回数据库。
 */

package com.student.webproject.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class NewsService {

    @Autowired
    private NewsRepository newsRepository;

    /**
     * 任务1: 获取所有新闻列表
     */
    public List<News> findAllNews() {
        return newsRepository.findAll();
    }

    /**
     * 任务2: 根据ID获取单条新闻
     */
    public Optional<News> findNewsById(Long id) {
        return newsRepository.findById(id);
    }

    /**
     * 任务3: 点赞新闻
     * @return 返回更新后的新闻对象
     */
    public News likeNews(Long id) {
        // 1. 从数据库找到这条新闻
        Optional<News> newsOptional = newsRepository.findById(id);

        if (newsOptional.isPresent()) {
            News news = newsOptional.get();
            // 2. 将点赞数 +1
            news.setLikeCount(news.getLikeCount() + 1);
            // 3. 保存回数据库并返回
            return newsRepository.save(news);
        } else {
            // 如果新闻不存在，可以抛出异常
            throw new RuntimeException("News not found with id: " + id);
        }
    }
}