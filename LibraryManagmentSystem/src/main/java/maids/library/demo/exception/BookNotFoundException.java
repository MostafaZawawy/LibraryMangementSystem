package maids.library.demo.exception;

import lombok.Builder;

@Builder
public class BookNotFoundException extends RuntimeException {
    private final String message;

    @Builder
    public BookNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}