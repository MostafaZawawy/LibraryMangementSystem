package maids.library.demo.exception;

import lombok.Builder;

public class BorrowingRecordNotFoundException extends RuntimeException {
    private final String message;

    @Builder
    public BorrowingRecordNotFoundException(String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

