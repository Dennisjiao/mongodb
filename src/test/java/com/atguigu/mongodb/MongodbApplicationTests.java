package com.atguigu.mongodb;

import com.atguigu.mongodb.entity.User;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.regex.Pattern;


@SpringBootTest
class MongodbApplicationTests {

    //注入mongoTemplate
    @Autowired
    private MongoTemplate mongoTemplate;

    //添加操作
    @Test
    public void create() {
        User user = new User();
        user.setAge(23);
        user.setName("Mark");
        user.setEmail("123456@gmail.com");
        User user1 = mongoTemplate.insert(user);
        System.out.println(user1);
    }

    //查询所有数据
    @Test
    public void findAll(){
        List<User> all = mongoTemplate.findAll(User.class);
        System.out.println(all);
    }

    //根据id查询
    @Test
    public void findId(){
        User userbyid = mongoTemplate.findById("629f3af7d94e3082d9ff94d0", User.class);
        System.out.println(userbyid);
    }

    //条件查询(根据年龄，邮箱等)
    @Test
    public void findUserList(){
        //name = DennisTest and age =23
        //Query是构建一个条件
        Query query = new Query(Criteria.where("name").is("DennisTest").and("age").is(23));
        List<User> users = mongoTemplate.find(query, User.class);
        System.out.println(users);
    }

    //模糊查询
    @Test
    public void findLikeUserList(){
        //name like Denn   regex中传入的是Pattern这个对象

        String name  = "Dennis";
        String regex = String.format("%s%s%s", "^.*", name, ".*$"); //模糊查询中拼接的规则
        //Pattern中的regex参数是匹配的规则
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);

        //模糊查询包含Dennis 并且23岁的
        //Query query = new Query(Criteria.where("name").regex(pattern).and("age").is(23));
        //模糊查询包含Dennis的所有的数据
        Query query = new Query(Criteria.where("name").regex(pattern));
        List<User> users = mongoTemplate.find(query, User.class);
        System.out.println(users);
    }

    //分页查询
    @Test
    public void findPageUserList(){
        //分页查询需要有的参数: 当前页 每页记录数
        int pageNo = 1;
        int pageSize = 3;//每页显示的记录数
        String name  = "Dennis";

        //条件构建部分
        String regex = String.format("%s%s%s", "^.*", name, ".*$"); //模糊查询中拼接的规则
        //Pattern中的regex参数是匹配的规则
        Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);
        Query query = new Query(Criteria.where("name").regex(pattern));

        //分页构建
        //查询记录数
        long count = mongoTemplate.count(query, User.class);//返回的是记录的数量
        //分页
        List<User> users = mongoTemplate.find(
                 query.skip((pageNo - 1) * pageSize).limit(pageSize), User.class);
                //分页开始位置(pageNo - 1) * pageSize)
                //每页显示的记录数limit(pageSize)
        System.out.println(count);  //记录数
        System.out.println(users);  //分页查询的结果
    }

    //修改操作
    @Test
    public void updateUser(){
        //第一步根据id查询 第二步设置修改的值  第三步调用方法实现修改
        //根据id查询
        User user = mongoTemplate.findById("629f3af7d94e3082d9ff94d0", User.class);
        //设置修改值
        user.setName("John");
        user.setEmail("00@163.com");
        //调用方法实现修改
        Query query = new Query(Criteria.where("_id").is(user.getId()));
        Update update = new Update();
        update.set("name",user.getName());
        update.set("email",user.getEmail());
        UpdateResult upsert = mongoTemplate.upsert(query, update, User.class);
        long modifiedCount = upsert.getModifiedCount(); //成功改了几条记录，即为影响的行数
        System.out.println(modifiedCount);
    }

    //删除操作
    @Test
    public void deleteUser(){
        //删除Mark
        Query query = new Query(Criteria.where("_id").is("629fc255bf86a41b76f5296a"));
        DeleteResult remove = mongoTemplate.remove(query, User.class);
        long deletedCount = remove.getDeletedCount();   //删除了几条记录,即为影响的行数
        System.out.println(deletedCount);
    }



}
