package theoneamin.bookings.backend.api.entity.booking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import theoneamin.bookings.backend.api.entity.services.ServiceEntity;
import theoneamin.bookings.backend.api.entity.user.CustomerEntity;
import theoneamin.bookings.backend.api.entity.user.StaffEntity;
import theoneamin.bookings.backend.api.entity.user.UserEntity;
import theoneamin.bookings.backend.api.entity.util.DateAudited;

import javax.persistence.*;
import java.time.LocalTime;


@Data
@Entity(name = "booking")
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class BookingEntity extends DateAudited {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String bookingDate;

    @Column
    private LocalTime startTime;

    @Column
    private LocalTime endTime;

    @Column
    private Boolean status;

    @Column
    private Integer mediaId;

    @Column
    private boolean notifyCustomer;

    @JsonIgnore
    @JoinColumn(name="staffId", referencedColumnName = "user_id")
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = StaffEntity.class)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private StaffEntity staff;

    @JsonIgnore
    @JoinColumn(name="customerId", referencedColumnName = "user_id", columnDefinition = "customer_id")
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = CustomerEntity.class)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private CustomerEntity customer;

    @JsonIgnore
    @JoinColumn(name="serviceId", referencedColumnName = "service_id")
    @ManyToOne(fetch = FetchType.LAZY, targetEntity = ServiceEntity.class)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private ServiceEntity service;
}
