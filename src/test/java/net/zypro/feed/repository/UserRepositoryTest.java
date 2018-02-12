package net.zypro.feed.repository;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Entity;

import net.zypro.feed.Application;
import net.zypro.feed.domain.User;




import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes =Application.class)
@WebAppConfiguration
//@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class UserRepositoryTest extends AbstractJUnit4SpringContextTests  { //ģ������� 
	//private UserRepository userRepository=new UserRepository();
	@Autowired
	//@Resource(name="userRepository")
	private UserRepository userRepository;
	
//public UserRepository getUserRepository() {
//		return userRepository;
//	}

	public void setUserRepository(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	/*@Test
	public void testAdd() {
		User user=new User();
		user.setUsername("517334815_openID");
		user.setSource("QQ");
		user.setNickname("�Ǻ�");
		user.setSex("Ů");
		user.setAddress("����");
		user.setAge(20);
		//int id=userRepository.add(user);
//		if(userRepository==null){
//			System.out.print("null");
//		}
		User u=userRepository.save(user);   //userRepositoryδװ��
		System.out.println(u.getId());
		//System.out.println("����û���ID="+id);
		
	}
*/
	/*@Test
	public void testFindByUsername() {
		User u=userRepository.findByUsername("517334815_openID");
		System.out.println("nickname="+u.getNickname());
		System.out.println("source="+u.getSource());
		System.out.println("address="+u.getAddress());
		System.out.println("age="+u.getAge());
		System.out.println("sex="+u.getSex());
	}
	*/

	/*@Test
      public void testUpdate() {
		User user=new User();
		user.setId(3);
		user.setUsername("517334815_openId");
		user.setNickname("����");
		user.setSource("QQ");
		user.setAddress("�人");
		user.setAge(19);
		user.setSex("��");
		userRepository.update(user.getNickname(), user.getSource(), user.getAddress(), user.getAge(), user.getSex(), user.getId());
		List<User> list=new ArrayList<User>();
		list=userRepository.findAll();
		for(User u:list){
			System.out.println("ID="+u.getId());
			System.out.println("username="+u.getUsername());
			System.out.println("nickname="+u.getNickname());
			System.out.println("source="+u.getSource());
			System.out.println("address="+u.getAddress());
			System.out.println("age="+u.getAge());
			System.out.println("sex="+u.getSex());
		}
	}
*/	

/*@Test
	public void testFindAll() {
		List<User> users=new ArrayList<User>();
		users=userRepository.findAll();
		for(User u:users){
			System.out.println("ID="+u.getId());
			System.out.println("username="+u.getUsername());
			System.out.println("nickname="+u.getNickname());
			System.out.println("source="+u.getSource());
			System.out.println("address="+u.getAddress());
			System.out.println("age="+u.getAge());
			System.out.println("sex="+u.getSex());
		}
	}
*/
	@Test
	public void testFindById(){
		User u=new User();
		u=userRepository.findById(8);
		System.out.println(u.getNickname());
	}
}
