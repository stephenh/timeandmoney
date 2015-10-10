package com.domainlanguage.money;

import java.util.Currency;

import com.google.gwt.user.client.rpc.CustomFieldSerializer;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

public class Money_CustomFieldSerializer extends CustomFieldSerializer<Money> {

  public static Money instantiate(SerializationStreamReader streamReader) throws SerializationException {
    double amount = streamReader.readDouble();
    Currency currency = Currency.getInstance(streamReader.readString());
    return Money.valueOf(amount, currency);
  }

  public static void deserialize(SerializationStreamReader streamReader, Money instance) {
  }

  public static void serialize(SerializationStreamWriter streamWriter, Money instance) throws SerializationException {
    streamWriter.writeDouble(instance.getAmount().doubleValue());
    streamWriter.writeString(instance.getCurrency().getCurrencyCode());
  }

  @Override
  public boolean hasCustomInstantiateInstance() {
    return true;
  }

  public Money instantiateInstance(SerializationStreamReader streamReader) throws SerializationException {
    return instantiate(streamReader);
  }

  @Override
  public void deserializeInstance(SerializationStreamReader streamReader, Money instance) throws SerializationException {
    deserialize(streamReader, instance);
  }

  @Override
  public void serializeInstance(SerializationStreamWriter streamWriter, Money instance) throws SerializationException {
    serialize(streamWriter, instance);
  }

}
