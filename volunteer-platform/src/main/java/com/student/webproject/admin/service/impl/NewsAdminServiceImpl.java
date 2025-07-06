package com.student.webproject.admin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper; // 导入 QueryWrapper
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.student.webproject.admin.service.NewsAdminService;
import com.student.webproject.common.response.Result;
import com.student.webproject.news.News;
import com.student.webproject.news.NewsMapper;
import com.student.webproject.news.dto.NewsCreateDTO;
import com.student.webproject.news.dto.NewsUpdateDTO;
import com.student.webproject.user.Entity.User;
import com.student.webproject.user.mapper.UserMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;

@Service
public class NewsAdminServiceImpl implements NewsAdminService {

    @Autowired
    private NewsMapper newsMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public Result<IPage<News>> listNews(Long page, Long pageSize) {
        IPage<News> pageRequest = new Page<>(page, pageSize);
        IPage<News> pageResult = newsMapper.selectPage(pageRequest, null);
        return Result.success(pageResult, "新闻列表查询成功");
    }

    @Override
    public Result<News> createNews(NewsCreateDTO dto, Principal principal) {
        // 使用 QueryWrapper 通过 username 查询用户
        User currentUser = userMapper.selectOne(new QueryWrapper<User>().eq("username", principal.getName()));
        if (currentUser == null) {
            throw new RuntimeException("无法获取当前用户信息");
        }

        News news = new News();
        BeanUtils.copyProperties(dto, news);
        news.setAuthorId(currentUser.getId());

        // 移除不存在的 setCreatedAt，保留 setUpdatedAt 和 setPublishedAt
        news.setUpdatedAt(LocalDateTime.now());
        news.setPublishedAt(LocalDateTime.now()); // 可根据需求调整发布时间

        news.setLikesCount(0);
        news.setCommentsCount(0);
        news.setFavoritesCount(0);

        newsMapper.insert(news);
        return Result.created(news, "新闻创建成功");
    }

    @Override
    public Result<News> updateNews(Long id, NewsUpdateDTO dto) {
        News news = newsMapper.selectById(id);
        if (news == null) {
            throw new RuntimeException("新闻不存在");
        }
        BeanUtils.copyProperties(dto, news);
        news.setUpdatedAt(LocalDateTime.now());
        newsMapper.updateById(news);
        return Result.success(news, "新闻更新成功");
    }

    @Override
    public Result<Void> deleteNews(Long id) {
        if (newsMapper.selectById(id) == null) {
            throw new RuntimeException("删除失败，找不到ID为 " + id + " 的新闻。");
        }
        newsMapper.deleteById(id);
        return Result.success(null, "新闻删除成功");
    }
}