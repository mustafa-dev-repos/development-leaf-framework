package com.baselib.module;

import com.baselib.bean.binder.Binder;
import com.baselib.moduletype.Module;
import com.baselib.moduletype.ModuleType;

//this module will be used by application beans
@ModuleType(order = 0)

public class DefaultModule implements Module {

	public void bindBeans(Binder binder) {
		// TODO Auto-generated method stub

	}

}
