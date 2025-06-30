package com.student.webproject.news;

import com.student.webproject.common.Result;
import com.student.webproject.news.dto.NewsDetailDTO;
import com.student.webproject.news.dto.NewsSummaryDTO;
import com.student.webproject.news.dto.PageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    /**
     * 1. 查询新闻列表接口
     */
    @GetMapping
    public Result<PageDTO<NewsSummaryDTO>> getNewsList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        PageDTO<NewsSummaryDTO> newsPage = newsService.findNewsPage(page, pageSize);
        return Result.success("查询成功", newsPage);
    }

    /**
     * 2. 查询新闻详情接口
     */
    @GetMapping("/{id}")
    public Result<NewsDetailDTO> getNewsDetail(@PathVariable Long id) {
        NewsDetailDTO newsDetail = newsService.findNewsDetailById(id);
        if (newsDetail == null) {
            return Result.error(404, "新闻不存在");
        }
        return Result.success("查询成功", newsDetail);
    }

    /**
     * 3. 点赞新闻接口
     */
    @PostMapping("/{id}/like")
    public Result<?> likeNews(@PathVariable Long id) {
        Integer newLikesCount = newsService.likeNews(id);
        if (newLikesCount == null) {
            return Result.error(404, "新闻不存在，无法点赞");
        }
        // 根据API文档，返回一个包含 newLikesCount 字段的对象
        Map<String, Integer> responseData = Collections.singletonMap("newLikesCount", newLikesCount);
        return Result.success("点赞成功", responseData);
    }
}