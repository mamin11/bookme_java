package theoneamin.bookings.backend.api.entity.util;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.Instant;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class DateAudited implements Serializable {
    @CreatedDate
    @Column(name = "date_added", nullable = false, updatable = false)
    private Instant dateAdded;

    @LastModifiedDate
    @Column(name = "date_updated")
    private Instant dateUpdated;
}
