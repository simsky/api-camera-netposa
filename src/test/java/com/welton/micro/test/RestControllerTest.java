package com.welton.micro.test;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.jayway.jsonpath.JsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "target/generated-snippets")
public abstract class RestControllerTest {
	private final static String tokenKey = "Authorization";
	private static Map<String, String> switchTokenValueMap = new HashMap<String, String>();
	private static String curAccount;
	protected static final String DEFAULT_ACCOUNT = "test";
	protected static final String DEFAULT_PWD = "123456";
	protected static String ticket;

	@Autowired
	protected MockMvc mockMvc;


	protected String getTokenValue() {
		return switchTokenValueMap.get(curAccount);
	}

	public void switchAccount(String account, String pwd) throws Exception {
		curAccount = account;
		if (switchTokenValueMap.containsKey(account)) {
			return;
		}
		switchTokenValueMap.put(account, login(account, pwd));
	}

	@BeforeClass
	public static void beforeClass() {
		if (!DEFAULT_ACCOUNT.equals(curAccount)) {
			curAccount = null;
		}
	}

	@Before
	public void setup() throws Exception {
		// 保证系统登录（不关注登录具体登录用户）
		if (curAccount == null) {
			switchAccount(DEFAULT_ACCOUNT, DEFAULT_PWD);
		}
	}

	/**
	 * 统一登录用例
	 * 
	 * @throws Exception
	 */
	public String login(String account, String pwd) throws Exception {
		ResultActions result = mockMvc.perform(MockMvcRequestBuilders.get("/api/np/login")
				.param("host", "192.168.1.80")
				.param("port", "2015")
				.param("username", account)
				.param("password", pwd)
				).andExpect(status().isOk());
		
		String body = result.andReturn().getResponse().getContentAsString();
		return JsonPath.read(body, "$.data");
	}

	public MockHttpServletRequestBuilder get(String uri) {
//		return MockMvcRequestBuilders.get(uri).header(tokenKey, getTokenValue());
		return MockMvcRequestBuilders.get(uri).param("ticket", getTokenValue());
	}

	public MockHttpServletRequestBuilder post(String uri) {
//		return MockMvcRequestBuilders.post(uri).header(tokenKey, getTokenValue());
		return MockMvcRequestBuilders.post(uri).param("ticket", getTokenValue());
	}

	public MockHttpServletRequestBuilder delete(String uri) {
//		return MockMvcRequestBuilders.delete(uri).header(tokenKey, getTokenValue());
		return MockMvcRequestBuilders.delete(uri).param("ticket", getTokenValue());
	}
}
