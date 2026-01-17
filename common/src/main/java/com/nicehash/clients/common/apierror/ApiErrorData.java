package com.nicehash.clients.common.apierror;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiErrorData {
  public ApiErrorData() {}

  @JsonProperty("error_id")
  private String errorId;

  private List<Error> errors;

  public String getErrorId() {
    return errorId;
  }

  public void setErrorId(String errorId) {
    this.errorId = errorId;
  }

  public List<Error> getErrors() {
    return errors;
  }

  public void setErrors(List<Error> errors) {
    this.errors = errors;
  }

  @Override
  public String toString() {
    return "{" + "errorId='" + errorId + '\'' + ", errors=" + errors + '}';
  }

  public static class Error {
    private int code;
    private String message;

    public Error() {}

    public Error(int code, String message) {
      this.code = code;
      this.message = message;
    }

    public int getCode() {
      return code;
    }

    public void setCode(int code) {
      this.code = code;
    }

    public String getMessage() {
      return message;
    }

    public void setMessage(String message) {
      this.message = message;
    }

    @Override
    public String toString() {
      return "{" + "code=" + code + ", message='" + message + '\'' + '}';
    }
  }
}
