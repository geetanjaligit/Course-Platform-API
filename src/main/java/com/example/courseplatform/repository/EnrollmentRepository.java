package com.example.courseplatform.repository;

import com.example.courseplatform.model.Enrollment;
import com.example.courseplatform.model.Course;
import com.example.courseplatform.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByUserAndCourse(User user, Course course);

    List<Enrollment> findByUser(User user);

    Optional<Enrollment> findByIdAndUser(Long id, User user);
}
