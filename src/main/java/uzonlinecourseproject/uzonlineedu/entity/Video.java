package uzonlinecourseproject.uzonlineedu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "video")
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "original_filename")
    private String originalFilename;

    @Column(name = "download_url")
    private String downloadUrl;

    @Column(name = "size")
    private Long size;

    @Column(name = "upload_date")
    private LocalDateTime uploadDate;

    @OneToMany(mappedBy = "video")
    private List<Lesson> lessons;

    @OneToMany(mappedBy = "previewVideo")
    private List<Course> courses;
}