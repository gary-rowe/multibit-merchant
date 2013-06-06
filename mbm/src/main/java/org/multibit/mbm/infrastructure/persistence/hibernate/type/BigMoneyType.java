package org.multibit.mbm.infrastructure.persistence.hibernate.type;

import org.hibernate.HibernateException;
import org.hibernate.engine.SessionImplementor;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;
import org.joda.money.BigMoney;
import org.joda.money.CurrencyUnit;
import org.multibit.mbm.utils.ObjectUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * <p>Hibernate type to provide the following to application:</p>
 * <ul>
 * <li>Conversion between Hibernate persistence and Joda Money BigMoney type</li>
 * </ul>
 *
 * @since 0.0.1
 *         
 */
public class BigMoneyType implements CompositeUserType {
  @Override
  public String[] getPropertyNames() {
    return new String[]{"amount", "currency"};
  }

  @Override
  public Type[] getPropertyTypes() {
    return new Type[]{BigDecimalType.INSTANCE, StringType.INSTANCE};
  }

  @Override
  public Object getPropertyValue(Object component, int propertyIndex) throws HibernateException {
    if (component == null) {
      return null;
    }
    final BigMoney money = (BigMoney) component;
    switch (propertyIndex) {
      case 0: {
        return money.getAmount();
      }
      case 1: {
        return money.getCurrencyUnit().getCurrencyCode();
      }
      default: {
        throw new HibernateException("Invalid property index [" + propertyIndex + "]");
      }
    }
  }

  @Override
  public void setPropertyValue(Object component, int propertyIndex, Object value) throws HibernateException {
    if (component == null) {
      return;
    }

    final BigMoney money = (BigMoney) component;
    switch (propertyIndex) {
      case 0: {
        money.withAmount((BigDecimal) value);
        break;
      }
      case 1: {
        money.withCurrencyUnit(CurrencyUnit.getInstance((String) value));
        break;
      }
      default: {
        throw new HibernateException("Invalid property index [" + propertyIndex + "]");
      }
    }
  }

  @Override
  public Class returnedClass() {
    return BigMoney.class;
  }

  @Override
  public boolean equals(Object x, Object y) throws HibernateException {
    if (x == y) {
      return true;
    }
    if (x == null) {
      return false;
    }
    if (y == null) {
      return false;
    }
    if (x.getClass() != y.getClass()) {
      return false;
    }
    final BigMoney xMoney = (BigMoney) x;
    final BigMoney yMoney = (BigMoney) y;

    return ObjectUtils.isEqual(
      xMoney.getCurrencyUnit(), yMoney.getCurrencyUnit(),
      xMoney.getAmount(), yMoney.getAmount()
    );
  }

  @Override
  public int hashCode(Object x) throws HibernateException {
    assert (x != null);
    return x.hashCode();
  }

  @Override
  public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
    assert names.length == 2;
    BigDecimal amount = BigDecimalType.INSTANCE.nullSafeGet(rs, names[0], session);
    String currencyCode = StringType.INSTANCE.nullSafeGet(rs, names[1], session);
    return amount == null && currencyCode == null
      ? null
      : BigMoney.of(CurrencyUnit.getInstance(currencyCode), amount);
  }

  @Override
  public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
    if (value == null) {
      BigDecimalType.INSTANCE.set(st, null, index, session);
      StringType.INSTANCE.set(st, null, index + 1, session);
    } else {
      final BigMoney money = (BigMoney) value;
      BigDecimalType.INSTANCE.set(st, money.getAmount(), index, session);
      StringType.INSTANCE.set(st, money.getCurrencyUnit().getCurrencyCode(), index + 1, session);
    }
  }

  @Override
  public Object deepCopy(Object value) throws HibernateException {
    if(value==null)
      return null;
    BigMoney money = (BigMoney)value;
    return BigMoney.of(money);
  }

  @Override
  public boolean isMutable() {
    return true;
  }

  @Override
  public Serializable disassemble(Object value, SessionImplementor session) throws HibernateException {
    return (Serializable) value;
  }

  @Override
  public Object assemble(Serializable cached, SessionImplementor session, Object owner) throws HibernateException {
    return cached;
  }

  @Override
  public Object replace(Object original, Object target, SessionImplementor session, Object owner) throws HibernateException {
    return deepCopy(original);
  }
}