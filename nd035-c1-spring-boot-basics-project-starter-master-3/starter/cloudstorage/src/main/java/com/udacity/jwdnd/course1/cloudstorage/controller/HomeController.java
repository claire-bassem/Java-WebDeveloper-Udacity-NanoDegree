package com.udacity.jwdnd.course1.cloudstorage.controller;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.EncryptionService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final FileService fileService;
    private final UserService userService;
    private final NoteService noteService;
    private final CredentialService credentialService;
    private final EncryptionService encryptionService;

    public HomeController(
            FileService fileService, UserService userService, NoteService noteService,
            CredentialService credentialService, EncryptionService encryptionService) {
        this.fileService = fileService;
        this.userService = userService;
        this.noteService = noteService;
        this.credentialService = credentialService;
        this.encryptionService = encryptionService;
    }

    @GetMapping
    public String getHomePage(
            Authentication authentication, @ModelAttribute("newFile") FileForm newFile,
            @ModelAttribute("newNote") NoteForm newNote, @ModelAttribute("newCredential") CredentialForm newCredential,
            Model model) {
        Integer userId = userService.getUser(authentication.getName()).getUserId();

        model.addAttribute("files", this.fileService.getAllFilesForUser(userId));
        model.addAttribute("notes", noteService.getAllNotesForUser(userId));
        model.addAttribute("credentials", credentialService.getAllCredentialsForUser(userId));
        model.addAttribute("encryptionService", encryptionService);

        return "home";
    }


@PostMapping
    public String newFile(Authentication authentication, @ModelAttribute("newFile") FileForm newFile,
    @ModelAttribute("newNote") NoteForm newNote, @ModelAttribute("newCredential") CredentialForm newCredential, Model model) throws IOException {

        String userName = authentication.getName();
        User user = userService.getUser(userName);
        Integer userId = user.getUserId();
        MultipartFile multipartFile = newFile.getFile();
        String fileName = multipartFile.getOriginalFilename();
        if (fileName.isEmpty()) {
            model.addAttribute("uploadError", "Please select a file to upload.");
        } else if (!fileService.isFileNameAvailable(multipartFile.getOriginalFilename(), userId)) {
            model.addAttribute("uploadError", "File with this name already exists.");
        } else {
            fileService.uploadFile(multipartFile, userId);
            model.addAttribute("files", fileService.getAllFilesForUser(userId));
            
        }
        return "home"; 
    }
   
    @GetMapping("/get-file/{fileId}")
    public ResponseEntity<Resource> viewFile( @PathVariable("fileId") Integer fileId) {
        File file = fileService.getFileById(fileId);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFileName() + "\"")
                .body(new ByteArrayResource(file.getFileData()));
    }
   
    @GetMapping("/delete-file/{fileId}")
    public String deleteFile(@PathVariable("fileId") Integer fileId, Model model) {
 

    fileService.deleteFile(fileId);
        return "redirect:/home"; 
    }
}
