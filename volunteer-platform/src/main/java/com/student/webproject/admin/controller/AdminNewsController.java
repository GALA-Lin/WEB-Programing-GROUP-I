package com.student.webproject.admin.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.student.webproject.admin.service.NewsAdminService;
import com.student.webproject.common.response.Result;
import com.student.webproject.news.News;
import com.student.webproject.news.dto.NewsCreateDTO;
import com.student.webproject.news.dto.NewsUpdateDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/admin/news")
public class AdminNewsController {

    @Autowired
    private NewsAdminService newsAdminService;

    @GetMapping
    public Result<IPage<News>> listNews(@RequestParam(defaultValue = "1") Long page,
                                        @RequestParam(defaultValue = "10") Long pageSize) {
        return newsAdminService.listNews(page, pageSize);
    }

    @PostMapping
    public Result<News> createNews(@RequestBody NewsCreateDTO dto, Principal principal) {
        return newsAdminService.createNews(dto, principal);
    }

    @PutMapping("/{id}")
    public Result<News> updateNews(@PathVariable Long id, @RequestBody NewsUpdateDTO dto) {
        return newsAdminService.updateNews(id, dto);
    }

    @DeleteMapping("/{id}")
    public Result<Void> deleteNews(@PathVariable Long id) {
        return newsAdminService.deleteNews(id);
    }
}