package uzonlinecourseproject.uzonlineedu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uzonlinecourseproject.uzonlineedu.resAndreq.ApiResponse;
import uzonlinecourseproject.uzonlineedu.dto.BlogDto;
import uzonlinecourseproject.uzonlineedu.modify.BlogCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.BlogUpdateDto;
import uzonlinecourseproject.uzonlineedu.exception.ResourceNotFoundException;
import uzonlinecourseproject.uzonlineedu.service.blog.BlogServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/blogs")
@RequiredArgsConstructor
public class BlogController {

    private final BlogServiceImpl blogService;

    @GetMapping("/get/{id}")
    public ResponseEntity<ApiResponse> getBlogById(@PathVariable Long id) {
        try {
            BlogDto blogDto = blogService.getBlogById(id);
            return ResponseEntity.ok(new ApiResponse("Blog found", blogDto));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteBlogById(@PathVariable Long id) {
        try {
            blogService.deleteBlogById(id);
            return ResponseEntity.ok(new ApiResponse("Blog deleted successfully", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/get/all")
    public ResponseEntity<ApiResponse> getAllBlogs() {
        List<BlogDto> blogs = blogService.getAllBlogs();
        return ResponseEntity.ok(new ApiResponse("All blogs fetched", blogs));
    }

    @GetMapping("/exists/{id}")
    public ResponseEntity<ApiResponse> existsById(@PathVariable Long id) {
        boolean exists = blogService.existsById(id);
        return ResponseEntity.ok(new ApiResponse("Existence check completed", exists));
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<ApiResponse> getBlogsByAuthorId(@PathVariable Long authorId) {
        List<BlogDto> blogs = blogService.getBlogsByAuthorId(authorId);
        return ResponseEntity.ok(new ApiResponse("Blogs fetched by author ID", blogs));
    }

    @GetMapping("/recent")
    public ResponseEntity<ApiResponse> getRecentBlogs(@RequestParam(defaultValue = "5") int limit) {
        List<BlogDto> recentBlogs = blogService.getRecentBlogs(limit);
        return ResponseEntity.ok(new ApiResponse("Recent blogs fetched", recentBlogs));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse> updateBlog(
            @PathVariable Long id,
            @Valid @RequestBody BlogUpdateDto blogUpdateDto) {
        try {
            BlogDto updatedBlog = blogService.updateBlogById(id, blogUpdateDto);
            return ResponseEntity.ok(new ApiResponse("Blog updated successfully", updatedBlog));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createBlog(@Valid @RequestBody BlogCreateDto blogCreateDto) {
        try {
            BlogDto createdBlog = blogService.createBlog(blogCreateDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new ApiResponse("Blog created successfully", createdBlog));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), null));
        }
    }
}
