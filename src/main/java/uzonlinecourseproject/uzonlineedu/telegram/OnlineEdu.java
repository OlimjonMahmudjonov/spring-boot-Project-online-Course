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
                    sendMessage(chatId, "avval ro`yxatdan o`ting .. /registration");
                }
            } else if (messageText.equals("/my_courses")) {
                if (isUserRegistration(chatId)) {
                    showMainCourses(chatId);
                    usersStates.put(chatId, TelegramStat.STARTED.toString()); // Holatni qayta o'rnatish
                } else {
                    sendMessage(chatId, "Avval ro`yxatdan o`ting .. /registration");
                }

            } else if (messageText.equals("my_payments")) {
                if (isUserRegistration(chatId)) {
                    showMyPayments(chatId);
                } else {
                    sendMessage(chatId, "avval ro`yxatdan o`ting .. /registration");
                }
            } else if (messageText.equals("/registration")) {
                sendMessage(chatId, "Enter Username.... ");
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
                sendMessage(chatId, "Enter Email.... ");
                usersStates.put(chatId, TelegramStat.AWAITING_EMAIL.toString());
            } else if (current.equals(TelegramStat.AWAITING_EMAIL.toString())) {
                tempRegistrationData.put(chatId, messageText);
                sendMessage(chatId, "Enter Password.... ");
                usersStates.put(chatId, TelegramStat.AWAITING_PASSWORD.toString());
            } else if (current.equals(TelegramStat.AWAITING_PASSWORD.toString())) {
                completeRegistration(chatId, update, messageText);
            } else if (current.equals("/menu")) {
                showMainMenu(chatId);
            } else {
                sendMessage(chatId, "wrong something , correct chose options");
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
            sendMessage(chatId, "Welcome to bot . you  was registration  already . /menu on the click  ");
            usersStates.put(chatId, TelegramStat.STARTED.toString());
            showMainMenu(chatId);
        } else {
            sendMessage(chatId, "welcome to bot , you  was  not  registration , please before registration  do ... /registration   ");
            usersStates.put(chatId, TelegramStat.AWAITING_EMAIL_CHECK.toString());
        }

    }

    private void handleEmailCheckForExistingUser(Long chatId, Update update, String email) throws TelegramApiException {
        Optional<User> byEmail = userRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            User user = byEmail.get();
            user.setTelegramChatId(chatId);
            user.setTelegramUserName(update.getMessage().getFrom().getUserName() != null
                    ? update.getMessage().getFrom().getUserName() : "Anonymous");
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
            sendMessage(chatId, "Username or Email already in use , please agean  ");
            usersStates.put(chatId, TelegramStat.STARTED.toString());
            return;
        }

        User newUser = new User();
        newUser.setUsername(username);
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setTelegramChatId(chatId);
        newUser.setTelegramUserName(update.getMessage().getFrom().getUserName() != null
                ? update.getMessage().getFrom().getUserName() : "Anonymous");
        newUser.setVisible(true);
        newUser.setRoles(GeneralRoles.ROLE_STUDENT);
        newUser.setStatus(GeneralsStatus.ACTIVE);
        newUser.setCreatedDate(LocalDateTime.now());

        userRepository.save(newUser);
        sendMessage(chatId, "Your account has been created  ... ");
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
        String menuText = "Asosiy menyu:\n" +
                "/pay - 🧑‍🏫 Kursni sotib olish\n" +
                "/my_courses - 📂 Mening kurslarim\n" +
                "/my_payments - 💳 Mening to‘lovlarim";
        sendMessage(chatId, menuText);
        usersStates.put(chatId, TelegramStat.STARTED.toString()); // Holatni yangilash
    }

    @SneakyThrows
    private void showMainCourses(Long chatId) {
        User user = getUserByChatId(chatId);
        if (user == null) {
            sendMessage(chatId, "Foydalanuvchi topilmadi!");
            return;
        }
        List<Payment> payments = paymentRepository.findByUserAndPayProgress(user, PayProgress.SUCCESS);
        StringBuilder courses = new StringBuilder("Sizning kurslaringiz:\n");
        if (payments.isEmpty()) {
            courses.append("Hech qanday to‘lov topilmadi!");
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
        StringBuilder sb = new StringBuilder("Sizning  to`lovlaringiz ...");
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
            sendMessage(chatId, " hozirda  kursalr  mavjud emas  , iltmos  keyinroq  uringib ko`rin ");
            return;
        } else {


            StringBuilder sb = new StringBuilder("Courses Menu ");
            for (Course course : courses) {
                String price = course.getIsFree() ? " Discount "
                        : (course.getDiscountPrice() != null && course.getDiscountEndDate().isAfter(LocalDateTime.now())
                        ? " Dollar" + course.getDiscountPrice() : "Dollar" + course.getOriginalPrice());
                sb.append(course.getId()).append("-> ").append(course.getTitle()).append("-").append(price).append("\n");

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
                    sendMessage(chatId, " siz tekin  kursni tanladingiz :" + course.getTitle() + "Amal  qilsih  mudati " + course.getDuration() + "tastiqlash uchun /confirm bosing ");
                    usersStates.put(chatId, TelegramStat.SELECT_FREE.toString());
                } else {
                    String price = course.getDiscountPrice() != null && course.getDiscountEndDate().isAfter(LocalDateTime.now())
                            ? "Dollar" + course.getDiscountPrice() : "Dollar" + course.getOriginalPrice();
                    String paymentMessage = "siz tanlagan  course " + course.getTitle()
                            + "\n Price " + price + "\n To`lov uchun karta  raqam : " + CART_NUMBER;
                    InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                    List<List<InlineKeyboardButton>> rows = new ArrayList<>();
                    InlineKeyboardButton backButton = new InlineKeyboardButton();
                    backButton.setText("Back");
                    backButton.setCallbackData("back_to_menu");
                    rows.add(Collections.singletonList(backButton));
                    markup.setKeyboard(rows);
                    sendMessageWithMarkup(chatId, paymentMessage, markup);
                    savePaymentIgnition(course, "tokenUchun ", chatId);
                    usersStates.put(chatId, TelegramStat.PAYMENT_.toString() + courseId);

                }
            } else {
                sendMessage(chatId, "course not found");
            }
        } catch (NumberFormatException e) {
            sendMessage(chatId, " wrong number , please check course id ");
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }

    }

    private void confirmFreeCourse(Long chatId, User user) throws TelegramApiException {
        Long courseId = userCourseSelect.get(chatId);
        if (courseId == null) {
            sendMessage(chatId, "course tanlanmagan  /pay ni  bosing ");
            return;
        }
        Course course = courseRepository.findById(courseId).orElse(null);
        if (course == null) {
            sendMessage(chatId, "wrong something ....");
        }
        if (course.getIsFree()) {
            sendMessage(chatId, "tabriklaymiz  siz bepul  kursni tanladingiz " + course.getTitle()
                    + " amali qilish  davomiyligi " + course.getDuration());
            usersStates.put(chatId, TelegramStat.STARTED.toString());
            userCourseSelect.remove(chatId);
        } else {
            sendMessage(chatId, " bu kurs tekin emas " + course.getTitle()
                    + " Price " + course.getOriginalPrice());
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
            newUser.setEmail("Anonymous" + chatId + "_email");
            newUser.setTelegramUserName("Anonymous" + chatId);
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
