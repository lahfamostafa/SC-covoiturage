package ma.youcode.covoiturage.commun.dto;

import java.util.List;
import org.springframework.data.domain.Page;

public record PageResponseDTO<T>(List<T> content, long totalElements, int totalPages, int page, int size) {
    public static <T> PageResponseDTO<T> from(Page<T> data) {
        return new PageResponseDTO<>(
                data.getContent(),
                data.getTotalElements(),
                data.getTotalPages(),
                data.getNumber(),
                data.getSize()
        );
    }
}