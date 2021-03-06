package com.struts.registration.web.action;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.LabelValueBean;
import org.apache.struts.util.MessageResources;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.struts.registration.dao.UserDao;
import com.struts.registration.domain.Gender;
import com.struts.registration.domain.HobbyType;
import com.struts.registration.domain.MaritalStatus;
import com.struts.registration.domain.User;
import com.struts.registration.domain.UserProperties;
import com.struts.registration.utils.PropertiesUtil;
import com.struts.registration.web.form.UserForm;

/**
 *
 * @author Shamsul Kamal
 *
 */
public class UserAction extends BaseAction {
    private final Logger logger = LoggerFactory.getLogger(UserAction.class);

    public ActionForward list(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                    HttpServletResponse response) throws Exception {
        UserDao userDao = getUserDao();
        List<User> users = userDao.findAll();
        request.setAttribute("users", users);
        return mapping.findForward("success");
    }

    public ActionForward create(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                    HttpServletResponse response) throws Exception {
        saveToken(request);
        List<LabelValueBean> genderLabelValueBeans = new ArrayList<LabelValueBean>();
        List<LabelValueBean> maritalStatusLabelValueBeans = new ArrayList<LabelValueBean>();
        List<LabelValueBean> hobbyTypesLabelValueBeans = new ArrayList<LabelValueBean>();

        MessageResources messageResources = getResources(request);

        for (Gender each : Gender.values()) {
            genderLabelValueBeans.add(new LabelValueBean(messageResources.getMessage(String.format("user.%s.%s", UserProperties.GENDER, each.getName())), each.getName()));
        }
        for (MaritalStatus each : MaritalStatus.values()) {
            maritalStatusLabelValueBeans.add(new LabelValueBean(messageResources.getMessage(String.format("user.%s.%s", UserProperties.MARITALSTATUS, each.getName())), each.getName()));
        }
        for (HobbyType each : HobbyType.values()) {
            hobbyTypesLabelValueBeans.add(new LabelValueBean(messageResources.getMessage(String.format("user.%s.%s", UserProperties.HOBBYTYPES, each.getName())), each.getName()));
        }

        UserForm userForm = (UserForm) form;
        userForm.setGenderLabelValueBeans(genderLabelValueBeans);
        userForm.setMaritalStatusLabelValueBeans(maritalStatusLabelValueBeans);
        userForm.setHobbyTypesLabelValueBeans(hobbyTypesLabelValueBeans);

        return mapping.findForward("success");
    }

    public ActionForward save(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                    HttpServletResponse response) throws Exception {
        if (isCancelled(request)) {
            return mapping.findForward("index");
        }

        ActionErrors errors = new ActionErrors();
     // Prevent unintentional duplication submissions by checking
        // that we have not received this token previously
        if (!isTokenValid(request)) {
            errors.add(
                ActionMessages.GLOBAL_MESSAGE,
                new ActionMessage("errors.token"));
        }
        resetToken(request);

        // Report any errors we have discovered back to the original form
        if (!errors.isEmpty()) {
            saveErrors(request, errors);
            saveToken(request);
            return (mapping.getInputForward());
        }

        UserForm userForm = (UserForm) form;
        User user = new User();

        if (userForm.getDocumentFormFile().getFileData().length != 0) {
            FileOutputStream outputStream = null;
//            String filePath = System.getProperty("java.io.tmpdir") + "/" + userForm.getDocumentFormFile().getFileName();
            String documentPath = System.getProperty("user.home") + PropertiesUtil.getValue("document.path") + "/" + userForm.getDocumentFormFile().getFileName();
            try {
                outputStream = new FileOutputStream(new File(documentPath));
                outputStream.write(userForm.getDocumentFormFile().getFileData());
            } catch (Exception e) {
//                ActionErrors errors = new ActionErrors();
                errors.add("document", new ActionMessage("errors.file.save", userForm.getDocumentFormFile().getFileName()));
                saveErrors(request, errors);
            } finally {
                if (outputStream != null) {
                    outputStream.close();
                }
            }
            user.setDocumentPath(documentPath);
        }

        logger.debug(">>> save user form: "+ ToStringBuilder.reflectionToString(userForm, ToStringStyle.MULTI_LINE_STYLE));

        PropertyUtils.copyProperties(user, userForm);

        UserDao userDao = getUserDao();

        userDao.save(user);
        logger.info(">>> user successfully created: " + "[" + user + "]");

        if(getErrors(request).isEmpty()){
            return mapping.findForward("success");
        } else {
            return mapping.getInputForward();
        }
    }

    public ActionForward edit(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                    HttpServletResponse response) throws Exception {
        UserForm userForm = (UserForm) form;
        User user = getUserDao().findByIdAndUuid(userForm.getId(), userForm.getUuid());

        Hibernate.initialize(user.getHobbyTypes());

        PropertyUtils.copyProperties(userForm, user);

        MessageResources messageResources = getResources(request);

        List<LabelValueBean> genderLabelValueBeans = new ArrayList<LabelValueBean>();
        for (Gender gender : Gender.values()) {
            genderLabelValueBeans.add(new LabelValueBean(messageResources.getMessage(String.format("user.%s.%s", UserProperties.GENDER, gender.getName())), gender.getName()));
        }
        userForm.setGenderLabelValueBeans(genderLabelValueBeans);

        List<LabelValueBean> maritalStatusLabelValueBeans = new ArrayList<LabelValueBean>();
        for (MaritalStatus each : MaritalStatus.values()) {
            maritalStatusLabelValueBeans.add(new LabelValueBean(messageResources.getMessage(String.format("user.%s.%s", UserProperties.MARITALSTATUS, each.getName())), each.getName()));
        }
        userForm.setMaritalStatusLabelValueBeans(maritalStatusLabelValueBeans);

        List<LabelValueBean> hobbyTypesLabelValueBeans = new ArrayList<LabelValueBean>();
        for (HobbyType each : HobbyType.values()) {
            hobbyTypesLabelValueBeans.add(new LabelValueBean(messageResources.getMessage(String.format("user.%s.%s", UserProperties.HOBBYTYPES, each.getName())), each.getName()));
        }
        userForm.setHobbyTypesLabelValueBeans(hobbyTypesLabelValueBeans);

        return mapping.findForward("success");
    }

    public ActionForward update(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                    HttpServletResponse response) throws Exception {
        UserForm userForm = (UserForm) form;
        User user = getUserDao().findByIdAndUuid(userForm.getId(), userForm.getUuid());
        PropertyUtils.copyProperties(user, userForm);
        UserDao userDao = getUserDao();
        userDao.save(user);
        return mapping.findForward("success");
    }

    public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                    HttpServletResponse response) throws Exception {

        String id = request.getParameter(UserProperties.ID);
        String uuid = request.getParameter(UserProperties.UUID);
        UserDao userDao = getUserDao();
        User user = userDao.findByIdAndUuid(Long.valueOf(id), uuid);

        UserForm userForm = (UserForm) form;
        PropertyUtils.copyProperties(userForm, user);
        MessageResources messageResources = getResources(request);

        if (user.getBirthdate() != null) {
            userForm.setBirthdateStr(new SimpleDateFormat("dd/MM/yyyy").format(user.getBirthdate()));
        }
        userForm.setGenderStr(user.getGender().getName());

        List<LabelValueBean> maritalStatusLabelValueBeans = new ArrayList<LabelValueBean>();
        for (MaritalStatus each : MaritalStatus.values()) {
            maritalStatusLabelValueBeans.add(new LabelValueBean(messageResources.getMessage(String.format("user.%s.%s", UserProperties.MARITALSTATUS, each.getName())), each.getName()));
        }
        userForm.setMaritalStatusLabelValueBeans(maritalStatusLabelValueBeans);
        userForm.setMaritalStatusStr(user.getMaritalStatus().getName());

        List<LabelValueBean> hobbyTypesLabelValueBeans = new ArrayList<LabelValueBean>();
        for (HobbyType each : HobbyType.values()) {
            hobbyTypesLabelValueBeans.add(new LabelValueBean(messageResources.getMessage(String.format("user.%s.%s", UserProperties.HOBBYTYPES, each.getName())), each.getName()));
        }
        userForm.setHobbyTypesLabelValueBeans(hobbyTypesLabelValueBeans);

        String[] hobbyTypesStr = new String[user.getHobbyTypes().size()];
        int i = 0;
        for (HobbyType each : user.getHobbyTypes()) {
            hobbyTypesStr[i] = each.getName();
            i++;
        }
        userForm.setHobbyTypesStr(hobbyTypesStr);

        return mapping.findForward("success");
    }

    public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                    HttpServletResponse response) throws Exception {
        UserForm userForm = (UserForm) form;
        UserDao userDao = getUserDao();
        User user = userDao.findById(userForm.getId());
        userDao.delete(user);
        return mapping.findForward("success");
    }
}
