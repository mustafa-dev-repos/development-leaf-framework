package com.baselib.proxy;


/**
 * 
 * @Auhtor: Mustafa   
 */

public class InterceptorChain
{

	private InterceptorChain nextChain;

	private InterceptorHandler interceptorHandler;

    public InterceptorChain(InterceptorHandler interceptorHandler) {
    	
    	this.interceptorHandler = interceptorHandler;
	}

    public final Object start(InterceptorContext interceptorContext) throws Throwable
    {

		Object returnValue = null;

		try {

			interceptorHandler.beforeInvoke(interceptorContext);
			
			returnValue = interceptorContext.getBeforeInvokeResult();
			
			if (returnValue == null) {

				if (nextChain != null) {
					
					returnValue = nextChain.start(interceptorContext);
				}
				else {
					
					returnValue = interceptorContext.getMethod().invoke(interceptorContext.getTarget(), interceptorContext.getArgs());
					
					if (returnValue != null)
						interceptorContext.setInvokeResultValue(returnValue);
				}
			}

			interceptorHandler.afterInvoke(interceptorContext);

			return returnValue;
			
		} catch (Throwable t) {

			interceptorContext.setException(t);
			
			interceptorHandler.afterThrowing(interceptorContext);

			throw t;

		} finally {

			interceptorHandler.afterFinally(interceptorContext);
		}
    }

    public void setNextChain(InterceptorChain nextChain)
    {
        this.nextChain = nextChain;
    }

    public InterceptorChain getNextChain()
    {
        return nextChain;
    }
    
}	
