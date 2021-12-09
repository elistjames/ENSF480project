/*
 * Author(s): Eli, Manjot
 * Documented by: Ryan Sommerville
 * Date created: Nov 30, 2021
 * Last Edited: Dec 6, 2021
 */

package Model.User;

import java.time.LocalDate;

/**
 * A class that holds the information for an email,
 * including from and to, as well as the subject, date,
 * and message.
 */
public class Email {
    private String fromEmail;
    private String toEmail;
    private String subject;
    private LocalDate date;
    private String message;

    public Email(String fromEmail, String toEmail) {
        this.fromEmail = fromEmail;
        this.toEmail = toEmail;
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
