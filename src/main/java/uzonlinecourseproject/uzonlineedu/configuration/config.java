package uzonlinecourseproject.uzonlineedu.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class config {

    @Bean
    public GroupedOpenApi users() {
        return GroupedOpenApi.builder()
                .group("Users")
                .pathsToMatch("/api/user/**")
                .build();
    }

    @Bean
    public GroupedOpenApi course() {
        return GroupedOpenApi.builder()
                .group("Course")
                .pathsToMatch("/api/course/**")
                .build();
    }

    @Bean
    public GroupedOpenApi category() {
        return GroupedOpenApi.builder()
                .group("Category")
                .pathsToMatch("/api/categories/**")
                .build();
    }

    @Bean
    public GroupedOpenApi video() {
        return GroupedOpenApi.builder()
                .group("Video")
                .pathsToMatch("/api/video/**")
                .build();
    }

    @Bean
    public GroupedOpenApi lesson() {
        return GroupedOpenApi.builder()
                .group("Lesson")
                .pathsToMatch("/api/lesson/**")
                .build();
    }


    @Bean
    public GroupedOpenApi questionAndAnswer() {
        return GroupedOpenApi.builder()
                .group("QuestionAndAnswer")
                .pathsToMatch("/api/question-answers/**")
                .build();
    }

    @Bean
    public GroupedOpenApi blog() {
        return GroupedOpenApi.builder()
                .group("Blog")
                .pathsToMatch("/api/blogs/**")
                .build();
    }

    @Bean
    public GroupedOpenApi comment() {
        return GroupedOpenApi.builder()
                .group("Comment")
                .pathsToMatch("/api/course-comment/**")
                .build();
    }

    @Bean
    public GroupedOpenApi auth() {
        return GroupedOpenApi.builder()
                .group("Auth")
                .pathsToMatch("/api/auth/**")
                .build();
    }

    @Bean
    public GroupedOpenApi payment() {
        return GroupedOpenApi.builder()
                .group("Payment")
                .pathsToMatch("/api/payment/**")
                .build();
    }
 @Bean
    public GroupedOpenApi reviews() {
        return GroupedOpenApi.builder()
                .group("Reviews")
                .pathsToMatch("/api/reviews/**")
                .build();
    }


    @Configuration
    public static class SwaggerConfig {

        @Bean
        public OpenAPI springOpenAPI() {
            return new OpenAPI()
                    .info(new Info()
                            .title("Online Course Platform API")
                            .description("API documentation for the Online Course Platform")
                            .version("1.0.0")
                            .contact(new Contact()
                                    .name("Mahmudjonov Olimjon")
                                    .email("olimjontatu@gmail.com")
                                    .url("https://github.com/OlimjonMahmudjonov"))
                            .license(new License()
                                    .name("Apache 2.0")
                                    .url("https://springdoc.org"))
                            .termsOfService("https://swagger.io/terms/"))
                    .externalDocs(new ExternalDocumentation()
                            .description("SpringShop Wiki Documentation")
                            .url("https://springshop.wiki.github.org/docs"))
                    .servers(List.of(
                                    new Server()
                                            .url("http://localhost:8080")
                                            .description("Local Development")
                            )
                    );
        }
    }
}
