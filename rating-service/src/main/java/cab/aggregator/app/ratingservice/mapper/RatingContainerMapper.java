package cab.aggregator.app.ratingservice.mapper;

import cab.aggregator.app.ratingservice.dto.response.RatingContainerResponse;
import cab.aggregator.app.ratingservice.dto.response.RatingResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.data.domain.Page;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RatingContainerMapper {

    default RatingContainerResponse toContainer(Page<RatingResponse> ratings) {
        return RatingContainerResponse.builder()
                .items(ratings.getContent())
                .currentPage(ratings.getNumber())
                .sizePage(ratings.getSize())
                .countPages(ratings.getTotalPages())
                .totalElements(ratings.getTotalElements())
                .build();
    }
}
