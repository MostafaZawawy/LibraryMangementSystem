package maids.library.demo.exception;

import lombok.Builder;

@Builder
public class PatronNotFoundException extends RuntimeException {
    private final String message;

    @Builder
    public PatronNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

