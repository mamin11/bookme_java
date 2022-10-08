package theoneamin.bookings.backend.api.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import theoneamin.bookings.backend.api.entity.booking.BookingEntity;
import theoneamin.bookings.backend.api.entity.timeslot.StaffTimeslot;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * This class extends UserEntity because we want to be able to
 * create JPA relationships in entities where both customer and staff
 * are linked. For example a booking belongs to a staff and customer.
 * We want to be able to do booking.getCustomer() and booking.getStaff()
 */
@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class StaffEntity extends UserEntity implements Serializable {

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, targetEntity = StaffServicesLink.class, mappedBy = "staff", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<StaffServicesLink> servicesLinks;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, targetEntity = StaffWorkDayLink.class, mappedBy = "staff", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<StaffWorkDayLink> workDayLinks;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, targetEntity = BookingEntity.class, mappedBy = "staff", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<BookingEntity> bookings;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(fetch = FetchType.LAZY, targetEntity = StaffTimeslot.class, mappedBy = "staff", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<StaffTimeslot> timeslots;
}
