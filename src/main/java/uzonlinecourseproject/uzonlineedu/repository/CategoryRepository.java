package uzonlinecourseproject.uzonlineedu.repository;

import aj.org.objectweb.asm.commons.Remapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uzonlinecourseproject.uzonlineedu.entity.Category;

public interface CategoryRepository  extends JpaRepository<Category, Long> {

    Page<Category> findByNameContainingIgnoreCase(String name, Pageable pageable);}
