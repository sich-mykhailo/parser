package com.parser.parser.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "tokens")
public class TokenEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String value;
}
