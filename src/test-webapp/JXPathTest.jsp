<%@ page language="java" %>
<%@ page import="org.apache.commons.jxpath.JXPathContext" %>
<%@ page import="org.apache.commons.jxpath.servlet.JXPathServletContexts" %>

<%!
    private String assertEqual(
            String title,
            Object actual,
            Object expected) 
    {
        if ((actual == null && expected == null)
                || (actual != null && actual.equals(expected))) {
            return "<p>" + title + ": Ok";
        }
        else {
            return "<p><font color=red>" + title + ": Failure</font>";
        }
    }
%>

<html>
<head>
 <title>JXPathServletContext</title>
</head>
<body>
  <%
    pageContext.setAttribute("page", "page");
    pageContext.getServletContext().setAttribute("app", "app");
    request.setAttribute("request", "request");
    session.setAttribute("session", "session");

    JXPathContext context = JXPathServletContexts.getPageContext(pageContext); 
    context.setLenient(true);
  %>
  <h1>JXPathServletContexts JSP PageContext Context Test</h1>

  <%= assertEqual("Page Scope", context.getValue("page"), "page") %>
  <%= assertEqual("Request Scope", context.getValue("request"), "request") %>
  <%= assertEqual("Session Scope", context.getValue("session"), "session") %>
  <%= assertEqual("Application Scope", context.getValue("app"), "app") %>
  <%= assertEqual("Explicit Page Scope", context.getValue("$page/page"), "page") %>
  <%= assertEqual("Explicit Request Scope", context.getValue("$request/request"), "request") %>
  <%= assertEqual("Explicit Session Scope", context.getValue("$session/session"), "session") %>
  <%= assertEqual("Explicit Application Scope", context.getValue("$application/app"), "app") %>

  <jsp:include page="jxpath?parm=OK"/>
 </body>
</html>