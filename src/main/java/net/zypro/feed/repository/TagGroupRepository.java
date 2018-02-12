package net.zypro.feed.repository;

import java.util.List;

import javax.transaction.Transactional;

import net.zypro.feed.domain.Admin;
import net.zypro.feed.domain.Tag;
import net.zypro.feed.domain.TagGroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagGroupRepository extends JpaRepository<TagGroup, Integer>{
TagGroup findByTag1AndTag2(String tag1,String tag2);
List<TagGroup> findByTag1OrTag2(String tag1,String tag2);

}
