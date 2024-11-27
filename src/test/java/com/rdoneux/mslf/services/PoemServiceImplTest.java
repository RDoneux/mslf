package com.rdoneux.mslf.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.rdoneux.mslf.controllers.exceptions.EntityNotFoundException;
import com.rdoneux.mslf.models.Poem;
import com.rdoneux.mslf.models.PoemDTO;
import com.rdoneux.mslf.repositories.PoemRepository;
import com.rdoneux.mslf.util.poem.PoemMapper;

public class PoemServiceImplTest {

    @Mock
    private PoemRepository poemRepository;

    @Mock
    private PoemMapper poemMapper;

    @InjectMocks
    private PoemServiceImpl poemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private final Poem mockPoem = Poem.builder()
            .id("6f5d0c97-eb0e-46d1-8e5a-9b99b27c1caa")
            .title("mock-poem-title")
            .author("mock-poem-author").build();

    private final PoemDTO mockPoemDTO = PoemDTO.builder()
            .id("25ccbbb8-fc65-448f-b69c-5000b02d1108")
            .title("mock-poemDTO-title")
            .author("mock-poemDTO-author").build();

    /*
     **********
     * FIND ALL
     **********
     */

    @Test
    void shouldReturnListOfPoemDTOFromRepository() {

        when(poemRepository.findAll()).thenReturn(List.of(mockPoem));
        when(poemMapper.toDTOList(List.of(mockPoem))).thenReturn(List.of(mockPoemDTO));

        List<PoemDTO> result = poemService.findAll();

        assertThat(result).hasSize(1);
        PoemDTO returnedPoem = result.get(0);
        assertThat(returnedPoem.getId()).isEqualTo(mockPoemDTO.getId());
        assertThat(returnedPoem.getTitle()).isEqualTo(mockPoemDTO.getTitle());
        assertThat(returnedPoem.getAuthor()).isEqualTo(mockPoemDTO.getAuthor());

        verify(poemRepository, times(1)).findAll();
        verify(poemMapper, times(1)).toDTOList(anyList());

    }

    /*
     *********************
     * FIND ALL PAGINATION
     *********************
     */

    @Test
    void shouldReturnPoemDTOPage() {
        Integer page = 0;
        Integer size = 10;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString("asc"), "createdAt"));

        when(poemRepository.findAll(pageable)).thenReturn(new PageImpl<Poem>(List.of(mockPoem)));
        when(poemMapper.toDTO(mockPoem)).thenReturn(mockPoemDTO);

        Page<PoemDTO> result = poemService.findAll(page, size);

        List<PoemDTO> poems = result.get().toList();
        assertThat(poems).hasSize(1);
        assertThat(poems.get(0).getId()).isEqualTo(mockPoemDTO.getId());

        verify(poemRepository, times(1)).findAll(pageable);
        verify(poemMapper, times(1)).toDTO(mockPoem);
    }

    /*
    *************
    * FIND BY ID
    *************
    */
    @Test
    void shouldReturnPoemDTOIfFound() {
        String id = "valid-id";

        when(poemRepository.findById(id)).thenReturn(Optional.of(mockPoem));
        when(poemMapper.toDTO(mockPoem)).thenReturn(mockPoemDTO);

        PoemDTO result = poemService.findById(id);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(mockPoemDTO.getId());

        verify(poemRepository, times(1)).findById(id);
        verify(poemMapper, times(1)).toDTO(mockPoem);
    }

    @Test
    void shouldThrowPoemEntityNotFoundExceptionIfNotFound() {
        String id = "not-found-id";

        when(poemRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> poemService.findById(id));

        verify(poemRepository, times(1)).findById(id);
        verify(poemMapper, times(0)).toDTO(mockPoem);
    }

    /*
    **************
    * CREATE POEM
    **************
    */
    @Test
    void shouldReturnPoemDTOOnceSavedCreatePoem() {
        when(poemRepository.save(mockPoem)).thenReturn(mockPoem);
        when(poemMapper.toPoem(mockPoemDTO)).thenReturn(mockPoem);
        when(poemMapper.toDTO(mockPoem)).thenReturn(mockPoemDTO);

        PoemDTO result = poemService.createPoem(mockPoemDTO);

        assertThat(result.getId()).isEqualTo(mockPoemDTO.getId());

        verify(poemRepository, times(1)).save(mockPoem);
        verify(poemMapper, times(1)).toDTO(mockPoem);
        verify(poemMapper, times(1)).toPoem(mockPoemDTO);
    }

    /*
    **************
    * UPDATE POEM
    **************
    */
    @Test
    void shouldReturnPoemDTOOnceSavedUpdatePoem() {
        String id = "test-id";

        when(poemRepository.save(any())).thenReturn(mockPoem);
        when(poemMapper.toPoem(any())).thenReturn(mockPoem);
        when(poemMapper.toDTO(mockPoem)).thenReturn(mockPoemDTO);

        PoemDTO result = poemService.updatePoem(id, mockPoemDTO);

        assertThat(result.getId()).isEqualTo(mockPoemDTO.getId());

        verify(poemRepository, times(1)).save(mockPoem);
        verify(poemMapper, times(1)).toDTO(mockPoem);
        verify(poemMapper, times(1)).toPoem(any());
    }

    /*
    **************
    * UPDATE POEM
    **************
    */
    @Test
    void shouldCallDeleteByIdOnRepository() {

        String id = "test-id";

        poemService.deletePoem(id);

        verify(poemRepository, times(1)).deleteById(id);
    }
}
