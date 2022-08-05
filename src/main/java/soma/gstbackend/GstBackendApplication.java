package soma.gstbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class GstBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GstBackendApplication.class, args);
    }

}
