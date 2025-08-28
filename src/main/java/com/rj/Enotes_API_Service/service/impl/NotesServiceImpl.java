package com.rj.Enotes_API_Service.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rj.Enotes_API_Service.dto.FavouriteNoteDto;
import com.rj.Enotes_API_Service.dto.NotesDto;
import com.rj.Enotes_API_Service.dto.NotesResponse;
import com.rj.Enotes_API_Service.dto.NotesDto.CategoryDto;
import com.rj.Enotes_API_Service.dto.NotesDto.FileDto;
import com.rj.Enotes_API_Service.entity.FavouriteNote;
import com.rj.Enotes_API_Service.entity.FileDetails;
import com.rj.Enotes_API_Service.entity.Notes;
import com.rj.Enotes_API_Service.exception.ResourceNotFoundException;
import com.rj.Enotes_API_Service.repository.CategoryRepository;
import com.rj.Enotes_API_Service.repository.FavouriteNoteRepository;
import com.rj.Enotes_API_Service.repository.FileRepository;
import com.rj.Enotes_API_Service.repository.NotesRepository;
import com.rj.Enotes_API_Service.service.NotesService;

@Service
public class NotesServiceImpl implements NotesService {

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private FavouriteNoteRepository favouriteNoteRepository;

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CategoryRepository categoryRepo;

    @Value("{file.upload.path}")
    private String uploadPath;

    @Autowired
    private FileRepository fileRepository;

    @Override
    public Boolean saveNotes(String notes, MultipartFile file) throws Exception {

        /// string to object
        ObjectMapper ob = new ObjectMapper();
        NotesDto notesDto = ob.readValue(notes, NotesDto.class);
        notesDto.setIsDeleted(false);
        notesDto.setDeletedOn(null);
        // updating notes if id is given in the request
        if (!ObjectUtils.isEmpty(notesDto.getId())) {
            updateNotes(notesDto, file);
        }

        // category Validation notes
        checkCategoryExist(notesDto.getCategory());

        Notes notesMap = mapper.map(notesDto, Notes.class);

        FileDetails fileDetails = saveFileDetails(file);

        if (!ObjectUtils.isEmpty(fileDetails)) {
            notesMap.setFileDetails(fileDetails);
        } else {
            if (ObjectUtils.isEmpty(notesDto.getId())) {
                notesMap.setFileDetails(null);
            }

        }

        Notes saveNotes = notesRepository.save(notesMap);

        if (!ObjectUtils.isEmpty(saveNotes)) {
            return true;
        }
        return false;

    }

    private void updateNotes(NotesDto notesDto, MultipartFile file) throws Exception {

        Notes existNotes = notesRepository.findById(notesDto.getId())
                .orElseThrow(() -> new ResourceNotFoundException("invalid notes id"));
        if (ObjectUtils.isEmpty(file)) {

            notesDto.setFileDetails(mapper.map(existNotes.getFileDetails(), FileDto.class));

        }

    }

    private FileDetails saveFileDetails(MultipartFile file) throws IOException {

        if (!ObjectUtils.isEmpty(file) && !file.isEmpty()) {
            String originalFileName = file.getOriginalFilename();

            String rndString = UUID.randomUUID().toString();
            String extension = FilenameUtils.getExtension(originalFileName);
            String uploadFileName = rndString + "." + extension;

            File saveFile = new File(uploadPath);
            if (!saveFile.exists()) {
                saveFile.mkdir();
            }
            // path :enotesapiservice/notes/java.pdf

            String storePath = uploadPath.concat(uploadFileName);

            /// upload file
            long upload = Files.copy(file.getInputStream(), Paths.get(storePath));

            if (upload != 0) {
                FileDetails fileDetails = new FileDetails();
                fileDetails.setOriginalFileName(originalFileName);
                fileDetails.setDisplayFileName(getDisplayName(originalFileName));
                fileDetails.setUploadFileName(uploadFileName);
                fileDetails.setFileSize(file.getSize());
                fileDetails.setPath(storePath);
                FileDetails saveFileDetails = fileRepository.save(fileDetails);
                return saveFileDetails;
            }

        }
        return null;
    }

    private String getDisplayName(String originalFileName) {
        String extension = FilenameUtils.getExtension(originalFileName);
        String fileName = FilenameUtils.removeExtension(originalFileName);
        if (fileName.length() > 8) {
            fileName = fileName.substring(0, 7);
        }
        fileName = fileName + "." + extension;
        return fileName;
    }

    private void checkCategoryExist(CategoryDto category) throws Exception {
        categoryRepo.findById(category.getId())
                .orElseThrow(() -> new ResourceNotFoundException("category id invalid"));
    }

    @Override
    public List<NotesDto> getAllNotes() {

        return notesRepository.findAll().stream()
                .map(note -> mapper.map(note, NotesDto.class)).toList();
    }

    @Override
    public byte[] downloadFile(FileDetails fileDetails) throws Exception {

        InputStream io = new FileInputStream(fileDetails.getPath());
        return StreamUtils.copyToByteArray(io);

    }

    @Override
    public FileDetails getFileDetails(Integer id) throws Exception {
        FileDetails fileDetails = fileRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("File is not available"));

        return fileDetails;
    }

    @Override
    public NotesResponse getAllNotesByUser(Integer userId, Integer pageNo, Integer pageSize) {

        Pageable pagable = PageRequest.of(pageNo, pageSize);
        Page<Notes> pageNotes = notesRepository.findByCreatedByAndIsDeletedFalse(userId, pagable);

        List<NotesDto> notesDtos = pageNotes.get()
                .map(n -> mapper.map(n, NotesDto.class)).toList();
        NotesResponse notes = NotesResponse.builder().notes(notesDtos)
                .pageNo(pageNotes.getNumber())
                .pageSize(pageNotes.getSize())
                .totalElement(pageNotes.getTotalElements())
                .totalPages(pageNotes.getTotalPages())
                .isFirst(pageNotes.isFirst())
                .isLast(pageNotes.isLast())
                .build();

        return notes;

    }

    @Override
    public void softDeleteNotes(Integer id) throws Exception {

        Notes notes = notesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notes id invalid or not found"));

        notes.setIsDeleted(true);
        notes.setDeletedOn(LocalDateTime.now());
        notesRepository.save(notes);
    }

    @Override
    public void restoreNotes(Integer id) throws Exception {
        Notes notes = notesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Notes id invalid or not found"));

        notes.setIsDeleted(false);
        notes.setDeletedOn(null);
        notesRepository.save(notes);
    }

    @Override
    public List<NotesDto> getUserRecycleBinNotes(Integer userId) {

        List<Notes> recycleNotes = notesRepository.findByCreatedByAndIsDeletedTrue(userId);
        List<NotesDto> notesDtoList = recycleNotes
                .stream().map(note -> mapper.map(note, NotesDto.class)).toList();
        return notesDtoList;
    }

    @Override
    public void hardDeleteNotes(Integer id) throws Exception {
        Notes notes = notesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("notes not found"));

        if (notes.getIsDeleted()) {
            notesRepository.delete(notes);
        } else {
            throw new IllegalArgumentException("Sorry you cant hard delete directly ");
        }
    }

    @Override
    public void emptyRecycleBin(int userId) {
        List<Notes> recycleNotes = notesRepository.findByCreatedByAndIsDeletedTrue(userId);

        if (!CollectionUtils.isEmpty(recycleNotes)) {
            notesRepository.deleteAll(recycleNotes);
        }
    }

    @Override
    public void favouriteNotes(Integer id) throws Exception {
        int userId = 1;
        Notes notes = notesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("notes not found"));
        FavouriteNote favouriteNote = FavouriteNote.builder()
                .notes(notes)
                .userId(userId)
                .build();
        favouriteNoteRepository.save(favouriteNote);
    }

    @Override
    public void unFavouriteNotes(Integer favouriteNoteId) throws Exception {
        FavouriteNote favNotes = favouriteNoteRepository.findById(favouriteNoteId)
                .orElseThrow(() -> new ResourceNotFoundException("favourite notes not found"));

        favouriteNoteRepository.delete(favNotes);
    }

    @Override
    public List<FavouriteNoteDto> getUserFavouriteNotes() throws Exception {

        int userId = 1;
        List<FavouriteNote> favouriteNotes = favouriteNoteRepository.findByUserId(userId);
        return favouriteNotes.stream().map(fn -> mapper.map(fn, FavouriteNoteDto.class)).toList();

    }

    @Override
    public boolean copyNotes(Integer id) throws Exception {
        Notes notes = notesRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("notes id invalid not found"));

        Notes copyNote=Notes.builder()
        .title(notes.getTitle())
        .description(notes.getDescription())
        .category(notes.getCategory())
        .isDeleted(false)
        .fileDetails(null)
        .build();

        // TODO:  need to check user validation 
        
        Notes saveCopyNotes= notesRepository.save(copyNote);

        if (!ObjectUtils.isEmpty(saveCopyNotes)) {
            return true;
        }
        return false;

    }

}
