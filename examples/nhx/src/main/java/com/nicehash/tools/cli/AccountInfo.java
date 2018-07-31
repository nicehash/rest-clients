package com.nicehash.tools.cli;

import com.nicehash.exchange.client.ExchangeClient;
import com.nicehash.exchange.client.domain.account.Account;


public class AccountInfo extends AbstractExecutable {

    @Override
    void execute(String[] args) {

        checkRequiredSettings();
        ExchangeClient client = defaultExchangeClient();

        Account account = client.getAccount();
        System.out.println(account);
    }

    public static void main(String[] args) {
        new AccountInfo().execute(args);
    }
}
