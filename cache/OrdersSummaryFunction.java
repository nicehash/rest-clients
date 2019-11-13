package com.nicehash.main.cache;

import com.nicehash.platform.proto.main.Main;
import org.infinispan.commons.marshall.SerializeFunctionWith;
import org.infinispan.util.function.SerializableBiFunction;

import java.io.IOException;
import java.io.UncheckedIOException;


@SerializeFunctionWith(OrdersSummaryFunctionExternalizer.class)
public class OrdersSummaryFunction implements SerializableBiFunction<Main.MarketAlgorithm, Main.OrdersSummaryData, Main.OrdersSummaryData> {

    final byte[] value;

    OrdersSummaryFunction(byte[] value) {
        this.value = value;
    }

    public OrdersSummaryFunction(Main.OrdersSummaryData newValue) {
        this(newValue.toByteArray()); // serialize it
	}

	@Override
    public Main.OrdersSummaryData apply(Main.MarketAlgorithm key, Main.OrdersSummaryData current) {
		try {
            Main.OrdersSummaryData newValue = Main.OrdersSummaryData.parseFrom(value);
			if (current == null) {
                return newValue;
			}

            Main.OrdersSummaryData.Builder builder;
            // we just got profs, keep current speeds
            if (newValue.getProfsCount() > 0) {
                builder = current.toBuilder();
                builder.clearProfs(); // remove previous profs
                builder.mergeFrom(newValue); // non-profs should be 0
            } else { // we got speeds, no profs, keep current paying price and profs
                builder = newValue.toBuilder();
                builder.setPayingPrice(current.getPayingPrice());
                builder.addAllProfs(current.getProfsList());
			}
            return builder.build();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
		}
	}
}
