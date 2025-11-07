package com.technicalchallenge.validation;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

    private boolean valid;
    private List<String> errors = new ArrayList<>();

    public ValidationResult() {
        this.valid = true; // default: valid unless errors are added
    }

    public boolean isValid() {
        return valid;
        }

    public List<String> getErrors() {
        return errors;
    }

    // Add an error and mark invalid
    public void addError(String message) {
        this.valid = false;
        this.errors.add(message);
    }

    //  For building a ValidationResult directly as valid
    public static ValidationResult ok() {
        return new ValidationResult();
    }

    //  For building a failed result with a message
    public static ValidationResult fail(String message) {
        ValidationResult result = new ValidationResult();
        result.addError(message);
        return result;
    }

    //  Merge results (used when multiple rule sets validate trade)
    public void merge(ValidationResult other) {
        if (!other.isValid()) {
            this.valid = false;
            this.errors.addAll(other.errors);
        }
    }
}
