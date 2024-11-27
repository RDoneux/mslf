package com.rdoneux.mslf.util.blog;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.rdoneux.mslf.models.Blog;
import com.rdoneux.mslf.models.BlogDTO;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface BlogMapper {

    BlogDTO toDTO(Blog blog);

    Blog toBlog(BlogDTO blogDTO);

    List<BlogDTO> toDTOList(List<Blog> blogs);

    void updateBlogFromDTO(BlogDTO blogDTO, @MappingTarget Blog blog);

}
