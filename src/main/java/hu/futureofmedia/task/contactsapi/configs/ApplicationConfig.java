package hu.futureofmedia.task.contactsapi.configs;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
  @Bean
  PhoneNumberUtil phoneNumberUtil() {
    return PhoneNumberUtil.getInstance();
  }
}
