package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

@Controller
@RequestMapping("credential")
public class CredentialController {

    private final CredentialService credentialService;
    private final EncryptionService encryptionService;
    private final UserService userService;

    public CredentialController(CredentialService credentialService, EncryptionService encryptionService, UserService userService) {
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
        this.userService = userService;
    }

    @GetMapping
    public String getHomePage(
            Authentication authentication, @ModelAttribute("newFile") FileForm newFile,
            @ModelAttribute("newCredential") CredentialForm newCredential,
            @ModelAttribute("newNote") NoteForm newNote, Model model) {
        String userName = authentication.getName();
        User user = userService.getUser(userName);
        model.addAttribute("credentials", this.credentialService.getAllCredentialsForUser(user.getUserId()));
        model.addAttribute("encryptionService", encryptionService);

        return "home";
    }


    @PostMapping("/add-credential")
    public String addOrUpdateCredential( Authentication authentication, @ModelAttribute("newFile") FileForm newFile,
             @ModelAttribute("newCredential") CredentialForm newCredential,
             @ModelAttribute("newNote") NoteForm newNote, Model model) {
        String username = authentication.getName();
        
        if (newCredential.getCredentialId() == null || newCredential.getCredentialId().isEmpty()) {
            credentialService.createCredential(newCredential, username);
        } else {
            credentialService.updateCredential(newCredential, username);
        }
        User user = userService.getUser(username);
        model.addAttribute("credentials", credentialService.getAllCredentialsForUser(user.getUserId()));
        model.addAttribute("encryptionService", encryptionService);
        return "home";
    }

    @GetMapping(value = "/get-credential/{credentialId}")
    public Credential getCredential(@PathVariable Integer credentialId) {
        return credentialService.getCredential(credentialId);
    }

    @GetMapping("/delete-credential/{credentialId}")
    public String deleteCredential( Authentication authentication, @PathVariable Integer credentialId,
             @ModelAttribute("newCredential") CredentialForm newCredential,
             @ModelAttribute("newFile") FileForm newFile,
             @ModelAttribute("newNote") NoteForm newNote, Model model) {
        String username = authentication.getName();
        credentialService.deleteCredential(credentialId, username);
       User user = userService.getUser(username);
       model.addAttribute("credentials", credentialService.getAllCredentialsForUser(user.getUserId()));
       model.addAttribute("encryptionService", encryptionService);
        return "home";
    }
}
