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
package org.apache.commons.jxpath.ri.model.dom;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import org.apache.commons.jxpath.AbstractFactory;
import org.apache.commons.jxpath.JXPathContext;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * Test AbstractFactory.
 *
 * @author Dmitri Plotnikov
 * @version $Revision$ $Date$
 */
public class TestDOMFactory {

	public static AbstractFactory mockAbstractFactory1() {
		AbstractFactory mockInstance = spy(AbstractFactory.class);
		doReturn(false).when(mockInstance).declareVariable(any(), any());
		doAnswer((stubInvo) -> {
			JXPathContext context = stubInvo.getArgument(0);
			Object parent = stubInvo.getArgument(2);
			String name = stubInvo.getArgument(3);
			int index = stubInvo.getArgument(4);
			if (name.equals("location") || name.equals("address") || name.equals("street")) {
				addDOMElement((Node) parent, index, name, null);
				return true;
			}
			if (name.startsWith("price:")) {
				String namespaceURI = context.getNamespaceURI("price");
				addDOMElement((Node) parent, index, name, namespaceURI);
				return true;
			}
			return false;
		}).when(mockInstance).createObject(any(), any(), any(), any(), anyInt());
		return mockInstance;
	}

	private static void addDOMElement(Node parent, int index, String tag, String namespaceURI) {
		Node child = parent.getFirstChild();
		int count = 0;
		while (child != null) {
			if (child.getNodeName().equals(tag)) {
				count++;
			}
			child = child.getNextSibling();
		}
		while (count <= index) {
			Document doc = parent.getOwnerDocument();
			Node newElement;
			if (namespaceURI == null) {
				newElement = doc.createElement(tag);
			} else {
				newElement = doc.createElementNS(namespaceURI, tag);
			}
			parent.appendChild(newElement);
			count++;
		}
	}
}
