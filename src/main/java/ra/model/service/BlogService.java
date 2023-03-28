package ra.model.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import ra.dto.response.BlogReponse;
import ra.model.entity.Blog;

import java.util.List;

public interface BlogService {
    BlogReponse findById(int blogId);
    List<BlogReponse> getAll(int size, int page);
    List<BlogReponse> getByRecentlyArticles();
    List<BlogReponse> getByPopularArticles();


}
