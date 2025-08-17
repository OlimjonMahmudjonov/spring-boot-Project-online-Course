package uzonlinecourseproject.uzonlineedu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uzonlinecourseproject.uzonlineedu.enums.GeneralLevel;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "course")
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "original_price")
    private Double originalPrice;

    @Column(name = "discount_price")
    private Double discountPrice;

    @Column(name = "discount_end_date")
    private LocalDateTime discountEndDate;

    @Column(name = "is_free")
    private Boolean isFree;

    @Column(name = "duration")
    private String duration;

    @Column(name = "level")
    @Enumerated(EnumType.STRING)
    private GeneralLevel level;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "course")
    private List<Lesson> lessons;

    @OneToMany(mappedBy = "course")
    private List<Review> reviews;

    @OneToMany(mappedBy = "course")
    private List<CourseComment> comments;

    @OneToMany(mappedBy = "course")
    private List<Payment> payments;

    @ManyToMany(mappedBy = "enrolledCourses")
    private List<User> enrolledUsers;

    @ManyToOne
    @JoinColumn(name = "preview_video_id")
    private Video previewVideo;
}