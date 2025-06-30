package com.student.webproject.news;

import com.student.webproject.news.dto.NewsDetailDTO;
import com.student.webproject.news.dto.NewsSummaryDTO;
import com.student.webproject.news.dto.PageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NewsService {

    @Autowired
    private NewsMapper newsMapper;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public PageDTO<NewsSummaryDTO> findNewsPage(int page, int pageSize) {
        int offset = (page - 1) * pageSize;
        List<News> newsList = newsMapper.findListByPage(offset, pageSize);
        long total = newsMapper.countAll();

        List<NewsSummaryDTO> dtoList = newsList.stream()
                .map(this::convertToSummaryDTO)
                .collect(Collectors.toList());

        return new PageDTO<>(total, dtoList);
    }

    public NewsDetailDTO findNewsDetailById(Long id) {
        News news = newsMapper.findById(id);
        return news != null ? convertToDetailDTO(news) : null;
    }

    public Integer likeNews(Long id) {
        if (newsMapper.findById(id) == null) {
            return null; // 新闻不存在
        }
        newsMapper.incrementLikesCount(id);
        return newsMapper.findLikesCountById(id);
    }

    // --- 私有转换方法 (Entity -> DTO) ---

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