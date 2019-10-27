package com.nicehash.main.cache;

import org.infinispan.commons.marshall.AdvancedExternalizer;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Collections;
import java.util.Set;

/**
 * Add this externalizer, so function can be changed; e.g. new imports, etc
 *
 * @author Ales Justin
 */
public class OrdersSummaryFunctionExternalizer implements AdvancedExternalizer<OrdersSummaryFunction> {
    @Override
    public Set<Class<? extends OrdersSummaryFunction>> getTypeClasses() {
        return Collections.singleton(OrdersSummaryFunction.class);
    }

    @Override
    public Integer getId() {
        return 3_000_000;
    }

    @Override
    public void writeObject(ObjectOutput output, OrdersSummaryFunction fn) throws IOException {
        output.write(fn.value.length);
        output.write(fn.value);
    }

    @Override
    public OrdersSummaryFunction readObject(ObjectInput input) throws IOException {
        int length = input.readInt();
        byte[] buffer = new byte[length];
        input.readFully(buffer);
        return new OrdersSummaryFunction(buffer);
    }
}