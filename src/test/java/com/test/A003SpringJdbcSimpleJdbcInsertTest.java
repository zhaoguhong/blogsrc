package com.test;


import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.zhaoguhong.blogsrc.entity.User;

/**
 * SimpleJdbcInsertTest
 * 
 * @author zhaoguhong
 * @date 2017年12月14日
 */
public class A003SpringJdbcSimpleJdbcInsertTest {
  private SimpleJdbcInsert simpleJdbcInsert;
  private DriverManagerDataSource dataSource;

  @Before
  public void init() {
    dataSource = new DriverManagerDataSource();
    dataSource.setDriverClassName("com.mysql.jdbc.Driver");
    dataSource.setUrl("jdbc:mysql://localhost:3306/blogsrc?useUnicode=true&characterEncoding=UTF-8");
    dataSource.setUsername("root");
    dataSource.setPassword("zhao");
    simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
  }

  /**
   * simpleJdbcInsert新增
   */
  @Test
  public void insertTest() {
    Map<String, Object> parameters = new HashMap<String, Object>(3);
    parameters.put("user_name", "小明");
    parameters.put("password", "123456");
    simpleJdbcInsert.withTableName("user").execute(parameters);
  }

  /**
   * simpleJdbcInsert新增，并返回主键
   */
  @Test
  public void insertAndReturnPrimaryKeyTest() {
    Map<String, Object> parameters = new HashMap<String, Object>(3);
    parameters.put("user_name", "小明");
    parameters.put("password", "123456");
    Number primaryKey = simpleJdbcInsert.withTableName("user")
        .usingGeneratedKeyColumns("id")// 指定主键列名
        .executeAndReturnKey(parameters);
    System.out.println("主键为：" + primaryKey);
  }

  /**
   * simpleJdbcInsert新增，返回主键，并限制插入的列
   */
  @Test
  public void usingColumnsTest() {
    Map<String, Object> parameters = new HashMap<String, Object>(3);
    parameters.put("user_name", "小明");
    parameters.put("password", "123456");
    Number primaryKey = simpleJdbcInsert.withTableName("user")
        .usingColumns("user_name")// 限制输入的列，因为限制只新增用户名，所以password的值不会进数据库
        .usingGeneratedKeyColumns("id")// 指定主键列名
        .executeAndReturnKey(parameters);
    System.out.println("主键为：" + primaryKey);
  }

  /**
   * SqlParameterSource
   */
  @Test
  public void sqlParameterSourceTest() {
    User user = new User();
    user.setUserName("小明");
    user.setPassword("123456");
    // BeanPropertySqlParameterSource
    SqlParameterSource sqlParm = new BeanPropertySqlParameterSource(user);
    simpleJdbcInsert.withTableName("user").execute(sqlParm);

    // MapSqlParameterSource
    simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
    Map<String, Object> mapParameters = new HashMap<String, Object>();
    mapParameters.put("user_name", "小明");
    mapParameters.put("password", "123456");
    SqlParameterSource sqlParmMap = new MapSqlParameterSource(mapParameters);
    simpleJdbcInsert.withTableName("user").execute(sqlParmMap);

    simpleJdbcInsert = new SimpleJdbcInsert(dataSource);
    // 也可以通过MapSqlParameterSource addValue 添加参数
    SqlParameterSource sqlParmMapAdd = new MapSqlParameterSource()
        // addValues(map) 一次添加多个参数
        .addValue("user_name", "小明")// 单个添加
        .addValue("password", "123456");
    simpleJdbcInsert.withTableName("user").execute(sqlParmMapAdd);
  }

}
