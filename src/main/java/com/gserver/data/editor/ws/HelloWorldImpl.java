package com.gserver.data.editor.ws;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import com.gserver.data.editor.entity.Talent;

@WebService
@SOAPBinding(style = Style.RPC)
public class HelloWorldImpl implements HelloWorld {


	public Talent getTalent(@WebParam(name = "name") String name) {
		Talent t = new Talent();
		t.setName(name);
		System.out.println("Server: " + t.getName());
		return t;
	}

	public String setTalent(Talent talent) {
		System.out.println("Server: " + talent.getName());
		return "ok";
	}
}
