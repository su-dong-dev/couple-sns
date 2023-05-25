package com.couple.sns.domain.post.dto.response;

import static com.couple.sns.domain.post.dto.response.UserResponse.fromUser;

import com.couple.sns.domain.post.dto.LikeDto;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@AllArgsConstructor
public class LikeResponse {

    private Long typeId;
    private Long count;
    private List<UserResponse> users;


    public static LikeResponse from(Long typeId, Page<LikeDto> likes) {
        return new LikeResponse(
            typeId,
            likes.getTotalElements(),
            likes.map(likeDto -> fromUser(likeDto.getUser().getId(), likeDto.getUser().getUsername())).stream().toList()
        );
    }
}

