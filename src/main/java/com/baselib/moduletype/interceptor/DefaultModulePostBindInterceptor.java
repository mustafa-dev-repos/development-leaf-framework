package com.baselib.moduletype.interceptor;

import com.baselib.module.ModuleLifeCycleHelper;
import com.baselib.moduletype.Module;

public class DefaultModulePostBindInterceptor implements ModulePostBindInterceptor {

	public void handle(Module module) {

		ModuleLifeCycleHelper.initializeBeans(module);
	}

}
