package com.lec.spring.service.community;

import com.lec.spring.repository.community.LikeRepository;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

@Service
public class LikeServiceImpl implements LikeService {

    LikeRepository likeRepository;

    public LikeServiceImpl(SqlSession sqlSession) {
        this.likeRepository = sqlSession.getMapper(LikeRepository.class);
    }


    @Override
    public int addLike(Long userId, Long feedId) {
        return likeRepository.addLike(userId, feedId);
    }

    @Override
    public int removeLike(Long userId, Long feedId) {
        return likeRepository.removeLike(userId, feedId);
    }

    @Override
    public int countLike(Long feedId) {
        return likeRepository.countLike(feedId);
    }
}
