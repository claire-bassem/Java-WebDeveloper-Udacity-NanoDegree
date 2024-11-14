package com.udacity.jwdnd.course1.cloudstorage.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.udacity.jwdnd.course1.cloudstorage.model.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.model.FileForm;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;

@Controller
@RequestMapping("note")
public class NoteController {

    private final NoteService noteService;
    private final UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @GetMapping
    public String getHomePage(
            Authentication authentication, @ModelAttribute("newFile") FileForm newFile, @ModelAttribute("newNote") NoteForm newNote,
            @ModelAttribute("newCredential") CredentialForm newCredential, Model model) {
        Integer userId = userService.getUser(authentication.getName()).getUserId();

        model.addAttribute("notes", this.noteService.getAllNotesForUser(userId));

        return "home";
    }


     @PostMapping("add-note")
    public String createNote(Authentication authentication, @ModelAttribute("newFile") FileForm newFile,
             @ModelAttribute("newNote") NoteForm newNote, @ModelAttribute("newCredential") CredentialForm newCredential,
             Model model) {
        String username = authentication.getName();
        Integer userId = userService.getUser(username).getUserId();
       String noteIdStr = newNote.getNoteId();
        if (!noteIdStr.isEmpty()) {
            Note existingNote = getNote(Integer.parseInt(noteIdStr));
             noteService.updateNote(newNote, existingNote.getNoteId());
       
        }
        else{
            noteService.addNote(newNote, userId);
        }
        

        model.addAttribute("notes", noteService.getAllNotesForUser(userId));
    return "home";
    }

    @GetMapping(value = "/get-note/{noteId}")
    public Note getNote(@PathVariable Integer noteId) {

        return noteService.getNote(noteId);
    }


     @GetMapping("/delete-note/{noteId}")
    public String deleteNote( Authentication authentication, @PathVariable Integer noteId, @ModelAttribute("newNote") NoteForm newNote,
             @ModelAttribute("newFile") FileForm newFile, @ModelAttribute("newCredential") CredentialForm newCredential,
             Model model) {
        noteService.deleteNote(noteId);
        Integer userId = userService.getUser(authentication.getName()).getUserId();

        model.addAttribute("notes", noteService.getAllNotesForUser(userId));
        return "home";
    }
}
