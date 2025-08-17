package uzonlinecourseproject.uzonlineedu.service.questionAnswer;

import org.springframework.data.domain.Pageable;
import uzonlinecourseproject.uzonlineedu.dto.QuestionAnswerDto;
import uzonlinecourseproject.uzonlineedu.modify.QuestionAnswerCreateDto;
import uzonlinecourseproject.uzonlineedu.modify.QuestionAnswerUpdateDto;


import java.util.List;

public interface IQuestionAnswerService {

    QuestionAnswerDto getQuestionAnswerById(Long id);

    void deleteQuestionAnswerById(Long id);

    QuestionAnswerDto updateQuestionAnswerById(Long id, QuestionAnswerUpdateDto questionAnswerUpdateDto);

    QuestionAnswerDto createQuestionAnswer(QuestionAnswerCreateDto questionAnswerCreateDto);

    List<QuestionAnswerDto> getAllQuestionAnswers(Pageable pageable);

    List<QuestionAnswerDto> getQuestionAnswersByUserId(Long userId, Pageable pageable);

    List<QuestionAnswerDto> getQuestionAnswersByCourseId(Long courseId, Pageable pageable);

    List<QuestionAnswerDto> getRecentQuestionAnswers(int limit, Pageable pageable);


    boolean existsById(Long id);
}
