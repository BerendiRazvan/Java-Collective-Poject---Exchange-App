package com.project.backend.domain;

import com.project.backend.enums.DonationStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Data
@Table(name = "DONATION")
public class DonationEty {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "DONATION_ID_SQ", allocationSize = 1)
    @Column(name = "ID")
    Long id;
    @OneToOne
    @JoinColumn(name = "ITEM_ID")
    private ItemEty itemEty;
    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private CustomerEty customerEty;
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    DonationStatus status;
    @Column(name = "CREATION_TIME")
    LocalDate creationTime;
    @ManyToOne
    @JoinColumn(name = "RECEIVED_CUSTOMER")
    private CustomerEty receivedCustomer;
}
