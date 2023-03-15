package com.project.backend.domain;

import com.project.backend.enums.ExchangeStatus;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Data
@Table(name = "EXCHANGE")
public class ExchangeEty {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "EXCHANGE_ID_SQ", allocationSize = 1)
    @Column(name = "ID")
    Long id;
    @ManyToOne
    @JoinColumn(name = "LISTED_ITEM_ID")
    private ItemEty listedItemEty;
    @ManyToOne
    @JoinColumn(name = "OFFERED_ITEM_ID")
    private ItemEty offeredItemEty;
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    ExchangeStatus status;
    @Column(name = "CREATION_TIME")
    LocalDate creationTime;

}
