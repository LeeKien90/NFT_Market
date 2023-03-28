package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ra.model.entity.Tag;
import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag,Integer> {
    Tag findByTagName(String tagName);
}
