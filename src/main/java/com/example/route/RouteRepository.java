package com.example.route;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<com.example.route.Route, Long> {
    List<Route> findByStatus(RouteStatus status);
}
