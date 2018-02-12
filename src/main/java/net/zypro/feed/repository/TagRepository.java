package net.zypro.feed.repository;

import java.util.List;

import net.zypro.feed.domain.Tag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends JpaRepository<Tag,Integer>{
Tag findByName(String name);
Tag findById(int id);
@Query("select t from Tag t order by rand()")
List<Tag> findTags();
}
