package net.zypro.feed.repository;

import net.zypro.feed.domain.Feed;
import net.zypro.feed.domain.Hot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotRepository extends JpaRepository<Hot, Integer> { // select查找优先考虑jpa自动解析
	Hot findByFeed(Feed feed);

}
