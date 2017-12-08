package com.test;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.assertj.core.util.Lists;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.zhaoguhong.blogsrc.entity.User;


/**
 * @author zhaoguhong
 * @date 2017年11月29日
 */
public class A002SpringJdbcJdbcTemplateTest {
  private JdbcTemplate jdbcTemplate;
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Before
  public void init() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    dataSource.setUrl("jdbc:mysql://localhost:3306/blogsrc?useUnicode=true&characterEncoding=UTF-8");
    dataSource.setUsername("root");
    dataSource.setPassword("zhao");
    jdbcTemplate = new JdbcTemplate(dataSource);
    // 方式1
    namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    // 方式2
    // namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
  }

  @Test
  public void queryTest() {
    String sql = "select * from user";
    List<Map<String, Object>> users = jdbcTemplate.queryForList(sql);
    System.out.println(users);
  }

  @Test
  public void queryByParameterTest() {
    String sql = "select * from user where id =?";
    List<Map<String, Object>> users = jdbcTemplate.queryForList(sql, 1L);
    List<Map<String, Object>> users1 = jdbcTemplate.queryForList(sql, new Object[] {1L});
  }

  @Test
  public void simpleMapperTest() {
    String sql = "select * from user";
    List<User> users = jdbcTemplate.query(sql, new RowMapper<User>() {
      public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        User user = new User();
        // Long id = rs.getLong("id");// 如果id为null,会默认0
        Long id1 = rs.getObject("id") == null ? null : rs.getLong("id");
        user.setId(rs.getObject("id") == null ? null : rs.getLong("id"));
        user.setUserName(rs.getString("user_name"));
        user.setPassword(rs.getString("password"));
        return user;
      }
    });
  }

  /**
   * 测试存储过程
   */
  @Test
  public void proTest() {
    // String sql = "insert into user (user_name,password) VALUES (?, ?)";
    // jdbcTemplate.update(sql, "赵孤鸿", "123456"); // 插入数据
    String userName = jdbcTemplate.execute(new CallableStatementCreator() {
      public CallableStatement createCallableStatement(Connection con) throws SQLException {
        String proc = "{call test(?,?)}";
        CallableStatement cs = con.prepareCall(proc);
        cs.setLong(1, 1L);// 设置输入参数的值 索引从1开始
        cs.registerOutParameter(2, Types.VARCHAR);// 设置输出参数的类型
        return cs;
      }
    }, new CallableStatementCallback<String>() {
      public String doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
        cs.execute();
        return cs.getString(2);// 返回输出参数
      }
    });
    System.out.println(userName);
  }

  @Test
  public void namedParameterJdbcTemplateTest() {
    String sql = "select * from user where id =:id";
    Map<String, Object> parameters = new HashMap<String, Object>();
    parameters.put("id", 1L);
    List<Map<String, Object>> users = namedParameterJdbcTemplate.queryForList(sql, parameters);
    System.out.println(users);
  }

  /**
   * 批量更新测试
   */
  @Test
  public void batchUpdateTest() {
    String sql = "insert into user (user_name,password) VALUES (?, ?)";
    List<User> users = Lists.newArrayList();
    for (int i = 0; i <= 10; i++) {
      User user = new User();
      user.setUserName("xiaoming");
      user.setPassword("123456");
      users.add(user);
    }
    jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps, int i) throws SQLException {
        User user = users.get(i);
        int count = 0;
        ps.setString(++count, user.getUserName());// 索引从1开始
        ps.setString(++count, user.getPassword());
      }

      @Override
      public int getBatchSize() {
        return users.size();
      }
    });
  }

}
