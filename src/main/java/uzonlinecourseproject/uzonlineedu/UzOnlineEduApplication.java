package uzonlinecourseproject.uzonlineedu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import uzonlinecourseproject.uzonlineedu.telegram.OnlineEdu;

@SpringBootApplication(scanBasePackages = "uzonlinecourseproject.uzonlineedu")
public class UzOnlineEduApplication {

    public static void main(String[] args) {
        SpringApplication.run(UzOnlineEduApplication.class, args);
    }

    @Bean
    public TelegramBotsApi telegramBotsApi( OnlineEdu onlineEdu) throws Exception {
        TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
        botsApi.registerBot(onlineEdu);
        return botsApi;
    }

}
