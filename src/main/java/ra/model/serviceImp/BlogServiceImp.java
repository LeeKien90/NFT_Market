package ra.model.serviceImp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ra.dto.response.BlogReponse;
import ra.model.entity.Blog;
import ra.model.repository.BlogRepository;
import ra.model.service.BlogService;

import java.util.ArrayList;
import java.util.List;
@Service
public class BlogServiceImp implements BlogService {
    @Autowired
    private BlogRepository blogRepository;


    @Override
    public BlogReponse findById(int blogId) {
        try {
            Blog blog = blogRepository.findById(blogId).get();
            blog.setViews(blog.getViews()+1);
            blogRepository.save(blog);
            BlogReponse blogReponse = data(blogRepository.findById(blogId).get());
            return blogReponse;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public List<BlogReponse> getAll(int size, int page) {
        List<Blog> blogsList = blogRepository.getAll(size, page*size);
        List<BlogReponse> blogReponses = new ArrayList<>();
        for (Blog blog : blogsList) {
            BlogReponse blogReponse = data(blog);
            blogReponses.add(blogReponse);
        }
        return blogReponses;
    }

    @Override
    public List<BlogReponse> getByRecentlyArticles() {
        List<Blog> blogsList = blogRepository.getByRecentlyArticles();
        List<BlogReponse> blogReponses = new ArrayList<>();
        for (Blog blog : blogsList) {
            BlogReponse blogReponse = data(blog);
            blogReponses.add(blogReponse);
        }
        return blogReponses;
    }


    @Override
    public List<BlogReponse> getByPopularArticles() {
        List<Blog> blogs = blogRepository.getByPopularArticles();
        List<BlogReponse> blogReponses = new ArrayList<>();
        for (Blog blog: blogs) {
            BlogReponse blogReponse = data(blog);
            blogReponses.add(blogReponse);
        }
        return blogReponses;
    }


    public static BlogReponse data(Blog blog){
        BlogReponse blogReponse = new BlogReponse();
        blogReponse.setBlogID(blog.getBlogID());
        blogReponse.setBlogTitle(blog.getBlogTitle());
        blogReponse.setBlogImage(blog.getBlogImage());
        blogReponse.setCreatedDate(blog.getCreatedDate());
        blogReponse.setViews(blog.getViews());
        blogReponse.setAuthorId(blog.getUsers().getUserId());
        blogReponse.setAuthorName(blog.getUsers().getUserName());
        return blogReponse;
    }
}
