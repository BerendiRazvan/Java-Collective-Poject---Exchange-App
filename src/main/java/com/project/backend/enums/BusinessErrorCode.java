package com.project.backend.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@JsonFormat(shape = Shape.OBJECT)
@Getter
public enum BusinessErrorCode {
  //EDH0001400={E=error; DH=digital hive; 000x=error's number; 400: error code}
  CUSTOMER_NOT_FOUND("EDH0001400", "Customer with given id not found!"),
  ITEM_NOT_FOUND("EDH0002400", "Item with given id not found!"),
  DUPLICATE_DETAILS("EDH0003400","Account with this username/email exists already!"),
  LOGIN_INVALID_DATA("EDH0004400","Invalid email or password!"),
  LOGIN_EMAIL_INCORRECT("EDH0005400","Email might be wrong!"),
  LOGIN_PASSWORD_INCORRECT("EDH0006400","Password might be wrong!"),
  EXCHANGE_NOT_FOUND("EDH0007400","Exchange with given ids not found!"),
  EXCHANGE_CAN_NOT_BE_DONE("EDH0008400","The exchange status is not pending!"),
  DONATION_NOT_FOUND("EDH0008401", "The donation with the given id not found!"),
  DONATION_CAN_NOT_BE_DONE("EDH0008402", "This item is not for donation!");

  private final String errorCode;
  private final String devMsg;
  private final HttpStatus status;

  BusinessErrorCode(String errorCode, String devMsg) {
    this.errorCode = errorCode;
    this.devMsg = devMsg;
    this.status = HttpStatus.resolve(Integer.parseInt(errorCode.substring(errorCode.length() - 3)));
  }
}
