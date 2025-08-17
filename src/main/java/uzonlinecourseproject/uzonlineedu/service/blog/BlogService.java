package uzonlinecourseproject.uzonlineedu.service.blog;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzonlinecourseproject.uzonlineedu.dto.BlogDto;
import uzonlinecourseproject.uzonlineedu.entity.Blog;
import uzonlinecourseproject.uzonlineedu.entity.Course;
import uzonlinecourseproject.uzonlineedu.entity.User;
import uzonlinecourseproject.uzonlineedu.exception.ResourceNotFoundException;
import uzonlinecourseproject.uzonlineedu.modify.BlogCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.BlogUpdateDto;
import uzonlinecourseproject.uzonlineedu.repository.BlogRepository;
import uzonlinecourseproject.uzonlineedu.repository.CourseRepository;
import uzonlinecourseproject.uzonlineedu.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class BlogService implements BlogServiceImpl {

    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    public BlogDto getBlogById(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id " + id));
        return convertToDto(blog);
    }

    @Override
    public void deleteBlogById(Long id) {
        Blog blog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id " + id));
        blogRepository.delete(blog);
    }

    @Override
    public BlogDto updateBlogById(Long id, BlogUpdateDto blogUpdateDto) {
        Blog existingBlog = blogRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Blog not found with id " + id));

        if (blogUpdateDto.getTitle() != null) {
            existingBlog.setTitle(blogUpdateDto.getTitle());
        }
        if (blogUpdateDto.getContent() != null) {
            existingBlog.setContent(blogUpdateDto.getContent());
        }
        if (blogUpdateDto.getCourseId() != null) {
            Course course = courseRepository.findById(blogUpdateDto.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + blogUpdateDto.getCourseId()));
            existingBlog.setCourse(course);
        }

        Blog updatedBlog = blogRepository.save(existingBlog);
        return convertToDto(updatedBlog);
    }

    @Override
    public BlogDto createBlog(BlogCreateDto blogCreateDto) {
        User author = userRepository.findById(blogCreateDto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + blogCreateDto.getAuthorId()));

        Blog blog = new Blog();
        blog.setTitle(blogCreateDto.getTitle());
        blog.setContent(blogCreateDto.getContent());
        blog.setCreatedAt(LocalDateTime.now());
        blog.setAuthor(author);

        if (blogCreateDto.getCourseId() != null) {
            Course course = courseRepository.findById(blogCreateDto.getCourseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + blogCreateDto.getCourseId()));
            blog.setCourse(course);
        }

        Blog savedBlog = blogRepository.save(blog);
        return convertToDto(savedBlog);
    }

    @Override
    public List<BlogDto> getAllBlogs() {
        return blogRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BlogDto> getAllBlogs(Pageable pageable) {
        return blogRepository.findAll(pageable)
                .map(this::convertToDto);
    }

    @Override
    public List<BlogDto> getBlogsByAuthorId(Long authorId) {
        userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + authorId));

        return blogRepository.findByAuthorId(authorId, Pageable.unpaged())
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BlogDto> getBlogsByAuthorId(Long authorId, Pageable pageable) {
        userRepository.findById(authorId)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id " + authorId));

        return blogRepository.findByAuthorId(authorId, pageable)
                .map(this::convertToDto);
    }

    @Override
    public List<BlogDto> getRecentBlogs(int limit) {
        return blogRepository.findAll()
                .stream()
                .sorted((b1, b2) -> b2.getCreatedAt().compareTo(b1.getCreatedAt()))
                .limit(limit)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return blogRepository.existsById(id);
    }

    private BlogDto convertToDto(Blog blog) {
        BlogDto dto = new BlogDto();
        dto.setId(blog.getId());
        dto.setTitle(blog.getTitle());
        dto.setContent(blog.getContent());
        dto.setCreatedAt(blog.getCreatedAt());
        if (blog.getAuthor() != null) {
            dto.setAuthorId(blog.getAuthor().getId());
        }
        if (blog.getCourse() != null) {
            dto.setCourseId(blog.getCourse().getId());
        }
        return dto;
    }
}
