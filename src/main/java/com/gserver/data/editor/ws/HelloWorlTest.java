package com.gserver.data.editor.ws;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;

import com.gserver.data.editor.entity.Talent;


public class HelloWorlTest {

	private static JaxWsProxyFactoryBean factory;

	public static void main(String[] args) throws Exception {
		setUp();
		testGetTalent();
		testSetTalent();
	}

	public static void setUp() throws Exception {
		factory = new JaxWsProxyFactoryBean();
		factory.setServiceClass(HelloWorld.class);
		factory.setAddress("http://localhost:8080/ws/HelloWorld");
	}

	public static void testGetTalent() {
		HelloWorld service = (HelloWorld) factory.create();
		Talent talent = service.getTalent("talent");
		System.out.println("talent".equals(talent.getName()));
	}

	public static void testSetTalent() {
		HelloWorld service = (HelloWorld) factory.create();
		Talent talent = new Talent();
		talent.setName("talent");

		String result = service.setTalent(talent);
		System.out.println("ok".equals(result));
	}
}
