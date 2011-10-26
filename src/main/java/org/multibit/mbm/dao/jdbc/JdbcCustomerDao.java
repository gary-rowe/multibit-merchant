package org.multibit.mbm.dao.jdbc;

import org.multibit.mbm.dao.CustomerDao;
import org.multibit.mbm.dao.CustomerNotFoundException;
import org.multibit.mbm.domain.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

@Repository("customerDao")
public class JdbcCustomerDao implements CustomerDao {

  @Autowired
  private JdbcOperations jdbcTemplate;

  private static class CustomerRowMapper implements RowMapper<Customer> {

    @Override
    public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
      UUID id = UUID.fromString(rs.getString("id"));
      String openId = rs.getString("openId");

      // TODO Separate the email from the Customer
      String emailAddress = rs.getString("email");

      Customer customer=new Customer(id, openId);
      customer.setEmailAddress(emailAddress);

      return customer;
    }
  }

  @Override
  public Customer getCustomerByOpenId(String openId) throws CustomerNotFoundException {
    String sql = "select id, openid, email from customers where openId=?";
    try {
      return jdbcTemplate.queryForObject(sql, new CustomerRowMapper(), openId);
    } catch (IncorrectResultSizeDataAccessException ex) {
      throw new CustomerNotFoundException(ex);
    }
  }

  @Override
  public void newCustomer(Customer customer) {
    String sql = "insert into customers (id, openid, email) values (?, ?, ?)";
    jdbcTemplate.update(sql, customer.getId(), customer.getOpenId(), customer.getEmailAddress());
  }
}
