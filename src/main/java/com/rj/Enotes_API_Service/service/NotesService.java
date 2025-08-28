package com.rj.Enotes_API_Service.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.rj.Enotes_API_Service.dto.FavouriteNoteDto;
import com.rj.Enotes_API_Service.dto.NotesDto;
import com.rj.Enotes_API_Service.dto.NotesResponse;
import com.rj.Enotes_API_Service.entity.FileDetails;

public interface NotesService {

    public Boolean saveNotes(String  notes, MultipartFile file) throws Exception;

    public List<NotesDto>getAllNotes();

    public byte[] downloadFile(FileDetails fileDetails) throws Exception;

    public FileDetails getFileDetails(Integer id)throws Exception;

    public NotesResponse getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize);

    public void softDeleteNotes(Integer id) throws Exception;

    public void restoreNotes(Integer id) throws Exception;

    public List<NotesDto> getUserRecycleBinNotes(Integer userId);

    public void hardDeleteNotes(Integer id)throws Exception;

    public void emptyRecycleBin(int userId);

    public void favouriteNotes(Integer noteId) throws Exception;

    public void unFavouriteNotes(Integer noteId)throws Exception;

    public List<FavouriteNoteDto>getUserFavouriteNotes()throws Exception;

    public boolean copyNotes(Integer id)throws Exception;


}
