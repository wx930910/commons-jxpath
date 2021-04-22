/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.jxpath.ri.model.dynabeans;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.jxpath.AbstractFactory;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NestedTestBean;
import org.apache.commons.jxpath.Pointer;

/**
 * Test AbstractFactory.
 *
 * @author Dmitri Plotnikov
 * @version $Revision$ $Date$
 */
public class TestDynaBeanFactory {

	public static AbstractFactory mockAbstractFactory1() {
		AbstractFactory mockInstance = spy(AbstractFactory.class);
		doAnswer((stubInvo) -> {
			JXPathContext context = stubInvo.getArgument(0);
			String name = stubInvo.getArgument(1);
			context.getVariables().declareVariable(name, null);
			return true;
		}).when(mockInstance).declareVariable(any(JXPathContext.class), any(String.class));
		doAnswer((stubInvo) -> {
			Object parent = stubInvo.getArgument(2);
			String name = stubInvo.getArgument(3);
			int index = stubInvo.getArgument(4);
			if (name.equals("nestedBean")) {
				((DynaBean) parent).set("nestedBean", new NestedTestBean("newName"));
				return true;
			} else if (name.equals("beans")) {
				DynaBean bean = (DynaBean) parent;
				Object beans[] = (Object[]) bean.get("beans");
				if (beans == null || index >= beans.length) {
					beans = new NestedTestBean[index + 1];
					bean.set("beans", beans);
				}
				beans[index] = new NestedTestBean("newName");
				return true;
			} else if (name.equals("integers")) {
				DynaBean bean = (DynaBean) parent;
				bean.set("integers", index, new Integer(0));
				return true;
			}
			return false;
		}).when(mockInstance).createObject(any(JXPathContext.class), any(Pointer.class), any(Object.class),
				any(String.class), anyInt());
		return mockInstance;
	}
}
