package com.udacity.jwdnd.course1.cloudstorage.services;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;

@Service
public class FileService {
    private final FileMapper fileMapper;
    private final UserMapper userMapper;

    public FileService(FileMapper fileMapper, UserMapper userMapper) {
        this.fileMapper = fileMapper;
        this.userMapper = userMapper;
    }

    public List<File> getAllFilesForUser(Integer userId) {
        return fileMapper.getFilesForUser(userId);
    }

    public File getFileById(int fileId) {
        return fileMapper.getFileById(fileId);
    }

    public int uploadFile(MultipartFile file, Integer userId) throws IOException {
        File newFile = new File();
        newFile.setFileName(file.getOriginalFilename());
        newFile.setContentType(file.getContentType());
        newFile.setFileSize(String.valueOf(file.getSize()));
        newFile.setUserId(userId);
        newFile.setFileData(file.getBytes());
        return fileMapper.insertFile(newFile);
    }

    public void deleteFile(int fileId) {
        fileMapper.deleteFile(fileId);
    }

    public boolean isFileNameAvailable(String fileName, Integer userId) {
        return fileMapper.getFilesForUser(userId).stream()
                .noneMatch(file -> file.getFileName().equals(fileName));
    }
}
