package com.welton.video.np;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;

import java.net.URLEncoder;

import org.junit.Test;

import com.welton.micro.test.RestControllerTest;

public class NpApiControllerTest extends RestControllerTest {

	@Test
	public void testLogin() throws Exception {
		mockMvc.perform(
				get("/api/np/login")
				.param("host", "192.168.1.80")
				.param("port", "2015")
				.param("username", "test")
				.param("password", "123456")
				)
		.andDo(document("np-net-login"));
	}

	@Test
	public void testLogout() throws Exception {
		mockMvc.perform(
				get("/api/np/logout")
				)
		.andDo(document("np-net-logout"));
	}

	@Test
	public void testTicket() throws Exception {
		mockMvc.perform(
				get("/api/np/ticket")
				.param("host", "192.168.1.80")
				.param("port", "2015")
				.param("username", "test")
				.param("password", "123456")
				)
		.andDo(document("np-net-ticket"));
	}

	@Test
	public void testListCameraString() throws Exception {
		mockMvc.perform(
				get("/api/np/list/camera"))
		.andDo(document("np-net-list-camera"));
	}

	@Test
	public void testListCameraStringInt() throws Exception {
		mockMvc.perform(
				get("/api/np/list/objects")
				.param("type", "8")
				)
		.andDo(document("np-net-list-objects"));
	}

	@Test
	public void testListOffline() throws Exception {
		mockMvc.perform(
				get("/api/np/list/offline"))
		.andDo(document("np-net-list-offline"));
	}

	@Test
	public void testEventSubscribe() throws Exception {
		mockMvc.perform(
				get("/api/np/event/subscribe")
				.param("server", "http://localhost:8080")
				.param("path", "/np/api/event/receive")
				)
		.andDo(document("np-net-event-subscribe"));
	}

	@Test
	public void testEventCancelSubscribe() throws Exception {
		mockMvc.perform(
				get("/api/np/event/cancel"))
		.andDo(document("np-net-event-cancel"));
	}

//	@Test
//	public void testEventReceive() throws Exception {
//		mockMvc.perform(
//				get("/api/np/login"))
//		.andDo(document("np-net-login"));
//	}

	@Test
	public void testPtzControl() throws Exception {
		mockMvc.perform(
				get("/api/np/ptz/control")
				.param("avPath", "0Q0v0Q0v3Wk8C81")
				.param("cmd", "1")
				.param("param", "1")
				)
		.andDo(document("np-net-ptz-control"));
	}

	@Test
	public void testPtzControl3d() throws Exception {
		mockMvc.perform(
				get("/api/np/ptz/control3d")
				.param("avPath", "0Q0v0Q0v3Wk8C81")
				.param("direct", "1")
				.param("x", "1")
				.param("y", "1")
				.param("w", "1")
				.param("h", "1")
				)
		.andDo(document("np-net-ptz-control3d"));
	}

	@Test
	public void testPtzLock() throws Exception {
		mockMvc.perform(
				get("/api/np/ptz/lock")
				.param("avPath", "0Q0v0Q0v3Wk8C81")
				.param("lockTime", "10")
				)
		.andDo(document("np-net-ptz-lock"));
	}

	@Test
	public void testPtzAuxControl() throws Exception {
		mockMvc.perform(
				get("/api/np/ptz/aux")
				.param("avPath", "0Q0v0Q0v3Wk8C81")
				.param("num", "1")
				.param("control", "true")
				)
		.andDo(document("np-net-ptz-aux"));
	}

	@Test
	public void testRecordList() throws Exception {
		mockMvc.perform(
				get("/api/np/record/list")
				.param("avPath", "0Q0v0Q0v3Wk8C81")
				.param("vodType", "1")
				.param("beginTime", "20200228010000")
				.param("endTime", "20200228235959")
				)
		.andDo(document("np-net-record-list"));
	}

}
