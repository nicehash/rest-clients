package com.nicehash.test.common.domain;

import com.nicehash.common.domain.Currency;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Ales Justin
 */
public class CurrencyTest {
    @Test
    public void testDefault() {
        for (Currency currency : Currency.values()) {
            Assert.assertSame(currency.getAlt(), currency.getAlt().getAlt().getAlt());
        }
    }
}
