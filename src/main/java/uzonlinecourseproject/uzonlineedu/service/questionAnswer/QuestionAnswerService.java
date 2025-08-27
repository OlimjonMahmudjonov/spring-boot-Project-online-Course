package uzonlinecourseproject.uzonlineedu.service.questionAnswer;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uzonlinecourseproject.uzonlineedu.dto.QuestionAnswerDto;
import uzonlinecourseproject.uzonlineedu.entity.Course;
import uzonlinecourseproject.uzonlineedu.entity.QuestionAnswer;
import uzonlinecourseproject.uzonlineedu.entity.User;
import uzonlinecourseproject.uzonlineedu.exception.ResourceNotFoundException;
import uzonlinecourseproject.uzonlineedu.modify.QuestionAnswerCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.QuestionAnswerUpdateDto;
import uzonlinecourseproject.uzonlineedu.repository.CourseRepository;
import uzonlinecourseproject.uzonlineedu.repository.QuestionAnswerRepository;
import uzonlinecourseproject.uzonlineedu.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
public class QuestionAnswerService implements IQuestionAnswerService {
    private final QuestionAnswerRepository questionAnswerRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Override
    public QuestionAnswerDto getQuestionAnswerById(Long id) {
        return questionAnswerRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("QuestionAnswer not found"));
    }

    @Override
    public void deleteQuestionAnswerById(Long id) {
        questionAnswerRepository.findById(id).ifPresentOrElse(questionAnswerRepository::delete,
                () -> { throw new ResourceNotFoundException("QuestionAnswer not found"); });
    }

    @Override
    public QuestionAnswerDto updateQuestionAnswerById(Long id, QuestionAnswerUpdateDto questionAnswerUpdateDto) {
        return questionAnswerRepository.findById(id)
                .map(existingQA -> {
                    if (questionAnswerUpdateDto.getQuestion() != null) {
                        existingQA.setQuestion(questionAnswerUpdateDto.getQuestion());
                    }
                    if (questionAnswerUpdateDto.getAnswer() != null) {
                        existingQA.setAnswer(questionAnswerUpdateDto.getAnswer());
                    }
                   /* if (questionAnswerUpdateDto.getCourseId() != null) {
                        Course course = courseRepository.findById(questionAnswerUpdateDto.getCourseId())
                                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + questionAnswerUpdateDto.getCourseId()));
                        existingQA.setCourse(course);
                    }*/
                    return convertToDto(questionAnswerRepository.save(existingQA));
                }).orElseThrow(() -> new ResourceNotFoundException("QuestionAnswer not found with id " + id));
    }

    @Override
    public QuestionAnswerDto createQuestionAnswer(QuestionAnswerCreateDto questionAnswerCreateDto) {
        User user = userRepository.findById(questionAnswerCreateDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + questionAnswerCreateDto.getUserId()));
        Course course = courseRepository.findById(questionAnswerCreateDto.getCourseId())
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id " + questionAnswerCreateDto.getCourseId()));

        return Optional.of(questionAnswerCreateDto)
                .map(req -> {
                    QuestionAnswer qa = new QuestionAnswer();
                    qa.setQuestion(req.getQuestion());
                    qa.setAnswer(req.getAnswer());
                    qa.setCreatedAt(LocalDateTime.now());
                    qa.setUser(user);
                    qa.setCourse(course);
                    return convertToDto(questionAnswerRepository.save(qa));
                }).orElseThrow(() -> new ResourceNotFoundException("Invalid question answer data"));
    }

    @Override
    public List<QuestionAnswerDto> getAllQuestionAnswers(Pageable pageable) {
        return questionAnswerRepository.findAll(pageable)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionAnswerDto> getQuestionAnswersByUserId(Long userId, Pageable pageable) {
        userRepository.findById(userId).orElseThrow(() ->
                new ResourceNotFoundException("User not found with id " + userId));

        return questionAnswerRepository.findByUserId(userId, pageable)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionAnswerDto> getQuestionAnswersByCourseId(Long courseId, Pageable pageable) {
        courseRepository.findById(courseId).orElseThrow(() ->
                new ResourceNotFoundException("Course not found with id " + courseId));

        return questionAnswerRepository.findByCourseId(courseId, pageable)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionAnswerDto> getRecentQuestionAnswers(int limit, Pageable pageable) {
        return questionAnswerRepository.findAll(pageable)
                .stream()
                .sorted((qaOne, qaTwo) -> qaTwo.getCreatedAt().compareTo(qaOne.getCreatedAt()))
                .limit(limit)
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return questionAnswerRepository.existsById(id);
    }

    private QuestionAnswerDto convertToDto(QuestionAnswer qa) {
        QuestionAnswerDto qaDto = new QuestionAnswerDto();
        qaDto.setId(qa.getId());
        qaDto.setQuestion(qa.getQuestion());
        qaDto.setAnswer(qa.getAnswer());
        qaDto.setCreatedAt(qa.getCreatedAt());
        if (qa.getUser() != null) {
            qaDto.setUserId(qa.getUser().getId());
        }
        if (qa.getCourse() != null) {
            qaDto.setCourseId(qa.getCourse().getId());
        }
        return qaDto;
    }
}
