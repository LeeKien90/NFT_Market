package ra.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ra.model.entity.Blog;

import java.util.List;

@Repository
public interface BlogRepository extends JpaRepository<Blog,Integer> {
    @Query(value = "select b.blogID, b.blogContent, b.blogImage, b.blogTitle, b.createdDate, b.views, b.userCreatedID\n" +
            "from blog b \n" +
            "order by views desc limit 4",nativeQuery = true)
    List<Blog> getByPopularArticles();

    @Query(value = "select b.blogID, b.blogContent, b.blogImage, b.blogTitle, b.createdDate, b.views, b.userCreatedID\n" +
            "from blog b\n" +
            "order by createdDate desc limit 6",nativeQuery = true)
    List<Blog> getByRecentlyArticles();

    @Query(value = "select b.blogID, b.blogContent, b.blogImage, b.blogTitle, b.createdDate, b.views, b.userCreatedID\n" +
            "from blog b\n" +
            "order by createdDate desc limit :size offset :page",nativeQuery = true)
    List<Blog> getAll(@Param("size")int size, @Param("page")int page);
}
