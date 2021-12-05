package Model.User;

import java.time.LocalDate;

public class Email {
    private String fromEmail;
    private String toEmail;
    private String subject;
    private LocalDate date;
    private String message;

    public Email(String fromEmail, String toEmail) {
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
        this.subject = subject;
        this.message = message;
    }

    public Email(String fromEmail, String toEmail, LocalDate date, String subject,String message) {
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

    public LocalDate getDate() {
        return date;
    }

    public String getSubject() {
        return subject;
    }

    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
