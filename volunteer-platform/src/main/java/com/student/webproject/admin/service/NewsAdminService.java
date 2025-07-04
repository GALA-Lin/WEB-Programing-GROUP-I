package com.student.webproject.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.student.webproject.common.response.Result;
import com.student.webproject.news.News;
import com.student.webproject.news.dto.NewsCreateDTO;
import com.student.webproject.news.dto.NewsUpdateDTO;

import java.security.Principal;

public interface NewsAdminService {

    Result<IPage<News>> listNews(Long page, Long pageSize);

    Result<News> createNews(NewsCreateDTO dto, Principal principal);

    Result<News> updateNews(Long id, NewsUpdateDTO dto);

    Result<Void> deleteNews(Long id);
}