// 文件路径: src/main/java/com/student/webproject/news/NewsController.java
/*
Controller 是 API 的入口，它负责接收前端发来的请求，调用 Service 处理，然后把结果返回给前端。
@RestController: 告诉 Spring 这是一个处理 RESTful API 请求的控制器。

@RequestMapping("/api/news"): 定义了这个控制器下所有 API 的公共前缀路径。

@GetMapping, @PostMapping: 将方法和 HTTP 请求类型及路径绑定起来。

@PathVariable: 获取 URL路径中的变量 (例如 /api/news/{id}里的 id)。
 */

package com.student.webproject.news.controller;

import com.student.webproject.news.service.NewsService;
import com.student.webproject.news.entity.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/news") // 对应 Apifox 里的 /api/news
public class NewsController {

    @Autowired
    private NewsService newsService;

    /**
     * API 1: 获取新闻列表
     * GET /api/news
     */
    @GetMapping
    public List<News> getAllNews() {
        return newsService.findAllNews();
    }

    /**
     * API 2: 获取单个新闻详情
     * GET /api/news/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<News> getNewsById(@PathVariable Long id) {
        return newsService.findNewsById(id)
                .map(ResponseEntity::ok) // 如果找到，返回 200 OK 和新闻数据
                .orElse(ResponseEntity.notFound().build()); // 如果没找到，返回 404 Not Found
    }

    /**
     * API 3: 新闻点赞
     * POST /api/news/{id}/like
     */
    @PostMapping("/{id}/like")
    public ResponseEntity<News> likeNewsById(@PathVariable Long id) {
        try {
            News updatedNews = newsService.likeNews(id);
            return ResponseEntity.ok(updatedNews);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}