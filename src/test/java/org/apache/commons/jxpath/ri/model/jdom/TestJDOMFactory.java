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
package org.apache.commons.jxpath.ri.model.jdom;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.util.List;

import org.apache.commons.jxpath.AbstractFactory;
import org.apache.commons.jxpath.JXPathContext;
import org.jdom.Element;

/**
 * Test AbstractFactory.
 *
 * @author Dmitri Plotnikov
 * @version $Revision$ $Date$
 */
public class TestJDOMFactory {

	public static AbstractFactory mockAbstractFactory1() {
		AbstractFactory mockInstance = spy(AbstractFactory.class);
		doAnswer((stubInvo) -> {
			JXPathContext context = stubInvo.getArgument(0);
			Object parent = stubInvo.getArgument(2);
			String name = stubInvo.getArgument(3);
			int index = stubInvo.getArgument(4);
			if (name.equals("location") || name.equals("address") || name.equals("street")) {
				addJDOMElement((Element) parent, index, name, null);
				return true;
			}
			if (name.startsWith("price:")) {
				String namespaceURI = context.getNamespaceURI("price");
				addJDOMElement((Element) parent, index, name, namespaceURI);
				return true;
			}
			return false;
		}).when(mockInstance).createObject(any(), any(), any(), any(), anyInt());
		doReturn(false).when(mockInstance).declareVariable(any(), any());
		return mockInstance;
	}

	private static void addJDOMElement(Element parent, int index, String tag, String namespaceURI) {
		List children = parent.getContent();
		int count = 0;
		for (int i = 0; i < children.size(); i++) {
			Object child = children.get(i);
			if (child instanceof Element && ((Element) child).getQualifiedName().equals(tag)) {
				count++;
			}
		}
		while (count <= index) {
			Element newElement;
			if (namespaceURI != null) {
				String prefix = tag.substring(0, tag.indexOf(':'));
				tag = tag.substring(tag.indexOf(':') + 1);
				newElement = new Element(tag, prefix, namespaceURI);
			} else {
				newElement = new Element(tag);
			}
			parent.addContent(newElement);
			count++;
		}
	}
}
