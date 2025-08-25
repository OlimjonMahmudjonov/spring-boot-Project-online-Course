package uzonlinecourseproject.uzonlineedu.telegram;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uzonlinecourseproject.uzonlineedu.entity.Course;
import uzonlinecourseproject.uzonlineedu.entity.Payment;
import uzonlinecourseproject.uzonlineedu.entity.User;
import uzonlinecourseproject.uzonlineedu.enums.GeneralRoles;
import uzonlinecourseproject.uzonlineedu.enums.GeneralsStatus;
import uzonlinecourseproject.uzonlineedu.enums.PayProgress;
import uzonlinecourseproject.uzonlineedu.repository.CourseRepository;
import uzonlinecourseproject.uzonlineedu.repository.PaymentRepository;
import uzonlinecourseproject.uzonlineedu.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;


@Component
@RequiredArgsConstructor
public class OnlineEdu extends TelegramLongPollingBot {
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final PasswordEncoder passwordEncoder;
    private final CourseRepository courseRepository;


    private Map<Long, String> usersStates = new HashMap<>();
    private Map<Long, String> tempRegistrationData = new HashMap<>();
    private Map<Long, Long> userCourseSelect = new HashMap<>();
    private static final String CART_NUMBER = "8600 0969 7785 2004";

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Long chatId = update.getMessage().getChatId();
            String messageText = update.getMessage().getText();
            String current = usersStates.getOrDefault(chatId, TelegramStat.STARTED.toString());
            if (messageText.equals("/start")) {
                handleStartCommand(chatId, update);
            } else if (messageText.equals("/pay") || messageText.equals("/buy")) {
                if (isUserRegistration(chatId)) {
                    displayCourse(chatId);
                    usersStates.put(chatId, TelegramStat.SELECT_COURSE.toString());
                } else {
                    sendMessage(chatId, " \uD83D\uDCEE  Please register first ../registration");
                }
            } else if (messageText.equals("/my_courses")) {
                if (isUserRegistration(chatId)) {
                    showMainCourses(chatId);
                    usersStates.put(chatId, TelegramStat.STARTED.toString());
                } else {
                    sendMessage(chatId, "\uD83D\uDCEE  Please register first .... /registration");
                }

            } else if (messageText.equals("my_payments")) {
                if (isUserRegistration(chatId)) {
                    showMyPayments(chatId);
                } else {
                    sendMessage(chatId, "\uD83D\uDCEE  Please register first ../registration");
                }
            } else if (messageText.equals("/registration")) {
                sendMessage(chatId, " \uD83D\uDC71\u200D♂\uFE0F  Enter Username.... ");
                usersStates.put(chatId, TelegramStat.AWAITING_USERNAME.toString());
            } else if (current.equals(TelegramStat.SELECT_COURSE.toString())) {
                handleCoursesSelection(chatId, messageText);
            } else if (messageText.equals("/confirm") && current.equals(TelegramStat.SELECT_FREE.toString())) {
                confirmFreeCourse(chatId, getUserByChatId(chatId));
                usersStates.put(chatId, TelegramStat.STARTED.toString());
            } else if (current.equals("AWAITING_EMAIL_CHECK")) {
                handleEmailCheckForExistingUser(chatId, update, messageText);
            } else if (current.equals("AWAITING_USERNAME")) {
                tempRegistrationData.put(chatId, messageText);
                sendMessage(chatId, " \uD83D\uDCEA    Enter Email.... ");
                usersStates.put(chatId, TelegramStat.AWAITING_EMAIL.toString());
            } else if (current.equals(TelegramStat.AWAITING_EMAIL.toString())) {
                tempRegistrationData.put(chatId, messageText);
                sendMessage(chatId, " \uD83D\uDDDD  Enter Password.... ");
                usersStates.put(chatId, TelegramStat.AWAITING_PASSWORD.toString());
            } else if (current.equals(TelegramStat.AWAITING_PASSWORD.toString())) {
                completeRegistration(chatId, update, messageText);
            } else if (current.equals("/menu")) {
                showMainMenu(chatId);
            } else {
                sendMessage(chatId, "\uD83E\uDD37  wrong something , correct chose options");
            }
        } else if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getFrom().getId().longValue();
            String dataCallBack = update.getCallbackQuery().getData();
            if (dataCallBack.equals("back_to_menu")) {
                showMainMenu(chatId);
                usersStates.put(chatId, TelegramStat.STARTED.toString());
            } else {
                handleCoursesSelection(chatId, dataCallBack);
            }
        }


    }

    private void handleStartCommand(Long chatId, Update update) throws TelegramApiException {
        Optional<User> optionalUser = userRepository.findByTelegramChatId(chatId);
        if (optionalUser.isPresent()) {
            sendMessage(chatId, " \uD83D\uDCC7  Welcome to the bot! You are already registered. \n \uD83D\uDCDD   /menu  ");
            usersStates.put(chatId, TelegramStat.STARTED.toString());
            showMainMenu(chatId);
        } else {
            sendMessage(chatId, "Welcome to the bot! You are not registered. Please register first using. \n \uD83D\uDCE5  /registration   ");
            usersStates.put(chatId, TelegramStat.AWAITING_EMAIL_CHECK.toString());
        }

    }

    private void handleEmailCheckForExistingUser(Long chatId, Update update, String email) throws TelegramApiException {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            User user = byEmail.get();
            user.setTelegramChatId(chatId);
            user.setTelegramUserName(update.getMessage().getFrom().getUserName() != null
                    ? update.getMessage().getFrom().getUserName() : "\uD83D\uDEB6  Anonymous");
            userRepository.save(user);
            sendMessage(chatId, "you connected account  ... /menu on the click  ");
            showMainMenu(chatId);
            usersStates.put(chatId, TelegramStat.STARTED.toString());
        } else {
            sendMessage(chatId, "you connected did not  account  ... /registration  ");
            usersStates.put(chatId, TelegramStat.STARTED.toString());
        }
    }

    private void completeRegistration(Long chatId, Update update, String password) throws TelegramApiException {
        String username = tempRegistrationData.get(chatId);
        String email = tempRegistrationData.get(chatId + "_email");

        if (userRepository.findByUsername(username).isPresent() || userRepository.findByEmail(email).isPresent()) {
            sendMessage(chatId, " \uD83D\uDC7B   This username or email is already taken. \n Please try again. ");
            usersStates.put(chatId, TelegramStat.STARTED.toString());
            return;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setTelegramChatId(chatId);
        newUser.setTelegramUserName(update.getMessage().getFrom().getUserName() != null
                ? update.getMessage().getFrom().getUserName() : "\uD83D\uDEB6  Anonymous");
        newUser.setVisible(true);
        newUser.setRoles(GeneralRoles.ROLE_STUDENT);
        newUser.setStatus(GeneralsStatus.ACTIVE);
        newUser.setCreatedDate(LocalDateTime.now());

        userRepository.save(newUser);
        sendMessage(chatId, "\uD83D\uDCE9  Your account has been created  ... ");
        tempRegistrationData.remove(chatId);
        tempRegistrationData.remove(chatId + "_email");
        usersStates.put(chatId, TelegramStat.STARTED.toString());
        showMainMenu(chatId);

    }

    private boolean isUserRegistration(Long chatId) {
        return userRepository.findByTelegramChatId(chatId).isPresent();
    }

    private User getUserByChatId(Long chatId) {
        return userRepository.findByTelegramChatId(chatId).orElse(null);
    }


    private void showMainMenu(Long chatId) throws TelegramApiException {
        String menuText = "\uD83D\uDCDA   Menu :\n" +
                "/pay - 🧑‍🏫   Purchase a course\n" +
                "/my_courses - 📂 My Courses \n" +
                "/my_payments - 💳 My Payments ";
        sendMessage(chatId, menuText);
        usersStates.put(chatId, TelegramStat.STARTED.toString());
    }

    @SneakyThrows
    private void showMainCourses(Long chatId) {
        User user = getUserByChatId(chatId);
        if (user == null) {
            sendMessage(chatId, "Username not found ");
            return;
        }
        List<Payment> payments = paymentRepository.findByUserAndPayProgress(user, PayProgress.SUCCESS);
        StringBuilder courses = new StringBuilder("\uD83D\uDDC2  My  Courses :\n");
        if (payments.isEmpty()) {
            courses.append("No payment found!");
        } else {
            for (Payment payment : payments) {
                courses.append(payment.getCourse().getTitle()).append("\n");
            }
        }
        sendMessage(chatId, courses.toString());
    }

    @SneakyThrows
    private void showMyPayments(Long chatId) {
        User user = getUserByChatId(chatId);
        List<Payment> payments = paymentRepository.findByUser(user);
        StringBuilder sb = new StringBuilder("\uD83D\uDCB3   My Payments .... ");
        if (payments.isEmpty()) {
            sb.append("No payments found ");
        } else {
            for (Payment payment : payments) {
                sb.append(" \uD83D\uDCDA Course: ")
                        .append(payment.getCourse().getTitle()).
                        append("  , \uD83D\uDCB8 Price :  ").append(payment.getAmount())
                        .append("\uD83D\uDCCA State : ")
                        .append(payment.getPayProgress()).append("\n");
            }
        }
        sendMessage(chatId, sb.toString());
    }

    private void displayCourse(Long chatId) throws TelegramApiException {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rows = new ArrayList<>();
        List<InlineKeyboardButton> row = new ArrayList<>();
        List<Course> courses = courseRepository.findAll();
        if (courses.isEmpty()) {
            sendMessage(chatId, " No courses are currently available. Please try again later.");
            return;
        } else {


            StringBuilder sb = new StringBuilder(" \uD83D\uDCBB   Courses Menu \n  ");
            for (Course course : courses) {
                String price = course.getIsFree() ? " Discount "
                        : (course.getDiscountPrice() != null && course.getDiscountEndDate().isAfter(LocalDateTime.now())
                        ? " \uD83D\uDCB5  Price  = " + course.getDiscountPrice()  +" $ ": " \uD83D\uDCB5  Price = " + course.getOriginalPrice() +" $ ");
                sb.append(course.getId()).append(" =>  ").append(course.getTitle()).append(" - ").append(price).append("\n");

                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(String.valueOf(course.getId()));
                button.setCallbackData(String.valueOf(course.getId()));
                row.add(button);

                if (row.size() == 2) {
                    rows.add(new ArrayList<>(row));
                    row.clear();
                }
            }
            if (!rows.isEmpty()) {
                rows.add(new ArrayList<>(row));
            }
            InlineKeyboardButton backButton = new InlineKeyboardButton();
            backButton.setText("Back");
            backButton.setCallbackData("back_to_menu");
            rows.add(Collections.singletonList(backButton));
            markup.setKeyboard(rows);
            sendMessageWithMarkup(chatId, sb.toString(), markup);
        }
    }

    private void handleCoursesSelection(Long chatId, String messageText) throws TelegramApiException {
        try {
            Long courseId = Long.parseLong(messageText);
            Optional<Course> courseOptional = courseRepository.findById(courseId);
            if (courseOptional.isPresent()) {
                Course course = courseOptional.get();
                userCourseSelect.put(chatId, course.getId());
                if (course.getIsFree()) {
                    sendMessage(chatId, " You chose a free course ... :" + course.getTitle() + "Expiration date ... " + course.getDuration() + "To confirm \uD83C\uDFE7   /confirm  ");
                    usersStates.put(chatId, TelegramStat.SELECT_FREE.toString());
                } else {
                    String price = course.getDiscountPrice() != null && course.getDiscountEndDate().isAfter(LocalDateTime.now())
                            ? "\uD83D\uDCB5  price = " + course.getDiscountPrice() +" $ " : "\uD83D\uDCB5 price = " + course.getOriginalPrice() +" $ ";
                    String paymentMessage = "Your choose " + course.getTitle()
                            + "\n Price " + price + "\n Card number for payment: \uD83D\uDCB5  " + CART_NUMBER;
                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rows = new ArrayList<>();
                    InlineKeyboardButton backButton = new InlineKeyboardButton();
                    backButton.setText("Back");
                    backButton.setCallbackData("back_to_menu");
                    rows.add(Collections.singletonList(backButton));
                    markup.setKeyboard(rows);
                    sendMessageWithMarkup(chatId, paymentMessage, markup);
                    savePaymentIgnition(course, "this for token  ", chatId);
                    usersStates.put(chatId, TelegramStat.PAYMENT_.toString() + courseId);

                }
            } else {
                sendMessage(chatId, "course not found");
            }
        } catch (NumberFormatException e) {
            sendMessage(chatId, "\uD83E\uDD37  wrong number , please check course id ");
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    private void confirmFreeCourse(Long chatId, User user) throws TelegramApiException {
        Long courseId = userCourseSelect.get(chatId);
        if (courseId == null) {
            sendMessage(chatId, "course choose  \uD83D\uDCB3    /pay ");
            return;
        }
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            sendMessage(chatId, "wrong something ....");
        }
        if (course.getIsFree()) {
            sendMessage(chatId, "Congratulations! You have selected a free course" + course.getTitle()
                    + " Expiration date " + course.getDuration());
            usersStates.put(chatId, TelegramStat.STARTED.toString());
            userCourseSelect.remove(chatId);
        } else {
            sendMessage(chatId, " This course is not free. " + course.getTitle()
                    + "  \uD83D\uDCB8   Price " + course.getOriginalPrice());
        }


    }

    private void sendMessage(Long chatId, String message) throws TelegramApiException { // TODO  fix method
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(message);
        execute(sendMessage);
    }

    private void sendMessageWithMarkup(Long chatId, String text, InlineKeyboardMarkup markup) throws TelegramApiException {
        SendMessage sendMessages = new SendMessage();
        sendMessages.setChatId(chatId.toString());
        sendMessages.setText(text);
        sendMessages.setReplyMarkup(markup);
        try {
            execute(sendMessages);
        } catch (TelegramApiException e) {
            e.printStackTrace();
            sendMessage(chatId, "⁉\uFE0F  wrong something " + e.getMessage());
        }
    }

    private void savePaymentIgnition(Course course, String token, Long chatId) throws TelegramApiException {
        User user = userRepository.findByTelegramChatId(chatId).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail("\uD83D\uDEB6 Anonymous" + chatId + "_email");
            newUser.setTelegramUserName("\uD83D\uDEB6 Anonymous" + chatId);
            newUser.setVisible(true);
            newUser.setRoles(GeneralRoles.ROLE_STUDENT);
            newUser.setCreatedDate(LocalDateTime.now());
            newUser.setStatus(GeneralsStatus.ACTIVE);
            return userRepository.save(newUser);
        });

        Payment payment = new Payment();
        BigDecimal amound = BigDecimal.valueOf(course.getDiscountPrice() != null
                && course.getDiscountEndDate().isAfter(LocalDateTime.now()) ? course.getDiscountPrice() : course.getOriginalPrice());
        payment.setAmount(amound);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setPayProgress(PayProgress.PENDING);
        payment.setTelegramPaymentToken(token);
        payment.setCourse(course);
        payment.setUser(user);
        paymentRepository.save(payment);
    }

    @Override
    public String getBotUsername() {
        return "edu_online_paidbot";
    }

    @Override
    public String getBotToken() {
        return "8440611626:AAEczcVqTrKJTzVy1Xyl6B6UZYUN9ydQfMc";
    }
}
