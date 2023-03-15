

package com.project.backend.domain;


import com.project.backend.enums.ItemStatus;
import com.project.backend.enums.Scope;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Data
@Table(name = "ITEM")
public class ItemEty {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_Sequence")
    @SequenceGenerator(name = "id_Sequence", sequenceName = "ITEM_ID_SQ", allocationSize = 1)
    @Column(name = "ID")
    Long id;
    @Column(name = "TITLE")
    String title;
    @Column(name = "DESCRIPTION")
    String description;
    @Column(name = "ITEM_STATUS")
    @Enumerated(EnumType.STRING)
    ItemStatus status;
    @Column(name = "CREATION_TIME")
    LocalDate creationTime;
    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID")
    private CustomerEty customerEty;
    @Column(name = "SCOPE")
    @Enumerated(EnumType.STRING)
    Scope scope;
    @Column(name = "IMAGE")
    String image;
}
