package com.oa.core.service.system;

import com.oa.core.bean.system.FormCustomMade;
import com.oa.core.dao.system.FormCustomMadeDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service("formCustomMadeService")
public class FormCustomMadeServiceImpl implements FormCustomMadeService {

    @Resource
    private FormCustomMadeDao formCustomMadeDao;

    @Override
    public void insertFormCM(FormCustomMade formCustomMade) {
        formCustomMadeDao.insertFormCM(formCustomMade);
    }

    @Override
    public int deleteFormCM(FormCustomMade formCustomMade) {
        return formCustomMadeDao.deleteFormCM(formCustomMade);
    }

    @Override
    public int updateFormCM(FormCustomMade formCustomMade) {
        return formCustomMadeDao.updateFormCM(formCustomMade);
    }

    @Override
    public List<FormCustomMade> selectAllFormCM(FormCustomMade formCustomMade) {
        return formCustomMadeDao.selectAllFormCM(formCustomMade);
    }

    @Override
    public List<FormCustomMade> selectAllFormPage(FormCustomMade formCustomMade) {
        return formCustomMadeDao.selectAllFormPage(formCustomMade);
    }

    @Override
    public int selectCountFormCM(FormCustomMade formCustomMade) {
        return formCustomMadeDao.selectCountFormCM(formCustomMade);
    }

    @Override
    public int selectCountFormPage(FormCustomMade formCustomMade) {
        return formCustomMadeDao.selectCountFormPage(formCustomMade);
    }

    @Override
    public FormCustomMade selectFormCM(FormCustomMade formCustomMade) {
        return formCustomMadeDao.selectFormCM(formCustomMade);
    }

    @Override
    public FormCustomMade selectFormCMByID(String formcmName) {
        return formCustomMadeDao.selectFormCMByID(formcmName);
    }

    @Override
    public List<FormCustomMade> selectFormCMByIds(List<String> formId) {
        return formCustomMadeDao.selectFormCMByIds(formId);
    }
}
