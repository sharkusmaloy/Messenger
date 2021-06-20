package ilya.messenger.entity.repository.services;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;

public class DateFormatter {
  public static String FormatDateTime(Instant creationTime) {
    return DateTimeFormatter.RFC_1123_DATE_TIME.withZone(ZoneOffset.UTC).format(creationTime);
  }
}