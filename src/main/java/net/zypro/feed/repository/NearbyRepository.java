package net.zypro.feed.repository;

import java.util.List;

import javax.transaction.Transactional;

import net.zypro.feed.domain.Feed;
import net.zypro.feed.domain.Nearby;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface NearbyRepository extends JpaRepository<Nearby, Integer> {
	Nearby findByFeed(Feed feed);
	List<Nearby> findByAddress(String address);   //先查询然后再删除
}
