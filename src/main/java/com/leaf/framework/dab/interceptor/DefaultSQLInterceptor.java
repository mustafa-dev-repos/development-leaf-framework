package com.leaf.framework.dab.interceptor;import com.leaf.framework.dab.annotation.Procedure;import com.leaf.framework.dab.context.DefaultProcedureContext;
/** * It can be overriden by specifying custom implementation in {@link Procedure} annotation *   * @Author Mustafa  * *///TODO : replace with generic one..
public class DefaultSQLInterceptor implements SQLInterceptor {
	public void beforeInvoke(DefaultProcedureContext ctx) {
	}
	public void afterInvoke(DefaultProcedureContext ctx) {
	}
}
