package secondtask.customdom.service.impl;

/**
 * Created by Hustler on 27.09.2016.
 */
public class DomFactory {
	private static final DomFactory instance = new DomFactory();
	
	private DomFactory(){}
	
	public static DomFactory getInstance(){
		return instance;
	}
	
	public BaseDomParser create(){
		return new BaseDomParser();
	}

}
