package codefod.com.springbootmentor;

import codefod.com.springbootmentor.service.CarService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class SpringBootMentorApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(SpringBootMentorApplication.class, args);
        CarService carService = context.getBean(CarService.class);
        carService.drive();
    }

}
