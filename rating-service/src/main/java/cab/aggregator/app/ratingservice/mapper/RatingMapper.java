package cab.aggregator.app.ratingservice.mapper;

import cab.aggregator.app.ratingservice.dto.request.RatingRequest;
import cab.aggregator.app.ratingservice.dto.response.RatingResponse;
import cab.aggregator.app.ratingservice.entity.Rating;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RatingMapper {

    RatingResponse toDto(Rating rating);

    void updateRatingFromDto(RatingRequest carRequestDto, @MappingTarget Rating rating);

    Rating toEntity(RatingRequest ratingRequest);
}
