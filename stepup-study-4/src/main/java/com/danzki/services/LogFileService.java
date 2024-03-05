package com.danzki.services;

import com.danzki.annotation.LogTransformation;
import com.danzki.config.Constant;
import com.danzki.model.FileData;
import org.springframework.stereotype.Service;

@Service
public class LogFileService {
    @LogTransformation
    public String save(FileData fileData) {
        return Constant.CN_EMPTY_DATE + "(username=" + fileData.getLogin() + ", application=" + fileData.getApplication() + ").";
    }
}
