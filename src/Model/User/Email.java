package Model.User;

import java.sql.Date;
import java.time.LocalDate;

public class Email {
    private String fromEmail;
    private String toEmail;
    private String subject;
    private Date date;
    private String message;

    public Email(String fromEmail, String toEmail, String subject, String message) {
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.subject = subject;
        this.message = message;
        date = Date.valueOf(LocalDate.now());
    }

    public Email(String fromEmail, String toEmail, Date date, String subject,String message) {
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.subject = subject;
        this.message = message;
        this.date = date;
    }


    public String getFromEmail() {
        return fromEmail;
    }

    public String getToEmail() {
        return toEmail;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    public String getSubject() {
        return subject;
    }
}
