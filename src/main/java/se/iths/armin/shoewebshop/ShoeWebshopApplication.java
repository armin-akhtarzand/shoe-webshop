package se.iths.armin.shoewebshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"se.iths.armin.shoewebshop", "se.iths.armin.mailservice"})
public class ShoeWebshopApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShoeWebshopApplication.class, args);
    }

}
