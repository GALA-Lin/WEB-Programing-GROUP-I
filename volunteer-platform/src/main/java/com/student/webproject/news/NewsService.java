package com.student.webproject.news;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.student.webproject.news.dto.NewsDetailDTO;
import com.student.webproject.news.dto.NewsSummaryDTO;
import com.student.webproject.news.dto.PageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {

    @Autowired
    private NewsMapper newsMapper;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public PageDTO<NewsSummaryDTO> findNewsPage(int pageNum, int pageSize) {
        Page<News> page = new Page<>(pageNum, pageSize);
        IPage<News> iPage = newsMapper.findNewsPageWithAuthor(page);

        List<NewsSummaryDTO> dtoList = iPage.getRecords().stream()
                .map(this::convertToSummaryDTO)
                .collect(Collectors.toList());

        return new PageDTO<>(iPage.getTotal(), dtoList);
    }

    public NewsDetailDTO findNewsDetailById(Long id) {
        News news = newsMapper.findNewsDetailWithAuthor(id);
        return news != null ? convertToDetailDTO(news) : null;
    }

    public Integer likeNews(Long id) {
        News news = newsMapper.selectById(id);
        if (news == null) {
            return null;
        }

        news.setLikesCount(news.getLikesCount() + 1);
        newsMapper.updateById(news);

        return news.getLikesCount();
    }

    private NewsSummaryDTO convertToSummaryDTO(News news) {
        NewsSummaryDTO dto = new NewsSummaryDTO();
        dto.setId(news.getId());
        dto.setTitle(news.getTitle());
        dto.setSummary(news.getSummary());
        dto.setAuthorName(news.getAuthorName());
        dto.setLikesCount(news.getLikesCount());
        dto.setCommentsCount(news.getCommentsCount());
        if (news.getPublishedAt() != null) {
            dto.setPublishedAt(news.getPublishedAt().format(FORMATTER));
        }
        return dto;
    }

    private NewsDetailDTO convertToDetailDTO(News news) {
        NewsDetailDTO dto = new NewsDetailDTO();
        dto.setId(news.getId());
        dto.setTitle(news.getTitle());
        dto.setSummary(news.getSummary());
        dto.setContent(news.getContent());
        dto.setAuthorName(news.getAuthorName());
        dto.setLikesCount(news.getLikesCount());
        dto.setCommentsCount(news.getCommentsCount());
        dto.setFavoritesCount(news.getFavoritesCount());
        if (news.getPublishedAt() != null) {
            dto.setPublishedAt(news.getPublishedAt().format(FORMATTER));
        }
        return dto;
    }
}