package com.baselib.proxy;


/**
 * 
 * @Auhtor: Mustafa   
 */

public class InterceptorBuilder {
	
	private InterceptorChain interceptors;  
	
	private InterceptorChain newChain = null;

	private InterceptorChain previousChain = null;

	public InterceptorBuilder(){
		
		super();
	}

	public InterceptorChain getInterceptorChain() {
		
		return interceptors;
	} 
	
	public void addInterceptorHandler(InterceptorHandler interceptor) {

        //assign first chain for the first item..    
		if (interceptors == null)
			interceptors = new InterceptorChain(interceptor);

        if (previousChain != null)
        {
            previousChain.setNextChain(newChain);
        }

        //assign current as previous for the next step        
        previousChain = interceptors;
   }

}
