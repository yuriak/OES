package edu.dufe.oes.api;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserAPITest {

	
	UserAPI userAPI=new UserAPI();
	@Test
	public void testGetMyInfoByToken() {
//		System.out.println(userAPI.getMyInfoByToken("49b81718b5889b3652e7d2ab13027978"));
	}

	@Test
	public void testLogin() {
//		System.out.println(userAPI.login("2012", "2012"));
//		for (int i = 1; i <= 10; i++) {
//			System.out.println(userAPI.login("s"+i, "2012"));
//		}
	}

	@Test
	public void testCheckUniqueUser() {
//		fail("Not yet implemented");
//		System.out.println(userAPI.checkUniqueUser("s1"));
	}

	@Test
	public void testRegister() {
//		System.out.println(userAPI.register("s2", "2012", "b", "b", "b", 1, 1, "d@d", "13", 0, "33"));
//		for (int i = 13; i <=14; i++) {
//			System.out.println(userAPI.register(i+"", "2012", "a", "v", "s"+i, i, 1, "a@a", "1", 1, "v"));
//		}
	}

	@Test
	public void testGetUserQuestionByUserName() {
//		fail("Not yet implemented");
//		for (int i = 1; i <= 10; i++) {
//		}
	}

	@Test
	public void testVerifyAnswer() {
//		fail("Not yet implemented");
//		for (int i = 1; i <= 10; i++) {
//			System.out.println(userAPI.verifyAnswer(i, "v"));
//		}
	}

	@Test
	public void testResetUserPassword() {
	}

	@Test
	public void testGetReceivedMessageList() {
//		for (int i = 1; i <=14; i++) {
//			System.out.println(userAPI.getReceivedMessageList(i+""));
//		}
	}

	@Test
	public void testSetMessageOpenStatus() {
//		System.out.println(userAPI.resetUserPassword("v", "49b81718b5889b3652e7d2ab13027978"));
//		for (int i = 1; i <=14; i++) {
//				System.out.println(userAPI.setMessageOpenStatus(i, i+""));
//		}
//		System.out.println(userAPI.setMessageOpenStatus(1, 1+""));
	}

	@Test
	public void testDeleteMessage() {
//		fail("Not yet implemented");
//		for (int i = 1; i <=14; i++) {
//			System.out.println(userAPI.deleteMessage(i, i+""));
//		}
	}

	@Test
	public void testGetSentMessageList() {
//		for (int i = 1; i <=14; i++) {
//			System.out.println(userAPI.getSentMessageList(i+""));
//		}
	}

	@Test
	public void testSendMessage() {
//		fail("Not yet implemented");
//		System.out.println(userAPI.sendMessage("s1", "a", "a", 1, "c69f603641370c13bc859fa0b8c4323d"));
//		for (int i = 1; i <= 14; i++) {
//			for (int j = 1; j <= 14; j++) {
//				System.out.println(userAPI.sendMessage(j+"", j+"t", j+"c", 1, i+""));
//			}
//		}
	}

	@Test
	public void testUpdateUserInfo() {
//		for (int i = 1; i <= 14; i++) {
//			System.out.println(userAPI.updateUserInfo(i+"", i*10+"", i, i+"@c", 0, i+"", i+"m"));
//		}
	}

	@Test
	public void testVerifyPassword() {
//		for (int i = 1; i <=14; i++) {
//			System.out.println(userAPI.verifyPassword("2011", i+""));
//		}
	}

	@Test
	public void testUpdatePassword() {
//		for (int i = 1; i <=14; i++) {
//		System.out.println(userAPI.updatePassword(i+"", "2010"));
//	}
	}

	@Test
	public void testGetAllCollege() {
//		System.out.println(userAPI.getAllCollege());
	}

}
