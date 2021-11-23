package com.oa.core.service.system;

import com.oa.core.bean.system.FormCustomMade;

import java.util.List;

public interface FormCustomMadeService {

    void insertFormCM(FormCustomMade formCustomMade);
    int deleteFormCM(FormCustomMade formCustomMade);
    int updateFormCM(FormCustomMade formCustomMade);
    List<FormCustomMade> selectAllFormCM(FormCustomMade formCustomMade);
    List<FormCustomMade> selectAllFormPage(FormCustomMade formCustomMade);
    int selectCountFormCM(FormCustomMade formCustomMade);
    int selectCountFormPage(FormCustomMade formCustomMade);
    FormCustomMade selectFormCM(FormCustomMade formCustomMade);
    FormCustomMade selectFormCMByID(String formcmName);

    List<FormCustomMade> selectFormCMByIds(List<String> formId);
}
