package com.karnty.training.backend;

import com.karnty.training.backend.entity.Address;
import com.karnty.training.backend.entity.Social;
import com.karnty.training.backend.entity.User;
import com.karnty.training.backend.exception.BaseException;
import com.karnty.training.backend.service.AddressService;
import com.karnty.training.backend.service.SocialService;
import com.karnty.training.backend.service.UserService;
import com.karnty.training.backend.util.SecurityUtil;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestUserService {

	@Autowired
	private UserService userService;

	@Autowired
	private SocialService socialService;
	@Autowired
	private AddressService addressService;

	@Order(1)
	@Test
	void testCreate() throws BaseException {
		String token = SecurityUtil.generateToken();
		User user = userService.createUser(
				testCreateData.email,
				testCreateData.password,
				testCreateData.name,
				token,
				new Date()
		);
		Assertions.assertNotNull(user);
		Assertions.assertEquals(testCreateData.email,user.getEmail());
		boolean isMatched =   userService.matchPassword(testCreateData.password,user.getPassword());
		Assertions.assertTrue(isMatched);
		Assertions.assertEquals(testCreateData.name,user.getName());
	}
	@Order(2)
	@Test
	void testUpdate() throws BaseException {
		Optional<User> opt = userService.findByEmail(testCreateData.email);
		Assertions.assertTrue(opt.isPresent());

		User user = opt.get();
		User userUpdated = userService.updateName(user.getId(),testUpdateData.name);
		Assertions.assertNotNull(userUpdated);
		Assertions.assertEquals(testUpdateData.name,userUpdated.getName());
	}
	@Order(3)
	@Test
	void testCreateSocial() throws BaseException {
		Optional<User> opt = userService.findByEmail(testCreateData.email);
		Assertions.assertTrue(opt.isPresent());

		User user = opt.get();
		Social social = user.getSocial();
		Assertions.assertNull(social);
		social = socialService.create(user,
				testSocialData.facebook,
				testSocialData.line,
				testSocialData.instagram,
				testSocialData.tiktok);

		Assertions.assertNotNull(social);
		Assertions.assertEquals(testSocialData.facebook,social.getFacebook());
	}
	@Order(4)
	@Test
	void testCreateAddress() {
		Optional<User> opt = userService.findByEmail(testCreateData.email);
		Assertions.assertTrue(opt.isPresent());

		User user = opt.get();
		List<Address> addresses = user.getAddresses();
		Assertions.assertTrue(addresses.isEmpty());

		createAddress(user, AddressTestCreateData.line1, AddressTestCreateData.line2, AddressTestCreateData.zipcode);
		createAddress(user, AddressTestCreateData2.line1, AddressTestCreateData2.line2, AddressTestCreateData2.zipcode);
	}
	public void createAddress(User user,String line1,String line2,String zipcode){
		Address address = addressService.create(user,
				line1,
				line2,
				zipcode);
		Assertions.assertNotNull(address);
		Assertions.assertEquals(line1,address.getLine1());
		Assertions.assertEquals(line2,address.getLine2());
		Assertions.assertEquals(zipcode,address.getZipCode());

	}
	@Order(9)
	@Test
	void testDelete() {
		Optional<User> opt = userService.findByEmail(testCreateData.email);
		Assertions.assertTrue(opt.isPresent());

		User user = opt.get();

		//check social
		Social social = user.getSocial();
		Assertions.assertNotNull(social);
		Assertions.assertEquals(testSocialData.facebook,social.getFacebook());

		//check address
		List<Address> addresses = user.getAddresses();
		Assertions.assertFalse(addresses.isEmpty());
		Assertions.assertEquals(2,addresses.size());

		userService.deleteById(user.getId());

		Optional<User> optDeleted = userService.findByEmail(testCreateData.email);
		Assertions.assertTrue(optDeleted.isEmpty());

	}
	interface testCreateData{
		String email = "karncomsci25@gmail.com";
		String password = "12345678Xyz";
		String name = "Karn Comsci";
	}
	interface testUpdateData{
		String name =  "Karnty Dev";
	}
	interface testSocialData{
		String facebook = "i am karn";
		String line = "";
		String instagram = "";
		String tiktok = "";
	}
	interface AddressTestCreateData{
		String line1 = "3/26";
		String line2 = "Bangkok";
		String zipcode = "10400";
	}
	interface AddressTestCreateData2{
		String line1 = "3/27";
		String line2 = "Bangkok";
		String zipcode = "10401";
	}
}

