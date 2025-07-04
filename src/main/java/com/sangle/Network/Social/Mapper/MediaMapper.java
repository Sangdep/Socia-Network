
package com.sangle.Network.Social.Mapper;

import com.sangle.Network.Social.DTO.Response.MediaResponse;
import com.sangle.Network.Social.Entity.Media;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "Spring")
public interface MediaMapper  {
    MediaResponse toMediaResponse(Media media);
}
