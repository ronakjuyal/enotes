package com.rj.Enotes_API_Service.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class FavouriteNoteDto {

    private Integer id;
    private NotesDto notes;
    private Integer userId;

}
