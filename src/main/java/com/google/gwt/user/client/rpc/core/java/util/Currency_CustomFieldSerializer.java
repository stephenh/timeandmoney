package com.google.gwt.user.client.rpc.core.java.util;

import java.util.Currency;

import com.google.gwt.user.client.rpc.CustomFieldSerializer;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.client.rpc.SerializationStreamReader;
import com.google.gwt.user.client.rpc.SerializationStreamWriter;

public class Currency_CustomFieldSerializer extends CustomFieldSerializer<Currency> {

  public static Currency instantiate(SerializationStreamReader streamReader) throws SerializationException {
    return Currency.getInstance(streamReader.readString());
  }

  public static void deserialize(SerializationStreamReader streamReader, Currency instance) {
  }

  public static void serialize(SerializationStreamWriter streamWriter, Currency instance) throws SerializationException {
    streamWriter.writeString(instance.getCurrencyCode());
  }

  @Override
  public boolean hasCustomInstantiateInstance() {
    return true;
  }

  public Currency instantiateInstance(SerializationStreamReader streamReader) throws SerializationException {
    return instantiate(streamReader);
  }

  @Override
  public void deserializeInstance(SerializationStreamReader streamReader, Currency instance) throws SerializationException {
    deserialize(streamReader, instance);
  }

  @Override
  public void serializeInstance(SerializationStreamWriter streamWriter, Currency instance) throws SerializationException {
    serialize(streamWriter, instance);
  }

}
