<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html-el"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean-el"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html"%>
<%@ taglib uri="http://struts.apache.org/tags-bean" prefix="bean"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<%-- <%@ page import="org.apache.struts.util.LabelValueBean"%> --%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html-el:html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><bean-el:message key="user.create" /></title>
        <link rel="stylesheet" type="text/css" href="css/main.css">
    </head>
    <body>
        <html-el:errors />
        <html-el:form action="/saveUser.do" focus="firstName" enctype="multipart/form-data">
            <table border="0" width="100%">
                <tr>
                    <td>
                        <bean-el:message key="user.username" />
                    </td>
                    <td>
                        <html-el:text property="username" size="50" errorStyleClass="error-input" errorKey="org.apache.struts.action.ERROR" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <bean-el:message key="user.password" />
                    </td>
                    <td>
                        <html-el:password property="password" size="50" errorStyleClass="error-input" errorKey="org.apache.struts.action.ERROR"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <bean-el:message key="user.email" />
                    </td>
                    <td>
                        <html-el:text property="email" size="50" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <bean-el:message key="user.birthdate" />
                    </td>
                    <td>
                        <html-el:text property="birthdateStr" size="25" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <bean-el:message key="user.gender" />
                    </td>
                    <td>
                        <html-el:select property="genderStr">
                            <html-el:option value=""><bean-el:message key="select.option.default"/></html-el:option>
                            <html-el:optionsCollection property="genderLabelValueBeans" value="value" label="label" />
                        </html-el:select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <bean-el:message key="user.maritalStatus" />
                    </td>
                    <td>
                        <logic:iterate id="item" name="userForm" property="maritalStatusLabelValueBeans" type="org.apache.struts.util.LabelValueBean">
                            <html-el:radio property="maritalStatusStr" value="${item.value}"/>
                            <bean-el:message key="user.maritalStatus.${item.value}"/>
                        </logic:iterate>  
                    </td>
                </tr>
                <tr>
                    <td>
                        <bean-el:message key="user.hobbyTypes" />
                    </td>
                    <td>
                        <logic:iterate id="item" name="userForm" property="hobbyTypesLabelValueBeans">
                            <html-el:checkbox property="hobbyTypesStr" value="${item.value}" />
                            <bean-el:message key="user.hobbyTypes.${item.value}"/>
                        </logic:iterate>
                    </td>
                </tr>
                <tr>
                    <td>
                        <bean-el:message key="user.document" />
                    </td>
                    <td>
                        <html-el:file property="documentFormFile"></html-el:file>
                    </td>
                </tr>
                <tr>
                    <td>
                        <bean-el:message key="user.comment" />
                    </td>
                    <td>
                        <html-el:textarea property="comment"></html-el:textarea>
                    </td>
                </tr>
            </table>
            <table border="0">
                <tr>
                    <td>
<%--                         <html-el:submit onclick="this.form.action='saveUser.do'"> --%>
                        <html-el:submit>
                            <bean-el:message key="button.submit" />
                        </html-el:submit>
                    </td>
                    <td>
<%--                         <html-el:submit onclick="this.form.action='listUser.do'"> --%>
<%--                             <bean-el:message key="button.cancel" /> --%>
<%--                         </html-el:submit> --%>
                        <html-el:cancel>
                            <bean-el:message key="button.cancel" />
                        </html-el:cancel>
                    </td>
                    <td>
                        <html-el:reset>
                            <bean-el:message key="button.reset" />
                        </html-el:reset>
                    </td>
                </tr>
            </table>
        </html-el:form>
    </body>
</html-el:html>