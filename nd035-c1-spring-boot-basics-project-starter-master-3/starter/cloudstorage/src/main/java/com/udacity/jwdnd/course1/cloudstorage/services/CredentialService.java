package com.udacity.jwdnd.course1.cloudstorage.services;

import java.security.SecureRandom;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
@Service
public class CredentialService {
    private final UserMapper userMapper;
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(UserMapper userMapper, CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.userMapper = userMapper;
        this.credentialMapper = credentialMapper;
        this.encryptionService =encryptionService;
    }

    public Credential[] getAllCredentialsForUser(Integer userId) {

        return credentialMapper.getCredentialsForUser(userId);
    }

    public Credential getCredential(Integer noteId) {

        return credentialMapper.getCredentialById(noteId);
    }

    public void createCredential(CredentialForm credentialForm, String username) {
        User user = userMapper.getUser(username);
        String password = credentialForm.getPassword();
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(password, encodedKey);

        Credential credential = new Credential(null, credentialForm.getUrl(), credentialForm.getUserName(),encodedKey , encryptedPassword, user.getUserId());
        credentialMapper.insertCredential(credential);
    }

    public void updateCredential(CredentialForm credentialForm, String username) {
        Credential existingCredential = credentialMapper.getCredentialById(Integer.parseInt(credentialForm.getCredentialId()));
    
        // Retain the existing key 
        String encodedKey = existingCredential.getKey();
        String encryptedPassword = encryptionService.encryptValue(credentialForm.getPassword(), encodedKey);
    
        existingCredential.setUrl(credentialForm.getUrl());
        existingCredential.setUserName(credentialForm.getUserName());
        existingCredential.setPassword(encryptedPassword);
    
        credentialMapper.updateCredential(existingCredential.getCredentialid(),
                                          existingCredential.getUserName(),
                                          existingCredential.getUrl(),
                                          encodedKey,
                                          existingCredential.getPassword());
    }

     public void deleteCredential(Integer credentialId, String username) {
         credentialMapper.deleteCredential(credentialId);
    }
}
