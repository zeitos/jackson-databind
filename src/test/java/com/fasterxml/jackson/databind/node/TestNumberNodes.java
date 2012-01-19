package com.fasterxml.jackson.databind.node;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonNode;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BaseMapTest;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Basic tests for {@link JsonNode} implementations that
 * contain numeric values.
 */
public class TestNumberNodes extends BaseMapTest
{
    public void testInt()
    {
        IntNode n = IntNode.valueOf(1);
        assertStandardEquals(n);
        assertTrue(0 != n.hashCode());
        assertEquals(JsonToken.VALUE_NUMBER_INT, n.asToken());
        assertEquals(JsonParser.NumberType.INT, n.getNumberType());
        assertEquals(1, n.getIntValue());
        assertEquals(1L, n.getLongValue());
        assertEquals(BigDecimal.ONE, n.getDecimalValue());
        assertEquals(BigInteger.ONE, n.getBigIntegerValue());
        assertEquals("1", n.asText());

        assertNodeNumbers(n, 1, 1.0);

        assertTrue(IntNode.valueOf(0).canConvertToInt());
        assertTrue(IntNode.valueOf(Integer.MAX_VALUE).canConvertToInt());
        assertTrue(IntNode.valueOf(Integer.MIN_VALUE).canConvertToInt());

        assertTrue(IntNode.valueOf(0).canConvertToLong());
        assertTrue(IntNode.valueOf(Integer.MAX_VALUE).canConvertToLong());
        assertTrue(IntNode.valueOf(Integer.MIN_VALUE).canConvertToLong());
    }

    public void testLong()
    {
        LongNode n = LongNode.valueOf(1L);
        assertStandardEquals(n);
        assertTrue(0 != n.hashCode());
        assertEquals(JsonToken.VALUE_NUMBER_INT, n.asToken());
        assertEquals(JsonParser.NumberType.LONG, n.getNumberType());
        assertEquals(1, n.getIntValue());
        assertEquals(1L, n.getLongValue());
        assertEquals(BigDecimal.ONE, n.getDecimalValue());
        assertEquals(BigInteger.ONE, n.getBigIntegerValue());
        assertEquals("1", n.asText());

        assertNodeNumbers(n, 1, 1.0);

        // ok if contains small enough value
        assertTrue(LongNode.valueOf(0).canConvertToInt());
        assertTrue(LongNode.valueOf(Integer.MAX_VALUE).canConvertToInt());
        assertTrue(LongNode.valueOf(Integer.MIN_VALUE).canConvertToInt());
        // but not in other cases
        assertFalse(LongNode.valueOf(1L + Integer.MAX_VALUE).canConvertToInt());
        assertFalse(LongNode.valueOf(-1L + Integer.MIN_VALUE).canConvertToInt());

        assertTrue(LongNode.valueOf(0L).canConvertToLong());
        assertTrue(LongNode.valueOf(Long.MAX_VALUE).canConvertToLong());
        assertTrue(LongNode.valueOf(Long.MIN_VALUE).canConvertToLong());
    }

    public void testDouble()
    {
        DoubleNode n = DoubleNode.valueOf(0.25);
        assertStandardEquals(n);
        assertTrue(0 != n.hashCode());
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, n.asToken());
        assertEquals(JsonParser.NumberType.DOUBLE, n.getNumberType());
        assertEquals(0, n.getIntValue());
        assertEquals(0.25, n.getDoubleValue());
        assertNotNull(n.getDecimalValue());
        assertEquals(BigInteger.ZERO, n.getBigIntegerValue());
        assertEquals("0.25", n.asText());

        // 1.6:
        assertNodeNumbers(DoubleNode.valueOf(4.5), 4, 4.5);

        assertTrue(DoubleNode.valueOf(0).canConvertToInt());
        assertTrue(DoubleNode.valueOf(Integer.MAX_VALUE).canConvertToInt());
        assertTrue(DoubleNode.valueOf(Integer.MIN_VALUE).canConvertToInt());
        assertFalse(DoubleNode.valueOf(1L + Integer.MAX_VALUE).canConvertToInt());
        assertFalse(DoubleNode.valueOf(-1L + Integer.MIN_VALUE).canConvertToInt());

        assertTrue(DoubleNode.valueOf(0L).canConvertToLong());
        assertTrue(DoubleNode.valueOf(Long.MAX_VALUE).canConvertToLong());
        assertTrue(DoubleNode.valueOf(Long.MIN_VALUE).canConvertToLong());
    }

    public void testDecimalNode() throws Exception
    {
        DecimalNode n = DecimalNode.valueOf(BigDecimal.ONE);
        assertStandardEquals(n);
        assertTrue(n.equals(new DecimalNode(BigDecimal.ONE)));
        assertEquals(JsonToken.VALUE_NUMBER_FLOAT, n.asToken());
        assertEquals(JsonParser.NumberType.BIG_DECIMAL, n.getNumberType());
        assertTrue(n.isNumber());
        assertFalse(n.isIntegralNumber());
        assertTrue(n.isBigDecimal());
        assertEquals(BigDecimal.ONE, n.getNumberValue());
        assertEquals(1, n.getIntValue());
        assertEquals(1L, n.getLongValue());
        assertEquals(BigDecimal.ONE, n.getDecimalValue());
        assertEquals("1", n.asText());

        // 1.6:
        assertNodeNumbers(n, 1, 1.0);

        assertTrue(DecimalNode.valueOf(BigDecimal.ZERO).canConvertToInt());
        assertTrue(DecimalNode.valueOf(BigDecimal.valueOf(Integer.MAX_VALUE)).canConvertToInt());
        assertTrue(DecimalNode.valueOf(BigDecimal.valueOf(Integer.MIN_VALUE)).canConvertToInt());
        assertFalse(DecimalNode.valueOf(BigDecimal.valueOf(1L + Integer.MAX_VALUE)).canConvertToInt());
        assertFalse(DecimalNode.valueOf(BigDecimal.valueOf(-1L + Integer.MIN_VALUE)).canConvertToInt());

        assertTrue(DecimalNode.valueOf(BigDecimal.ZERO).canConvertToLong());
        assertTrue(DecimalNode.valueOf(BigDecimal.valueOf(Long.MAX_VALUE)).canConvertToLong());
        assertTrue(DecimalNode.valueOf(BigDecimal.valueOf(Long.MIN_VALUE)).canConvertToLong());
    }

    public void testBigIntegerNode() throws Exception
    {
        BigIntegerNode n = BigIntegerNode.valueOf(BigInteger.ONE);
        assertStandardEquals(n);
        assertTrue(n.equals(new BigIntegerNode(BigInteger.ONE)));
        assertEquals(JsonToken.VALUE_NUMBER_INT, n.asToken());
        assertEquals(JsonParser.NumberType.BIG_INTEGER, n.getNumberType());
        assertTrue(n.isNumber());
        assertTrue(n.isIntegralNumber());
        assertTrue(n.isBigInteger());
        assertEquals(BigInteger.ONE, n.getNumberValue());
        assertEquals(1, n.getIntValue());
        assertEquals(1L, n.getLongValue());
        assertEquals(BigInteger.ONE, n.getBigIntegerValue());
        assertEquals("1", n.asText());
        
        // 1.6:
        assertNodeNumbers(n, 1, 1.0);

        BigInteger maxLong = BigInteger.valueOf(Long.MAX_VALUE);
        
        n = BigIntegerNode.valueOf(maxLong);
        assertEquals(Long.MAX_VALUE, n.getLongValue());

        ObjectMapper mapper = new ObjectMapper();
        JsonNode n2 = mapper.readTree(maxLong.toString());
        assertEquals(Long.MAX_VALUE, n2.getLongValue());

        // then over long limit:
        BigInteger beyondLong = maxLong.shiftLeft(2); // 4x max long
        n2 = mapper.readTree(beyondLong.toString());
        assertEquals(beyondLong, n2.getBigIntegerValue());

        assertTrue(BigIntegerNode.valueOf(BigInteger.ZERO).canConvertToInt());
        assertTrue(BigIntegerNode.valueOf(BigInteger.valueOf(Integer.MAX_VALUE)).canConvertToInt());
        assertTrue(BigIntegerNode.valueOf(BigInteger.valueOf(Integer.MIN_VALUE)).canConvertToInt());
        assertFalse(BigIntegerNode.valueOf(BigInteger.valueOf(1L + Integer.MAX_VALUE)).canConvertToInt());
        assertFalse(BigIntegerNode.valueOf(BigInteger.valueOf(-1L + Integer.MIN_VALUE)).canConvertToInt());

        assertTrue(BigIntegerNode.valueOf(BigInteger.ZERO).canConvertToLong());
        assertTrue(BigIntegerNode.valueOf(BigInteger.valueOf(Long.MAX_VALUE)).canConvertToLong());
        assertTrue(BigIntegerNode.valueOf(BigInteger.valueOf(Long.MIN_VALUE)).canConvertToLong());
    }

}