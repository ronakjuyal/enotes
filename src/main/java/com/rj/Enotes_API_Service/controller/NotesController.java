package com.rj.Enotes_API_Service.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.rj.Enotes_API_Service.dto.FavouriteNoteDto;
import com.rj.Enotes_API_Service.dto.NotesDto;
import com.rj.Enotes_API_Service.dto.NotesResponse;
import com.rj.Enotes_API_Service.entity.FileDetails;
import com.rj.Enotes_API_Service.service.NotesService;
import com.rj.Enotes_API_Service.util.CommonUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

@RestController
@RequestMapping("api/v1/notes")
public class NotesController {

    @Autowired
    private NotesService notesService;

    @PostMapping("/")
    public ResponseEntity<?> saveNotes(@RequestParam String notes, @RequestParam(required = false) MultipartFile file)
            throws Exception {

        Boolean saveNotes = notesService.saveNotes(notes, file);
        if (saveNotes) {
            return CommonUtil.createBuildResponseMessage("notes saved success", HttpStatus.CREATED);
        }
        return CommonUtil.createErrorResponseMessage("notes not saved", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable Integer id) throws Exception {

        FileDetails fileDetails = notesService.getFileDetails(id);
        byte[] data = notesService.downloadFile(fileDetails);
        HttpHeaders headers = new HttpHeaders();
        String contentType = CommonUtil.getContentType(fileDetails.getOriginalFileName());
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.setContentDispositionFormData("attachment", fileDetails.getOriginalFileName());
        return ResponseEntity.ok().headers(headers).body(data);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllNotes() {

        List<NotesDto> notes = notesService.getAllNotes();
        if (CollectionUtils.isEmpty(notes)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @GetMapping("/user-notes")
    public ResponseEntity<?> getAllNotesByUser(@RequestParam(name = "pageNo", defaultValue = "0") Integer pageNo,
            @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize) {

        Integer userId = 1;
        NotesResponse notes = notesService.getAllNotesByUser(userId, pageNo, pageSize);

        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> deleteNotes(@PathVariable Integer id) throws Exception {

        notesService.softDeleteNotes(id);
        return CommonUtil.createBuildResponseMessage("Delete success", HttpStatus.OK);

    }

    @GetMapping("/restore/{id}")
    public ResponseEntity<?> restoreNotes(@PathVariable Integer id) throws Exception {

        notesService.restoreNotes(id);
        return CommonUtil.createBuildResponseMessage("notes restored successfully", HttpStatus.OK);

    }

    @GetMapping("/recycle-bin")
    public ResponseEntity<?> getUserRecycleBinNotes() throws Exception {

        Integer userId = 1;
        List<NotesDto> notes = notesService.getUserRecycleBinNotes(userId);

        if (ObjectUtils.isEmpty(notes)) {
            return CommonUtil.createBuildResponseMessage("no notes availabe to restore", HttpStatus.OK);

        }
        return CommonUtil.createBuildResponse(notes, HttpStatus.OK);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> hardDeleteNotes(@PathVariable Integer id) throws Exception {

        notesService.hardDeleteNotes(id);
        return CommonUtil.createBuildResponseMessage("Delete success", HttpStatus.OK);

    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> emptyRecycleBin() throws Exception {

        int userId = 1;
        notesService.emptyRecycleBin(userId);
        return CommonUtil.createBuildResponseMessage("Delete success", HttpStatus.OK);

    }

    @GetMapping("/fav/{noteId}")
    public ResponseEntity<?> favouriteNotes(@PathVariable Integer noteId) throws Exception {

        int userId = 1;
        notesService.favouriteNotes(noteId);
        return CommonUtil.createBuildResponseMessage("notes added to favourite", HttpStatus.CREATED);
    }

    @DeleteMapping("/un-fav/{favNoteId}")
    public ResponseEntity<?> unFavouriteNote(@PathVariable Integer favNoteId) throws Exception {

        int userId = 1;
        notesService.unFavouriteNotes(favNoteId);
        return CommonUtil.createBuildResponseMessage("remove favourite", HttpStatus.OK);

    }

    @GetMapping("/fav-note")
    public ResponseEntity<?> getUserFavouriteNote() throws Exception {

        int userId = 1;
        List<FavouriteNoteDto> userFavouriteNotes = notesService.getUserFavouriteNotes();

        if (CollectionUtils.isEmpty(userFavouriteNotes)) {
            return ResponseEntity.noContent().build();
        }
        return CommonUtil.createBuildResponse(userFavouriteNotes, HttpStatus.OK);

    }

    @GetMapping("/copy/{id}")
    public ResponseEntity<?> getUserFavouriteNote(@PathVariable Integer id) throws Exception {

        Boolean copyNotes = notesService.copyNotes(id);
        if (copyNotes) {
            return CommonUtil.createBuildResponseMessage("copied successfully", HttpStatus.OK);

        }
        return CommonUtil.createErrorResponseMessage("Copy failed , try again ", HttpStatus.INTERNAL_SERVER_ERROR);

    }

}
