package com.gserver.data.editor.ws;

import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.gserver.data.editor.entity.Talent;

public class HelloWorlTest {

	private JaxWsProxyFactoryBean factory;
	private Talent talent;

	@Before
	public void setUp() throws Exception {
		factory = new JaxWsProxyFactoryBean();
		talent = new Talent();
		talent.setName("talent");
	}

	@Test
	public void testRegister() {
		factory.setServiceClass(HelloWorld.class);
		factory.setAddress("http://localhost:8080/ws/HelloWorld");
		HelloWorld service = (HelloWorld) factory.create();
		Talent talent = service.getTalent("talent");
		Assert.assertEquals("talent", talent.getName());
	}

	@Test
	public void testBatchRegister() {
		factory.setServiceClass(HelloWorld.class);
		factory.setAddress("http://localhost:8080/ws/HelloWorld");
		HelloWorld service = (HelloWorld) factory.create();
		// 以上语句的功能 可以通过spring来实现
		Talent talent = new Talent();
		talent.setName("talent");

		String result = service.setTalent(talent);
		Assert.assertEquals("ok", result);
	}
}
