package ra.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.dto.response.BlogReponse;
import ra.model.entity.Blog;
import ra.model.service.BlogService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/v1/blog")
public class BlogController {
    @Autowired
    private BlogService blogService;

    @GetMapping("/getPopularArticles")
    public ResponseEntity<?> getPopularArticles(){
        return  ResponseEntity.ok(blogService.getByPopularArticles());
    }

    @GetMapping("/getRecentlyArticles")
    public ResponseEntity<?> getRecentlyArticles(){
        return ResponseEntity.ok(blogService.getByRecentlyArticles());
    }

    @GetMapping("/getAll")
    public List<BlogReponse> getAll(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "10") int size){
        return blogService.getAll(size,page);
    }

    @GetMapping("/getByBlogId/{blogId}")
    public ResponseEntity<?> getByBlogId(@PathVariable("blogId")int blogId){
        BlogReponse blogReponse = blogService.findById(blogId);
        if (blogReponse!=null){
            return ResponseEntity.ok(blogService.findById(blogId));
        }else {
            return ResponseEntity.ok("Co loi trong qua trinh su ly");
        }

    }
}
