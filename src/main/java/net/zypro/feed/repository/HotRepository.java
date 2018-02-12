package net.zypro.feed.repository;

import net.zypro.feed.domain.Feed;
import net.zypro.feed.domain.Hot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HotRepository extends JpaRepository<Hot, Integer> { // select�������ȿ���jpa�Զ�����
	Hot findByFeed(Feed feed);

}
