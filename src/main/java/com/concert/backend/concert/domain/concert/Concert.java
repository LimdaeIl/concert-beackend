package com.concert.backend.concert.domain.concert;

import com.concert.backend.common.audit.BaseAuditEntity;
import com.concert.backend.concert.domain.concertschedule.ConcertSchedule;
import com.concert.backend.concert.exception.ConcertErrorCode;
import com.concert.backend.concert.exception.ConcertException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "v1_concerts")
@Entity
public class Concert extends BaseAuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 100)
    private String title;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "organizer", nullable = false, length = 100)
    private String organizer;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private ConcertStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private ConcertCategory category;

    @OrderBy("performanceAt ASC")
    @OneToMany(
            mappedBy = "concert",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ConcertSchedule> schedules = new ArrayList<>();

    @OrderBy("sortOrder ASC")
    @OneToMany(
            mappedBy = "concert",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<ConcertImage> images = new ArrayList<>();

    private Concert(
            String title,
            String description,
            String organizer,
            ConcertCategory category
    ) {
        validateTitle(title);
        validateDescription(description);
        validateOrganizer(organizer);
        validateCategory(category);

        this.title = title;
        this.description = description;
        this.organizer = organizer;
        this.category = category;
        this.status = ConcertStatus.UPCOMING;
    }

    public static Concert create(
            String title,
            String description,
            String organizer,
            ConcertCategory category
    ) {
        return new Concert(title, description, organizer, category);
    }

    public void addSchedule(ConcertSchedule schedule) {
        if (schedule == null) {
            throw new ConcertException(
                    ConcertErrorCode.NULL_CONCERT_SCHEDULE
            );
        }

        schedules.add(schedule);
        schedule.assignConcert(this);
    }

    public void addImage(ConcertImage image) {
        if (image == null) {
            throw new ConcertException(
                    ConcertErrorCode.NULL_CONCERT_IMAGE
            );
        }

        images.add(image);
        image.assignConcert(this);
    }

    public void validateImages() {
        long representativeCount = images.stream()
                .filter(ConcertImage::isRepresentative)
                .count();

        if (representativeCount != 1) {
            throw new ConcertException(
                    ConcertErrorCode.INVALID_REPRESENTATIVE_IMAGE_COUNT
            );
        }
    }

    private void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new ConcertException(
                    ConcertErrorCode.INVALID_TITLE
            );
        }
    }

    private void validateDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new ConcertException(
                    ConcertErrorCode.INVALID_DESCRIPTION
            );
        }
    }

    private void validateOrganizer(String organizer) {
        if (organizer == null || organizer.isBlank()) {
            throw new ConcertException(
                    ConcertErrorCode.INVALID_ORGANIZER
            );
        }
    }

    private void validateCategory(ConcertCategory category) {
        if (category == null) {
            throw new ConcertException(
                    ConcertErrorCode.INVALID_CATEGORY
            );
        }
    }
}
