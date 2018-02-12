package net.zypro.feed.repository;

import net.zypro.feed.domain.Feedback;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> { // 反馈的处理
	Feedback findById(int id);
}
