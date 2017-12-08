package com.test;


import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.zhaoguhong.blogsrc.entity.User;



public class A001ReflectTest {
  private static Logger logger = Logger.getLogger(A001ReflectTest.class);
  private static Class<User> userClass = User.class;

  @Test
  public void classTest() throws Exception {
    // 获取Class对象的三种方式
    logger.info("根据类名:  \t" + User.class);
    logger.info("根据对象:  \t" + new User().getClass());
    logger.info("根据全限定类名:\t" + Class.forName("com.test.User"));
    // 常用的方法
    logger.info("获取全限定类名:\t" + userClass.getName());
    logger.info("获取类名:\t" + userClass.getName());
    logger.info("实例化:\t" + userClass.newInstance());
  }

  @Test
  public void constructorTest() throws Exception {
    // 获取全部的构造函数
    Constructor<?>[] constructors = userClass.getConstructors();
    // 取消安全性检查,设置后才可以使用private修饰的构造函数，也可以单独对某个构造函数进行设置
    // Constructor.setAccessible(constructors, true);
    for (int i = 0; i < constructors.length; i++) {
      Class<?> parameterTypesClass[] = constructors[i].getParameterTypes();
      System.out.print("第" + i + "个构造函数:\t (");
      for (int j = 0; j < parameterTypesClass.length; j++) {
        System.out.print(parameterTypesClass[j].getName() + (j == parameterTypesClass.length - 1 ? "" : "\t"));
      }
      logger.info(")");
    }
    // 调用构造函数，实例化对象
    logger.info("实例化，调用无参构造:\t" + constructors[0].newInstance());
    logger.info("实例化，调用有参构造:\t" + constructors[1].newInstance("韦德", 35));
  }

  @Test
  public void fieldTest() throws Exception {
    User user = userClass.newInstance();
    // 获取当前类所有属性
    Field fields[] = userClass.getDeclaredFields();
    // 获取公有属性(包括父类)
    // Field fields[] = cl.getFields();
    // 取消安全性检查,设置后才可以获取或者修改private修饰的属性，也可以单独对某个属性进行设置
    Field.setAccessible(fields, true);
    for (Field field : fields) {
      // 获取属性名 属性值 属性类型
      logger.info(
          "属性名:" + field.getName() + "\t属性值:" + field.get(user) + "  \t属性类型:" + field.getType());
    }
    Field fieldUserName = userClass.getDeclaredField("name");
    // 取消安全性检查,设置后才可以获取或者修改private修饰的属性，也可以批量对所有属性进行设置
    fieldUserName.setAccessible(true);
    fieldUserName.set(user, "韦德");
    // 可以直接对 private 的属性赋值
    logger.info("修改属性后对象:\t" + user);
  }

  @Test
  public void methodTest() throws Exception {
    User user = userClass.newInstance();
    // 获取当前类的所有方法
    Method[] methods = userClass.getDeclaredMethods();
    // 取消安全性检查,设置后才可以调用private修饰的方法，也可以单独对某个方法进行设置
    Method.setAccessible(methods, true);
    // 获取公有方法(包括父类)
    // Method[] methods = userClass.getMethods();
    for (Method method : methods) {
      // 获取方法名和返回类型 获取参数类型：getParameterTypes
      logger.info("方法名:" + method.getName() + " \t返回类型:" + method.getReturnType().getName());
    }
    // 获取无参方法
    Method getMethod = userClass.getDeclaredMethod("getName");
    // 取消安全性检查,设置后才可以调用private修饰的方法，也可以批量对所有方法进行设置
    getMethod.setAccessible(true);
    // 调用无参方法
    logger.info("调用getName方法：" + getMethod.invoke(user));
    // 获取有参方法
    Method setMethod = userClass.getDeclaredMethod("setName", String.class);
    // 取消安全性检查,设置后才可以调用private修饰的方法，也可以批量对所有方法进行设置
    setMethod.setAccessible(true);
    // 调用有参方法
    logger.info("调用setName方法：" + setMethod.invoke(user, "韦德"));
    logger.info("通过set方法修改属性后对象:\t" + user);
  }

}
