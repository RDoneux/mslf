package com.rdoneux.mslf.util.poem;

import java.util.List;

import org.mapstruct.Mapper;

import com.rdoneux.mslf.models.Poem;
import com.rdoneux.mslf.models.PoemDTO;

@Mapper(componentModel = "spring")
public interface PoemMapper {

    PoemDTO toDTO(Poem poem);

    Poem toPoem(PoemDTO poemDTO);

    List<PoemDTO> toDTOList(List<Poem> poems);
}
