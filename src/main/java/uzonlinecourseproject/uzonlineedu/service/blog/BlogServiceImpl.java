package uzonlinecourseproject.uzonlineedu.service.blog;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import uzonlinecourseproject.uzonlineedu.dto.BlogDto;
import uzonlinecourseproject.uzonlineedu.modify.BlogCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.BlogUpdateDto;

import java.util.List;

public interface BlogServiceImpl {
    BlogDto getBlogById(Long id);

    void deleteBlogById(Long id);

    BlogDto updateBlogById(Long id, BlogUpdateDto blogUpdateDto);

    BlogDto createBlog(BlogCreateDto blogCreateDto);

    List<BlogDto> getAllBlogs();

    Page<BlogDto> getAllBlogs(Pageable pageable);

    List<BlogDto> getBlogsByAuthorId(Long authorId);

    Page<BlogDto> getBlogsByAuthorId(Long authorId, Pageable pageable);

    List<BlogDto> getRecentBlogs(int limit);

    boolean existsById(Long id);
}
