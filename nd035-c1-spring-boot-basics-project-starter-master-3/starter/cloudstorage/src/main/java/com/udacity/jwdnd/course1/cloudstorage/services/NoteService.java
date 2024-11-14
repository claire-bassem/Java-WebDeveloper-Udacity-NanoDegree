package com.udacity.jwdnd.course1.cloudstorage.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.NoteForm;

@Service
public class NoteService {
    private final UserMapper userMapper;
    private final NoteMapper noteMapper;

    public NoteService(UserMapper userMapper, NoteMapper noteMapper) {
        this.userMapper = userMapper;
        this.noteMapper = noteMapper;
    }

    public Note[] getAllNotesForUser(Integer userId) {

        return noteMapper.getNotesForUser(userId);
    }

     @Transactional
    public int addNote(NoteForm noteForm, Integer userId) {
        Note note = new Note(null, noteForm.getTitle(), noteForm.getDescription(), userId);
        return noteMapper.insertNote(note);
   
    }

    public Note getNote(Integer noteId) {
        return noteMapper.getNoteById(noteId);
    }

    public void deleteNote(Integer noteId) {

        noteMapper.deleteNote(noteId);
    }


    @Transactional
    public void updateNote(NoteForm noteForm, Integer noteId) {
  
            noteMapper.updateNote(noteId, noteForm.getTitle(), noteForm.getDescription());
        
    }
}
