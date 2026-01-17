package com.nicehash.clients.examples.nhx;

import com.nicehash.clients.exchange.ExchangeClient;
import com.nicehash.clients.exchange.domain.account.Account;

public class AccountInfo extends AbstractExecutable {

  @Override
  protected void execute(String[] args) {

    checkRequiredSettings();
    ExchangeClient client = defaultExchangeClient();

    Account account = client.getAccount();
    System.out.println(account);
  }

  public static void main(String[] args) {
    new AccountInfo().execute(args);
  }
}
