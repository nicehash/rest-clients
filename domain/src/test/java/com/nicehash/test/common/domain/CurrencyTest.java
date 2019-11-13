package com.nicehash.test.common.domain;

import com.nicehash.clients.domain.Currency;
import org.junit.Assert;
import org.junit.Test;


public class CurrencyTest {
    @Test
    public void testDefault() {
        for (Currency currency : Currency.values()) {
            Currency alt = currency.getAlt();
            if (alt != null) {
                // Alt's alt is this currency again ...
                Assert.assertSame(currency, alt.getAlt());
            }

            // All test currencies should have alt, right?
            if (currency.isTest()) {
                Assert.assertNotNull(alt);
            }
        }
    }
}
