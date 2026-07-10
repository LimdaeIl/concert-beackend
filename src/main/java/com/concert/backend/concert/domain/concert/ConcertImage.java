package com.concert.backend.concert.domain.concert;

import com.concert.backend.common.audit.BaseAuditEntity;
import com.concert.backend.concert.exception.ConcertErrorCode;
import com.concert.backend.concert.exception.ConcertException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "v1_concert_images")
@Entity
public class ConcertImage extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url", nullable = false, length = 500)
    private String imageUrl;

    @Column(name = "sort_order", nullable = false)
    private int sortOrder;

    @Column(name = "representative", nullable = false)
    private boolean representative;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "concert_id", nullable = false)
    private Concert concert;

    private ConcertImage(
            String imageUrl,
            int sortOrder,
            boolean representative
    ) {
        validateImageUrl(imageUrl);
        validateSortOrder(sortOrder);

        this.imageUrl = imageUrl;
        this.sortOrder = sortOrder;
        this.representative = representative;
    }

    public static ConcertImage create(
            String imageUrl,
            int sortOrder,
            boolean representative
    ) {
        return new ConcertImage(
                imageUrl,
                sortOrder,
                representative
        );
    }

    public void assignConcert(Concert concert) {
        if (concert == null) {
            throw new ConcertException(
                    ConcertErrorCode.INVALID_CONCERT_REFERENCE
            );
        }

        this.concert = concert;
    }

    private void validateImageUrl(String imageUrl) {
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new ConcertException(
                    ConcertErrorCode.INVALID_IMAGE_URL
            );
        }
    }

    private void validateSortOrder(int sortOrder) {
        if (sortOrder < 0) {
            throw new ConcertException(
                    ConcertErrorCode.INVALID_IMAGE_SORT_ORDER,
                    sortOrder
            );
        }
    }
}
