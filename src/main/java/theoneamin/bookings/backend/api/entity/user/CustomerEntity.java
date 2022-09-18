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

import javax.persistence.*;
import java.util.List;

/**
 * This class extends UserEntity because we want to be able to
 * create JPA relationships in entities where both customer and staff
 * are linked. For example a booking belongs to a staff and customer.
 * We want to be able to do booking.getCustomer() and booking.getStaff()
 */
@Data
@ToString(callSuper = true)
@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
@JsonNaming(PropertyNamingStrategies.LowerCamelCaseStrategy.class)
public class CustomerEntity extends UserEntity{

    @Transient
    private String fullName;

    public String getFullName() {
        return this.getFirstname()+" "+this.getLastname();
    }

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, targetEntity = BookingEntity.class, mappedBy = "customer", cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.EXTRA)
    private List<BookingEntity> bookings;
}
