package br.com.payment.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {
    "br.com.payment.api.model.entity.wallet",
    "br.com.payment.api.model.entity.payment"
})
@EnableJpaRepositories(basePackages = {
    "br.com.payment.api.repository"
})
@SpringBootApplication
public class PaymentApplication {

  public static void main(String[] args) {
    SpringApplication.run(PaymentApplication.class, args);
  }

}

