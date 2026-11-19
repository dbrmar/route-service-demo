package com.example.route;

import com.example.route.RouteStatus;
import jakarta.persistence.*;

@Entity
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private RouteStatus status = RouteStatus.PLANNED;

    protected Route() { }            // for Hibernate only

    public Route(String name) {
        this.name = name;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public RouteStatus getStatus() { return status; }
}