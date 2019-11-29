package com.jwj.domain;

import lombok.Data;

import javax.persistence.*;


@Data
@Entity
@Table(name = "WC_USER")
public class WcUser implements java.io.Serializable {
    private static final long serialVersionUID = 4339281358624171240L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", unique = true, nullable = false, length = 19)
    private Long id;
    
    @Column(name = "USERNAME", nullable = true, length = 40)
    private String username;
    
    @Column(name = "PASSWORD", nullable = true, length = 50)
    private String password;
    

}