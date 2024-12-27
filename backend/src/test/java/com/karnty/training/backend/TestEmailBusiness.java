package com.karnty.training.backend;

import com.karnty.training.backend.business.EmailBusiness;
import com.karnty.training.backend.entity.Address;
import com.karnty.training.backend.entity.Social;
import com.karnty.training.backend.entity.User;
import com.karnty.training.backend.exception.BaseException;
import com.karnty.training.backend.service.AddressService;
import com.karnty.training.backend.service.SocialService;
import com.karnty.training.backend.service.UserService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestEmailBusiness {

	@Autowired
	private EmailBusiness emailBusiness;


	@Order(1)
	@Test
	void testCreate() throws BaseException {
		emailBusiness.sendActivatedUserEmail(TestData.email,TestData.name,TestData.token);
	}
	interface TestData{
		String email = "karncomsci26@gmail.com";
		String name = "Aiyakarn Yambun";
		String token = "k#@!@#879456123";
	}
}

