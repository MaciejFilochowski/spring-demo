package dev.filochowski.springdemo.email;

public interface EmailSender {
    void send(String recipient, String text);
}
