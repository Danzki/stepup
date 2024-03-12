package com.danzki.services;

import com.danzki.config.FileConfig;
import com.danzki.config.FileFactory;
import com.danzki.exception.InvalidSetup;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileChangeListener;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Set;

@Service
@Configurable
public class FileListener implements FileChangeListener {

    private static Logger logger = LoggerFactory.getLogger(FileListener.class);
    private String ext;
    private String delim;
    private String format;
    private FileFactory fileFactory;
    public FileListener() {
        logger.info("Create FileListener...");
    }

    public void setExt(String ext) {
        this.ext = getValidExt(ext);
    }

    public void setDelim(String delim) {
        this.delim = delim;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public void setFileFactory(FileFactory fileFactory) {
        this.fileFactory = fileFactory;
    }

    @SneakyThrows
    private String getValidExt(String ext) {
        if (ext.contains(" ")) {
            throw new InvalidSetup("Invalid ext setting. Please check your setup file.");
        }
        if (ext.substring(0, 1).equals(".")) {
            return ext;
        }
        return "."+ext;
    }

    public String getExt() {
        return ext;
    }

    @Override
    public void onChange(Set<ChangedFiles> changeSet) {
        logger.info("OnChange started...");
        for(ChangedFiles cfiles : changeSet) {
            for(ChangedFile cfile: cfiles.getFiles()) {
                if (!cfile.getFile().getName().contains(this.ext)) {
                    continue;
                }
                if((cfile.getType().equals(ChangedFile.Type.MODIFY)
                        || cfile.getType().equals(ChangedFile.Type.ADD))
                        && !isLocked(cfile.getFile().toPath())) {
                    fileDataSave(cfile.getFile());
                }
            }
        }
        logger.info("OnChange finished");
    }

    private boolean isLocked(Path path) {
        try (FileChannel ch = FileChannel.open(path, StandardOpenOption.WRITE); FileLock lock = ch.tryLock()) {
            return lock == null;
        } catch (IOException e) {
            return true;
        }
    }

    private void fileDataSave(File file) {
        var fileProcessor = fileFactory.fileDataSaver(file);
        fileProcessor.setDelim(delim);
        fileProcessor.setFormat(format);
        if (fileProcessor.isRightExtention(file, ext)) {
            fileProcessor.process();
        }
    }


}
