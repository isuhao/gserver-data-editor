package com.gserver.data.editor.ws;

import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;

import com.gserver.data.editor.entity.Talent;


@WebService
@SOAPBinding(style = Style.RPC)
public interface HelloWorld {
	public Talent getTalent(@WebParam(name = "name") String name);
	
	public String setTalent(Talent talent);
}
