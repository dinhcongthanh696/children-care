package childrencare.app.model;


import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "status_reservation")
@Data
@RequiredArgsConstructor
public class StatusModel {

    @Id
    @Column(name = "status_reservation_id")
    private int statusId;

    @Column(name = "status_name")
    private String statusName;

    @OneToMany(mappedBy = "statusReservation")
    List<ReservationModel> reservations;




}
