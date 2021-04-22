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
package org.apache.commons.jxpath.ri.model;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.jxpath.AbstractFactory;
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.NestedTestBean;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.TestBean;
import org.apache.commons.jxpath.TestMixedModelBean;

/**
 * Test AbstractFactory.
 *
 * @author Dmitri Plotnikov
 * @version $Revision$ $Date$
 */
public class TestMixedModelFactory {

	public static AbstractFactory mockAbstractFactory1() {
		AbstractFactory mockInstance = spy(AbstractFactory.class);
		doAnswer((stubInvo) -> {
			Object parent = stubInvo.getArgument(2);
			String name = stubInvo.getArgument(3);
			int index = stubInvo.getArgument(4);
			if (name.equals("nestedBean")) {
				((TestBean) parent).setNestedBean(new NestedTestBean("newName"));
				return true;
			} else if (name.equals("beans")) {
				TestBean bean = (TestBean) parent;
				if (bean.getBeans() == null || index >= bean.getBeans().length) {
					bean.setBeans(new NestedTestBean[index + 1]);
				}
				bean.getBeans()[index] = new NestedTestBean("newName");
				return true;
			} else if (name.equals("map")) {
				((TestBean) parent).setMap(new HashMap());
				return true;
			} else if (name.equals("TestKey5")) {
				TestBean tb = new TestBean();
				tb.setNestedBean(null);
				tb.setBeans(null);
				((Map) parent).put(name, tb);
				return true;
			} else if (name.equals("matrix")) {
				int[][] matrix = new int[2][];
				matrix[0] = new int[1];
				((TestMixedModelBean) parent).setMatrix(matrix);
				return true;
			}
			return false;
		}).when(mockInstance).createObject(any(JXPathContext.class), any(Pointer.class), any(Object.class),
				any(String.class), anyInt());
		doReturn(false).when(mockInstance).declareVariable(any(JXPathContext.class), any(String.class));
		return mockInstance;
	}
}
