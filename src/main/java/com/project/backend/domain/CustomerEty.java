package com.project.backend.domain;

import javax.persistence.*;

import lombok.Data;


@Entity
@Data
@Table(name = "CUSTOMER")
public class CustomerEty {
  @Id
  @GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "id_Sequence")
  @SequenceGenerator(name = "id_Sequence", sequenceName = "CUSTOMER_ID_SQ", allocationSize = 1)
  @Column(name = "ID")
  Long id;
  @Column(name = "USERNAME")
  String username;
  @Column(name = "EMAIL")
  String email;
  @Column(name="PASSWORD")
  String password;
}
