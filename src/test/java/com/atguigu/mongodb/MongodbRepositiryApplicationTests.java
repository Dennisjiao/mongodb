package com.atguigu.mongodb;

import com.atguigu.mongodb.entity.User;
import com.atguigu.mongodb.repository.UserRepository;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;


@SpringBootTest
class MongodbRepositiryApplicationTests {

    //注入mongoTemplate
    @Autowired
    private UserRepository userRepository;

    //添加操作
    @Test
    public void create() {
        User user = new User();
        user.setName("Lucy");
        user.setId("3");
        user.setAge(20);
        user.setEmail("wad@163.com");
        User save = userRepository.save(user);
        System.out.println(save);
    }

    //查询所有数据
    @Test
    public void findAll(){
        List<User> all = userRepository.findAll();
        System.out.println(all);
    }

    //根据id查询
    @Test
    public void findId(){
        User user = userRepository.findById("3").get();
        System.out.println(user);
    }

    //条件查询(根据年龄，邮箱等)
    @Test
    public void findUserList(){
        //名称是"DennisJiao", 年龄是20

        User user = new User();
        user.setName("DennisJiao");
        user.setAge(20);
        Example<User> userExample = Example.of(user);
        List<User> all = userRepository.findAll(userExample);
        System.out.println(all);
    }

    //模糊查询
    @Test
    public void findLikeUserList(){
        User user = new User();
        user.setName("Jiao");//DennisJiao
        user.setAge(20);
        //设置模糊查询匹配规则
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING) //字符串匹配
                    .withIgnoreCase(true); //忽略大小写
        Example<User> userExample = Example.of(user,matcher); //匹配规则
        List<User> all = userRepository.findAll(userExample);
        System.out.println(all);
    }

    //分页查询
    @Test
    public void findPageUserList(){
        //设置分页参数
        Pageable pageable = PageRequest.of(0,3);//0代表第一页 一共3页
        User user = new User();
        user.setName("DennisJiao");
        user.setAge(20);
        Example<User> userExample = Example.of(user);
        Page<User> page = userRepository.findAll(userExample, pageable);
        //page.get.....()可以得到很多数据
        System.out.println(page);
    }

    //修改操作
    @Test
    public void updateUser(){
        User user = userRepository.findById("3").get();
        user.setAge(20);
        //user.setName("Harry");
        User save = userRepository.save(user);//有id就修改 没有id就是添加
        System.out.println(save);

    }

    //删除操作
    @Test
    public void deleteUser(){
        userRepository.deleteById("1");
    }
}
