package com.example.courseplatform.repository;

import com.example.courseplatform.model.SubtopicProgress;
import com.example.courseplatform.model.User;
import com.example.courseplatform.model.Subtopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface SubtopicProgressRepository extends JpaRepository<SubtopicProgress, Long> {
    Optional<SubtopicProgress> findByUserAndSubtopic(User user, Subtopic subtopic);

    List<SubtopicProgress> findByUserAndSubtopic_Topic_Course_IdAndCompleted(User user, String courseId,
            boolean completed);

    List<SubtopicProgress> findByUserAndSubtopic_Topic_Course_Id(User user, String courseId);
}
