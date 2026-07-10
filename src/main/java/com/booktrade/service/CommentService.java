package com.booktrade.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.booktrade.entity.Comment;
import com.booktrade.entity.User;
import com.booktrade.mapper.CommentMapper;
import com.booktrade.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    private final CommentMapper commentMapper;
    private final UserMapper userMapper;

    public CommentService(CommentMapper commentMapper, UserMapper userMapper) {
        this.commentMapper = commentMapper;
        this.userMapper = userMapper;
    }

    public List<Comment> listByBookId(Long bookId) {
        List<Comment> comments = commentMapper.selectList(
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getBookId, bookId)
                        .isNull(Comment::getParentId)
                        .orderByDesc(Comment::getCreateTime));

        for (Comment comment : comments) {
            User user = userMapper.selectById(comment.getUserId());
            if (user != null) {
                comment.setUserNickname(user.getNickname());
            }
            List<Comment> replies = getRepliesByParentId(comment.getId());
            comment.setReplies(replies);
        }
        return comments;
    }

    public List<Comment> getRepliesByParentId(Long parentId) {
        List<Comment> replies = commentMapper.selectList(
                new LambdaQueryWrapper<Comment>()
                        .eq(Comment::getParentId, parentId)
                        .orderByAsc(Comment::getCreateTime));

        for (Comment reply : replies) {
            User user = userMapper.selectById(reply.getUserId());
            if (user != null) {
                reply.setUserNickname(user.getNickname());
            }
        }
        return replies;
    }

    public boolean create(Comment comment) {
        comment.setCreateTime(LocalDateTime.now());
        return commentMapper.insert(comment) > 0;
    }

    public Comment getById(Long id) {
        return commentMapper.selectById(id);
    }

    public long countByBookId(Long bookId) {
        return commentMapper.selectCount(
                new LambdaQueryWrapper<Comment>().eq(Comment::getBookId, bookId));
    }
}