<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-bean-el" prefix="bean-el"%>
<%@ taglib uri="http://struts.apache.org/tags-html-el" prefix="html-el"%>
<%@ taglib tagdir="/WEB-INF/tags" prefix="t" %>
<%@ taglib uri="http://struts.apache.org/tags-logic" prefix="logic"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><bean-el:message key="user.edit"/></title>
        <link rel="stylesheet" type="text/css" href="css/main.css">
    </head>
    <body>
        <html-el:errors />
        <html-el:form>
            <html-el:hidden property="id"/>
            <html-el:hidden property="uuid"/>
            <table border="0" width="100%">
                <tr>
                    <td>
                        <bean-el:message key="user.username" />
                    </td>
                    <td >
                        <html-el:text property="username" size="50" titleKey="user.username" errorStyleClass="error-input" errorKey="org.apache.struts.action.ERROR" />
                    </td>
                </tr>
                <tr>
                    <td>
                        <bean-el:message key="user.password" />
                    </td>
                    <td>
                        <html-el:password property="password" size="50" titleKey="user.password" errorStyleClass="error-input" errorKey="org.apache.struts.action.ERROR"/>
                    </td>
                </tr>
                <tr>
                    <td>
                        <bean-el:message key="user.email" />
                    </td>
                    <td>
                        <html-el:text property="email" size="40" maxlength="50" titleKey="user.email" />
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
                            <html-el:optionsCollection property="genderLabelValueBeans" value="value" label="label"/>
                        </html-el:select>
                    </td>
                </tr>
                <tr>
                    <td>
                        <bean-el:message key="user.maritalStatus" />
                    </td>
                    <td>
                        <logic:iterate id="bean" name="userForm" property="maritalStatusLabelValueBeans" type="org.apache.struts.util.LabelValueBean">
                            <html-el:radio property="maritalStatusStr" value="${bean.value}"/>
                            <bean-el:message key="user.maritalStatus.${bean.value}"/>
                        </logic:iterate>
                    </td>
                </tr>
                <tr>
                    <td>
                        <bean-el:message key="user.hobbyTypes" />
                    </td>
                    <td>
                        <logic:iterate id="bean" name="userForm" property="hobbyTypesLabelValueBeans">
                            <html-el:multibox property="hobbyTypesStr" value="${bean.value}" />
                            <bean-el:message key="user.hobbyTypes.${bean.value}"/>
                        </logic:iterate>
                    </td>
                </tr>
<%--                 <tr> --%>
<%--                     <td> --%>
<%--                         <bean-el:message key="user.document" /> --%>
<%--                     </td> --%>
<%--                     <td> --%>
<%--                         <html-el:file property="documentFormFile" value="${documentFormFile.fileName}"></html-el:file> --%>
<%--                     </td> --%>
<%--                 </tr> --%>
                <tr>
                    <td>
                        <bean-el:message key="user.comment" />
                    </td>
                    <td>
                        <html-el:textarea property="comment"></html-el:textarea>
                    </td>
                </tr>
            </table>
            <table border="0" width="100%">
                <tr>
                    <td>
                        <html-el:submit onclick="this.form.action='updateUser.do'">
                            <bean-el:message key="button.update" />
                        </html-el:submit>
                        <html-el:submit onclick="this.form.action='listUser.do'">
                            <bean-el:message key="button.cancel" />
                        </html-el:submit>
                    </td>
                </tr>
            </table>
        </html-el:form>
    </body>
</html>