package com.leaf.framework.dab.interceptor;import com.leaf.framework.dab.context.DefaultProcedureContext;
/** *
 * Interface defining methods of a invocation interceptor that is called just before and right after stored procedure invocation. *
 * @Author Mustafa  */
public interface SQLInterceptor {	/**	 * Method called before stored procedure invocation	 * @param ctx Invocation context	 */
	public void beforeInvoke(DefaultProcedureContext ctx);
	/**	 * Method called after stored procedure invocation	 * @param ctx Invocation context	 */
	public void afterInvoke(DefaultProcedureContext ctx);
}
